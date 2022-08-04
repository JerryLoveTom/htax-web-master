package com.htax.modules.txrh.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.htax.modules.txrh.entity.TxrhMxZhmxEntity;
import com.htax.modules.txrh.entity.vo.NodeMenuVo;

import java.util.List;


/**
 * 组合模型
 *
 * @author ppzz
 * @email 171712007@qq.com
 * @date 2022-08-02 19:47:02
 */
public interface TxrhMxZhmxService extends IService<TxrhMxZhmxEntity> {
    // 带分页，带条件的列表查询
    Page<TxrhMxZhmxEntity> queryPage(Long current, Long limit, TxrhMxZhmxEntity search);

    // 获取组合模型tree接口模型数据
    List<NodeMenuVo> getTreeList(TxrhMxZhmxEntity search);

    // 获取组合模型信息，包含创建者名称
    TxrhMxZhmxEntity getInfoById(String id);
}

