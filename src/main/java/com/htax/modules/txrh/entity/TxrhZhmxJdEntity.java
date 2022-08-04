package com.htax.modules.txrh.entity;

import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
	private String zhmxId;
	/**
	 * 外键，原子模型的id
	 */
	private String yzmxId;
	/**
	 * 字典，类型 source.数据源 model.模型 end.流程结束 dataclear.数据清理
	 */
	@TableField("lx")
	private String type;
	/**
	 * 模型名称,可以是原子模型的名字，也可以重新起新名字
	 */
	@TableField("mx_mc")
	private String name;
	/**
	 * 坐标
	 */
	@TableField("`left`")
	private String left;
	/**
	 * 坐标
	 */
	@TableField("`top`")
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
