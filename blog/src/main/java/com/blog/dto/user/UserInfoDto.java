package com.blog.dto.user;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * @program: blog
 * @description:
 **/
@Data
public class UserInfoDto {
    /**
     * 用户密码
     */
    @NotBlank(message = "客官 请填写用户名哦！")
    @Size(max = 25,min = 3,message = "用户名长度在 3-25之间")
    private String password;

    /**
     * 用户名
     */
    @NotBlank(message = "客官 请填写密码哦！")
    @Size(max = 25,min = 3,message = "密码长度在 3-25之间")
    private String username;

    @NotBlank(message = "客官 请填写验证码哦！")
    private String verifyCode;
}
