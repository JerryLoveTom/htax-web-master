package com.htax.modules.product.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.htax.common.utils.PageUtils;
import com.htax.modules.product.entity.HisOutProductEntity;

import java.util.Map;

/**
 * 出货记录表
 *
 * @author ppzz
 * @email 171712007@qq.com
 * @date 2022-07-08 09:37:50
 */
public interface HisOutProductService extends IService<HisOutProductEntity> {

    PageUtils queryPage(Map<String, Object> params);

    Page<HisOutProductEntity> queryPage(Long current , Long limit , HisOutProductEntity search);
}

