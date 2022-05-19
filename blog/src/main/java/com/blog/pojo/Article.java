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
public class Article implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 文章id
     */
    @TableId(value = "article_id")
    private String articleId;

    /**
     * 文章标题
     */
    private String articleTitle;

    /**
     * 文章添加时间
     */
    private Date articleAddTime;

    /**
     * 文章内容
     */
    private String articleContext;

    /**
     * 点赞次数
     */
    private Integer articleGoodNumber;

    /**
     * 观看次数
     */
    private Integer articleLookNumber;

    /**
     * 收藏次数
     */
    private Integer articleCollectionNumber;

    /**
     * 用户id
     */
    private String userId;

    private String articleTypeId;

    /**
     * 文章封面
     */
    private String articleCoverUrl;
    /**
     *
     * 热门文章 0是 1不是
     */
    private Integer articleHot;
}
