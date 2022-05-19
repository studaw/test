package com.blog.service.impl;

import com.blog.pojo.Admin;
import com.blog.mapper.AdminMapper;
import com.blog.service.AdminService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author blog
 * @since 2022-04-11
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {

}
