package com.htax.modules.txrh.entity.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

/**
 * @BelongsProject: htax-web-master
 * @BelongsPackage: com.htax.modules.txrh.entity.vo
 * @Author: ppzz
 * @CreateTime: 2022-08-13  14:28
 * @Description: TODO
 * @Version: 1.0
 */
@Data
public class TxrhZhmxJdVo {
    private String id;
    /**
     * 外键，组合模型的id
     */
    private String zhmxId;
    /**
     * 外键，原子模型的id
     */
    private String yzmxId;
    /**
     * 字典，类型 source.数据源 model.模型 end.流程结束 dataclear.数据清理
     */
    private String type;
    /**
     * 模型名称,可以是原子模型的名字，也可以重新起新名字
     */
    private String name;
    /**
     * 坐标
     */
    private String left;
    /**
     * 坐标
     */
    private String top;
    /**
     * 图标
     */
    private String ico;
    /**
     * 状态
     */
    private String state;
    /**
     * 种类：0 数据源  1 模型  2 输出
     * */
    private String mold;

    // 是否已经执行
    private boolean execStatus;
}
