package com.htax.modules.txrh.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.htax.modules.txrh.entity.TxrhMxYzmxEntity;
import com.htax.modules.txrh.entity.vo.NodeMenuVo;
import com.htax.modules.txrh.entity.vo.SelectOptionVo;
import com.htax.modules.txrh.entity.vo.YzmxVo;

import java.util.List;


/**
 * 原子模型基础表
 *
 * @author ppzz
 * @email 171712007@qq.com
 * @date 2022-07-13 17:42:03
 */
public interface TxrhMxYzmxService extends IService<TxrhMxYzmxEntity> {
    // 带条件的列表分页查询
    Page<TxrhMxYzmxEntity>queryPages(Long current, Long limit, TxrhMxYzmxEntity search);

    Page<TxrhMxYzmxEntity>queryPage(Long current, Long limit, TxrhMxYzmxEntity search);

    // 保存原子模型及其关联参数
    void saveRelated(YzmxVo entity);

    // 按照原子模型的Id修改原子模型及其关联参数
    int updateRelatedById(YzmxVo entity);
    // 根据原子模型Id 获取原子模型及其参数信息
    YzmxVo getYzmxAndParamsById(Long id);
    // 获取目录结构的选择框
    List<SelectOptionVo> getMenuOptions();

    // 获取tree接口模型数据
    List<NodeMenuVo> getTreeList(TxrhMxYzmxEntity search);
}

