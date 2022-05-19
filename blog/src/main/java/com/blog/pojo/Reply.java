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
public class Reply implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 评论回复id
     */
    @TableId(value = "comment_reply_id")
    private String commentReplyId;

    /**
     * 评论id
     */
    private String commentId;

    /**
     * 回复人的id
     */
    private String replyUserId;

    /**
     * 继续回复人的id
     */
    private String secondlyUserId;

    /**
     * 评论回复的时间
     */
    private Date commentReplyTime;

    /**
     * 回复的内容
     */
    private String replyContext;
}
