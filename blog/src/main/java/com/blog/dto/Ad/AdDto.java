package com.blog.dto.Ad;

import lombok.Data;

import java.util.Date;

/**
 * @program: blog
 * @description:
 **/
@Data
public class AdDto {

    private String adId;

    /**
     * 广告标题
     */
    private String adTitle;

    /**
     * 广告url
     */
    private String adImgUrl;

    /**
     *  广告url链接
     */
    private String adLinkUrl;

    /**
     * 广告排序，越小越在后面
     */
    private Integer adSort;

    /**
     * 广告类型
     */
    private String adTypeId;

    /**
     * 广告开始时间
     */
    private String adBeginTime;

    /**
     * 广告结束时间
     */
    private String adEndTime;

    /**
     * 添加广告时间
     */
    private String adAddTime;


    private String adTypeTitle;
}
