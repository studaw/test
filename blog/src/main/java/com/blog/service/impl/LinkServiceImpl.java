package com.blog.service.impl;

import com.blog.pojo.Link;
import com.blog.mapper.LinkMapper;
import com.blog.service.LinkService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author blog
 * @since 2022-04-06
 */
@Service
public class LinkServiceImpl extends ServiceImpl<LinkMapper, Link> implements LinkService {

}
