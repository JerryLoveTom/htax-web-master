package com.htax.modules.txrh.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.htax.modules.txrh.entity.TxrhDbSourceEntity;
import com.htax.modules.txrh.entity.TxrhMxYzmxEntity;
import com.htax.modules.txrh.entity.vo.NodeMenuVo;

import java.util.List;


/**
 * 多数据源配置信息
 *
 * @author ppzz
 * @email 171712007@qq.com
 * @date 2022-08-08 15:03:11
 */
public interface TxrhDbSourceService extends IService<TxrhDbSourceEntity> {

    Page<TxrhDbSourceEntity> queryPage(Long current, Long limit,TxrhDbSourceEntity search);

    // 获取树状结构
    List<NodeMenuVo> getTreeList(TxrhMxYzmxEntity search);
}

