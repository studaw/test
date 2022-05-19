package com.blog.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.blog.pojo.Comment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.blog.vo.CommentVo;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author blog
 * @since 2022-04-06
 */
public interface CommentMapper extends BaseMapper<Comment> {

    IPage<CommentVo> getArticleCommentList(Page<CommentVo> commentVoPage, String articleId);
}
