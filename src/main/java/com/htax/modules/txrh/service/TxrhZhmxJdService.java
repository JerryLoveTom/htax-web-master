package com.htax.modules.txrh.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.htax.common.utils.PageUtils;
import com.htax.modules.txrh.entity.TxrhZhmxJdEntity;

import java.util.Map;

/**
 * 组合模型节点信息
 *
 * @author ppzz
 * @email 171712007@qq.com
 * @date 2022-08-02 19:47:02
 */
public interface TxrhZhmxJdService extends IService<TxrhZhmxJdEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

