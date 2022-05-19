package com.blog.service.impl;

import com.blog.pojo.Ad;
import com.blog.mapper.AdMapper;
import com.blog.service.AdService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.blog.vo.AdVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author blog
 * @since 2022-04-06
 */
@Service
public class AdServiceImpl extends ServiceImpl<AdMapper, Ad> implements AdService {

    @Autowired
    AdMapper adMapper;

    /**
     * 广告标签属性获取
     * @param adTypeId
     * @return
     */
    @Override
    public List<AdVo> adList(String adTypeId) {
        return adMapper.adList(adTypeId);
    }
}
