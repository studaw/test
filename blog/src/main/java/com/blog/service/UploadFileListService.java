package com.blog.service;

import com.blog.pojo.UploadFileList;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author blog
 * @since 2022-04-12
 */
public interface UploadFileListService extends IService<UploadFileList> {

    String getUploadFileUrl(MultipartFile file);
}
