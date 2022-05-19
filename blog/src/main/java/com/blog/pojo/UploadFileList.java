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
 * @since 2022-04-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class UploadFileList implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 上传文件的列表
     */
    @TableId(value = "upload_file_list_id")
    private String uploadFileListId;

    /**
     * 文件大小，作为文件唯一标识
     */
    private Long fileSize;

    /**
     * 文件路径url
     */
    private String fileUrl;

    /**
     * 文件上传时间
     */
    private Date uploadFileTime;


}
