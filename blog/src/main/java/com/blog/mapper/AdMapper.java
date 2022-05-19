package com.blog.mapper;

import com.blog.pojo.Ad;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blog.vo.AdVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author blog
 * @since 2022-04-06
 */
public interface AdMapper extends BaseMapper<Ad> {

    List<AdVo> adList(@Param("adTypeId") String adTypeId);
}
