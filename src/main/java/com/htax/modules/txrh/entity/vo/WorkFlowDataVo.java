package com.htax.modules.txrh.entity.vo;

import com.htax.modules.txrh.entity.TxrhZhmxJdEntity;
import com.htax.modules.txrh.entity.TxrhZhmxLxEntity;
import lombok.Data;

import java.util.List;

/**
 * @BelongsProject: htax-web-master
 * @BelongsPackage: com.htax.modules.txrh.entity.vo
 * @Author: ppzz
 * @CreateTime: 2022-08-04  16:29
 * @Description: TODO：业务流程数据模版
 * @Version: 1.0
 */
@Data
public class WorkFlowDataVo {
    // 组合模型id
    private String zhmxId;

    // 业务模版名称
    private String name;

    // 连线集合
    private List<TxrhZhmxLxEntity>lineList;

    // 节点集合
    private List<TxrhZhmxJdEntity>nodeList;
}
