package com.blog.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.Version;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author blog
 * @since 2022-04-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class User implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 用户id
     */
    @TableId(value = "user_id")
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
     * 注册时间
     */
    private Date userRegisterTime;

    /**
     * 是否冻结，0正常，1冻结
     */
    private Integer userFrozen;

    /**
     * 是否可以发布文章 0不能，1可以发布
     */
    private Integer userPublishArticle;

}
