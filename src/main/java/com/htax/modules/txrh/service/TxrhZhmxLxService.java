package com.htax.modules.txrh.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.htax.common.utils.PageUtils;
import com.htax.modules.txrh.entity.TxrhZhmxLxEntity;

import java.util.Map;

/**
 * 组合模型中原子模型的连线
 *
 * @author ppzz
 * @email 171712007@qq.com
 * @date 2022-08-02 19:47:02
 */
public interface TxrhZhmxLxService extends IService<TxrhZhmxLxEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

