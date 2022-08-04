package com.htax.modules.txrh.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 组合模型
 *
 * @author ppzz
 * @email 171712007@qq.com
 * @date 2022-08-02 19:47:02
 */
@Data
@TableName("txrh_mx_zhmx")
public class TxrhMxZhmxEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	@TableId(type=IdType.ID_WORKER_STR)
	private String id;
	/**
	 * 模型名称(组合)
	 */
	private String mxMc;
	/**
	 * 备注
	 */
	private String bz;
	/**
	 * 创建人
	 */
	private Long createUser;

	@TableField(exist = false)
	private String nickName;
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
	private Integer mlJd;

}
