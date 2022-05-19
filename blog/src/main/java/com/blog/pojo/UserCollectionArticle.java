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
 * 用户收藏的文章
 * </p>
 *
 * @author blog
 * @since 2022-04-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class UserCollectionArticle implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 用户收藏的文章id
     */
    @TableId(value = "user_collection_article_id")
    private String userCollectionArticleId;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 文章id
     */
    private String articleId;

    /**
     * 收藏时间
     */
    private Date userCollectionArticleTime;


}
