package com.htax.modules.txrh.entity.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.htax.modules.txrh.entity.TxrhYzmxCcsEntity;
import com.htax.modules.txrh.entity.TxrhYzmxRcsEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @BelongsProject: htax-web-master
 * @BelongsPackage: com.htax.modules.txrh.entity.vo
 * @Author: ppzz
 * @CreateTime: 2022-08-02  19:51
 * @Description: TODO
 * @Version: 1.0
 */
@Data
public class YzmxVo {

    /**
     *
     */
    private String id;
    /**
     * 模型名称
     */
    private String mxMc;
    /**
     * 模型路径
     */
    private String mxLj;
    /**
     * 模型文件名称
     */
    private String mxWjMc;
    /**
     * 模型文件实际名称（本地存储的）
     */
    private String mxWjSjMc;
    /**
     * class类名称（当它为空的时候，直接调用方法，不为空的时候，类+方法）
     */
    private String lMc;
    /**
     * 方法名称
     */
    private String ffMc;
    /**
     * 是否有参数 0 否 1.是
     */
    private Integer sfyCs;
    /**
     * 是否有返回值0 否 1.是
     */
    private Integer sfyFhz;
    /**
     * 请求参数描述
     */
    private String csMs;
    /**
     * 返回值描述
     */
    private String fhzMs;
    /**
     * 备注
     */
    private String bz;
    /**
     * 创建人
     */
    private Long createUser;

    @TableLogic(value = "0",delval = "1") // 逻辑删除 新版本只需要加入这个，不需要进行config配置
    @ApiModelProperty(value = "逻辑删除 1（true）已删除， 0（false）未删除")
    private Boolean isDelete;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
    /**
     * 审核状态：-1 审核不通过 0.未审核 1.审核中 2.审核通过 （只有发布了才会审核）
     */
    private Integer shZt;
    /**
     * 发布状态 0 未发布。1.已发布
     */
    private Integer fbZt;
    /**
     * 审核不通过原因
     */
    private String shYy;
    /**
     * 父节点
     */
    private String pid;
    /**
     * 目录或节点0目录 1.节点
     */
    private String mlJd;

    private List<TxrhYzmxCcsEntity>yzmxCccsList;

    private List<TxrhYzmxRcsEntity>yzmxRccsList;
}
