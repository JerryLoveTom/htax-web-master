package com.htax.modules.txrh.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 组合模型节点信息
 *
 * @author ppzz
 * @email 171712007@qq.com
 * @date 2022-08-02 19:47:02
 */
@Data
@TableName("txrh_zhmx_jd")
public class TxrhZhmxJdEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@TableId(type = IdType.INPUT)
	private String id;
	/**
	 * 外键，组合模型的id
	 */
	private Long zhmxId;
	/**
	 * 外键，原子模型的id
	 */
	private Long yzmxId;
	/**
	 * 字典，类型 0.数据源 1.模型 2.流程结束 3.数据清理
	 */
	private Long lx;
	/**
	 * 模型名称,可以是原子模型的名字，也可以重新起新名字
	 */
	private String mxMc;
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

}
