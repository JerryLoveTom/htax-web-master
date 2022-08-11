package com.htax.modules.txrh.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 组合模型节点参数对照
 *
 * @author ppzz
 * @email 171712007@qq.com
 * @date 2022-08-09 15:13:52
 */
@Data
@TableName("txrh_zhmx_jd_csdz")
public class TxrhZhmxJdCsdzEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	@TableId
	private Long id;
	/**
	 * 外键，组合模型编号
	 */
	private String zhmxId;
	/**
	 * 开始节点类型，0.数据源节点 1.算法节点
	 */
	private String fromType;
	// 起点原子模型id
	private String fromYzmxId;

	/**
	 * 外键，起点编号 txrh_zhmx_jd
	 */
	private String fromNode;
	/**
	 * 外键，起点参数编号
	 */
	private Long fromParam;

	/**
	 * 结束节点类型
	 * */
	private String toType;
	// 终点原子模型id
	private String toYzmxId;
	/**
	 * 外键，数据源id txrh_db_source
	 */
	private String toNode;
	/**
	 * 数据库表名
	 */
	private Long toParam;

	/**
	 * 对照的起点表字段名称
	 * */
	private String fromColumnName;
	/**
	 * 对照的终点表字段名称
	 * */
	private String toColumnName;
	/**
	 * 创建人
	 */
	private Long createUser;
	// @TableLogic(value = "0",delval = "1") // 逻辑删除 新版本只需要加入这个，不需要进行config配置
	@ApiModelProperty(value = "逻辑删除 1（true）已删除， 0（false）未删除")
	private Boolean isDelete;

	@ApiModelProperty(value = "创建时间")
	@TableField(fill = FieldFill.INSERT)
	private Date createTime;

	@ApiModelProperty(value = "更新时间")
	@TableField(fill = FieldFill.INSERT_UPDATE)
	private Date updateTime;

}
