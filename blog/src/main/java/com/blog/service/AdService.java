package com.blog.service;

import com.blog.pojo.Ad;
import com.baomidou.mybatisplus.extension.service.IService;
import com.blog.vo.AdVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author blog
 * @since 2022-04-06
 */
public interface AdService extends IService<Ad> {

    /**
     * 广告标签属性获取
     * @param adTypeId
     * @return
     */
    List<AdVo> adList(String adTypeId);
}
