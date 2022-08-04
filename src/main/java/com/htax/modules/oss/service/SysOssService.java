/**
 * Copyright (c) 2016-2019 HTAX All rights reserved.
 *
 * 北京华泰安信科技有限公司
 *
 * 版权所有，侵权必究！
 */

package com.htax.modules.oss.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.htax.common.utils.PageUtils;
import com.htax.modules.oss.entity.SysOssEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * 文件上传
 *
 * @author Mark ppzz
 */
public interface SysOssService extends IService<SysOssEntity> {

	PageUtils queryPage(Map<String, Object> params);

}
