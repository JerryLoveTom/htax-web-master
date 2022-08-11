package com.htax.modules.txrh.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.htax.modules.txrh.entity.TxrhMxZhmxEntity;
import com.htax.modules.txrh.entity.TxrhZhmxJdCsdzEntity;
import com.htax.modules.txrh.entity.TxrhZhmxJdEntity;
import com.htax.modules.txrh.entity.vo.NodeMenuVo;
import com.htax.modules.txrh.entity.vo.WorkFlowDataVo;

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
    Page<TxrhMxZhmxEntity> queryPages(Long current, Long limit, TxrhMxZhmxEntity search);

    // 获取组合模型tree接口模型数据
    List<NodeMenuVo> getTreeList(TxrhMxZhmxEntity search);

    // 获取组合模型信息，包含创建者名称
    TxrhMxZhmxEntity getInfoById(String id);

    // 通过组合模型id获取模型、节点及连线信息
    WorkFlowDataVo getFlowDataById(String id);

    //保存模型流程信息
    int saveFlowData(WorkFlowDataVo entity);

    // 删除组合模型及其关联表相关数据
    boolean deleteZhmxById(String id);

    // 克隆模型信息
    boolean cloneFlow(String id);

    // 通过id执行工作流
    void runWorkFlow(String id);

    // 获取当前节点的上级节点
    List<TxrhZhmxJdEntity> getFromJdByToId(String toId);

}

