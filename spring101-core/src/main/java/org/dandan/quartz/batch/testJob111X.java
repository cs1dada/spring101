package org.dandan.quartz.batch;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class testJob111X {
    public void run(){
        log.info("run 执行成功");
    }

    public void run1(String str){
        log.info("run1 执行成功，参数为： {}" + str);
    }

    public void run2(){
        log.info("run2 执行成功");
    }
}
