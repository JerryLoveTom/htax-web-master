package com.htax.modules.oss.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.htax.common.utils.PageUtils;
import com.htax.modules.oss.entity.SysNativeOssEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;


/**
 * 本地文件存储记录表
 *
 * @author ppzz
 * @email 171712007@qq.com
 * @date 2022-06-18 14:58:34
 */
public interface SysNativeOssService extends IService<SysNativeOssEntity> {
    // 保存文件
    Map<String, String> saveFile(MultipartFile file);
    // 保存py文件
    Map<String, String> pathUpload(MultipartFile file);
}

