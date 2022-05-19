package com.blog.vo;

import lombok.Data;

import java.util.Date;

/**
 * @program: blog
 * @description:
 **/
@Data
public class ArticleVo {

    private String articleId;

    private String userName;
    /**
     * 文章标题
     */
    private String articleTitle;

    /**
     * 文章添加时间
     */
    private Date articleAddTime;


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

    private String articleTypeName;

    private String articleCoverUrl;

    private String articleContext;
}
