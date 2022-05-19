package com.blog.dto.user;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * @program: blog
 * @description:
 **/

@Data
public class UserDto {


    @NotBlank(message = "用户id不能为空")
    private String userId;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 用户名
     */
    private String username;


    /**
     * 是否冻结，0正常，1冻结
     */
    private Integer userFrozen;

    /**
     * 是否可以发布文章 0不能，1可以发布
     */
    private Integer userPublishArticle;

}
