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
public class Comment implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 文章评论id
     */
    @TableId(value = "comment_id")
    private String commentId;

    /**
     * 文章id
     */
    private String articleId;

    /**
     * 评论时间
     */
    private Date commentTime;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 点赞次数
     */
    private Integer commentGoodNumber;

    /**
     * 评论内容
     */
    private String commentContext;
}
