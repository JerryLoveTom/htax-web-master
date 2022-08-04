package com.htax.modules.txrh.entity.vo;

import com.htax.modules.txrh.service.DataTree;
import lombok.Data;

import java.util.List;

/**
 * @BelongsProject: htax-web-master
 * @BelongsPackage: com.htax.modules.txrh.entity.vo
 * @Author: ppzz
 * @CreateTime: 2022-08-03  10:51
 * @Description: TODO
 * @Version: 1.0
 */
@Data
public class NodeMenuVo implements DataTree<NodeMenuVo> {

    /**
     * 主键
     * */
    private String id;
    /**
     * 父级节点
     * */
    private String pid;
    /**
     * 类型
     * */
    private String type;
    /**
     * 名称
     * */
    private String name;
    /**
     * 图标
     * */
    private String ico;
    /**
     * 自定义覆盖样式
     * */
    private Object style;
    /**
     * 是否展开
     * */
    private Boolean open;
    private String label;
    private String value;
    /**
     * 子节点
     * */
    private List<NodeMenuVo>children;

}
