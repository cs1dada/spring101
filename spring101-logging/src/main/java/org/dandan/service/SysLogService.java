package org.dandan.service;


import cn.hutool.core.lang.Dict;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.dandan.annotation.Log;
import org.dandan.domain.SysLog;
import org.dandan.dto.SysLogDTO;
import org.dandan.repository.SysLogRepository;
import org.dandan.utils.StringUtils;
import org.dandan.vo.SysLogQueryVO;
import org.dandan.vo.SysLogUpdateVO;
import org.dandan.vo.SysLogVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

@Service
public class SysLogService {

    @Autowired
    private SysLogRepository sysLogRepository;

    public Long save(SysLogVO vO) {
        SysLog bean = new SysLog();
        BeanUtils.copyProperties(vO, bean);
        bean = sysLogRepository.save(bean);
        return bean.getLogId();
    }

    public void delete(Long id) {
        sysLogRepository.deleteById(id);
    }

    public void update(Long id, SysLogUpdateVO vO) {
        SysLog bean = requireOne(id);
        BeanUtils.copyProperties(vO, bean);
        sysLogRepository.save(bean);
    }

    public SysLogDTO getById(Long id) {
        SysLog original = requireOne(id);
        return toDTO(original);
    }

    public Page<SysLogDTO> query(SysLogQueryVO vO) {
        throw new UnsupportedOperationException();
    }

    private SysLogDTO toDTO(SysLog original) {
        SysLogDTO bean = new SysLogDTO();
        BeanUtils.copyProperties(original, bean);
        return bean;
    }

    private SysLog requireOne(Long id) {
        return sysLogRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Resource not found: " + id));
    }

    /**
     * 保存日志数据
     * @param username 用户
     * @param browser 浏览器
     * @param ip 请求IP
     * @param joinPoint /
     * @param sysLog 日志实体
     */
    @Async
    @Transactional(rollbackFor = Exception.class)
    public void save(String username, String browser, String ip, ProceedingJoinPoint joinPoint, SysLog sysLog){

        if (sysLog == null) {
            throw new IllegalArgumentException("Log 不能为 null!");
        }
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Log aopLog = method.getAnnotation(Log.class);

        // 方法路径
        String methodName = joinPoint.getTarget().getClass().getName() + "." + signature.getName() + "()";

        // 描述
        sysLog.setDescription(aopLog.value());

        sysLog.setRequestIp(ip);
        sysLog.setAddress(StringUtils.getCityInfo(sysLog.getRequestIp()));
        sysLog.setMethod(methodName);
        sysLog.setUsername(username);
        sysLog.setParams(getParameter(method, joinPoint.getArgs()));
        // 记录登录用户，隐藏密码信息
        if(signature.getName().equals("login") && StringUtils.isNotEmpty(sysLog.getParams())){
            JSONObject obj = JSON.parseObject(sysLog.getParams());
            sysLog.setUsername(obj.getString("username"));
            sysLog.setParams(JSON.toJSONString(Dict.create().set("username", sysLog.getUsername())));
        }
        sysLog.setBrowser(browser);
        sysLogRepository.save(sysLog);
    }

    /**
     * 根据方法和传入的参数获取请求参数
     */
    private String getParameter(Method method, Object[] args) {
        List<Object> argList = new ArrayList<>();
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            // 过滤掉不能序列化的类型: MultiPartFile
            if (args[i] instanceof MultipartFile) {
                continue;
            }
            //将RequestBody注解修饰的参数作为请求参数
            RequestBody requestBody = parameters[i].getAnnotation(RequestBody.class);
            if (requestBody != null) {
                argList.add(args[i]);
            }
            //将RequestParam注解修饰的参数作为请求参数
            RequestParam requestParam = parameters[i].getAnnotation(RequestParam.class);
            if (requestParam != null) {
                Map<String, Object> map = new HashMap<>(2);
                String key = parameters[i].getName();
                if (!StringUtils.isEmpty(requestParam.value())) {
                    key = requestParam.value();
                }
                map.put(key, args[i]);
                argList.add(map);
            }
        }
        if (argList.isEmpty()) {
            return "";
        }
        return argList.size() == 1 ? JSON.toJSONString(argList.get(0)) : JSON.toJSONString(argList);
    }
}
