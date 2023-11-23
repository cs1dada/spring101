package org.dandan.system.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.NoArgsConstructor;
import org.dandan.security.enums.OAuth2Provider;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * 系统用户
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sys_user")
public class SysUser implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @Id
    @Column(name = "user_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "sys_users_roles",
            joinColumns = {@JoinColumn(name = "user_id",referencedColumnName = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id",referencedColumnName = "role_id")})
    private Set<Role> roles;

    /**
     * 部门名称
     */
    @Column(name = "dept_id")
    private Long deptId;

    /**
     * 用户名
     */
    @Column(name = "username")
    private String username;

    /**
     * 昵称
     */
    @Column(name = "nick_name")
    private String nickName;

    /**
     * 性别
     */
    @Column(name = "gender")
    private String gender;

    /**
     * 手机号码
     */
    @Column(name = "phone")
    private String phone;

    /**
     * 邮箱
     */
    @Column(name = "email")
    private String email;

    /**
     * 头像地址
     */
    @Column(name = "avatar_name")
    private String avatarName;

    /**
     * 头像真实路径
     */
    @Column(name = "avatar_path")
    private String avatarPath;

    /**
     * 密码
     */
    @Column(name = "password")
    private String password;

    /**
     * 是否为admin账号
     */
    @Column(name = "is_admin")
    private Boolean admin;

    /**
     * 状态：1启用、0禁用
     */
    @Column(name = "enabled")
    private Long enabled;

    /**
     * 创建者
     */
    @Column(name = "create_by")
    private String createBy;

    /**
     * 更新者
     */
    @Column(name = "update_by")
    private String updateBy;

    /**
     * 修改密码的时间
     */
    @Column(name = "pwd_reset_time")
    private Date pwdResetTime;

    /**
     * 创建日期
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    @Enumerated(EnumType.STRING)
    private OAuth2Provider provider;
}
