package com.htax.modules.txrh.dao;

import com.htax.modules.txrh.entity.TxrhZhmxJdEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.htax.modules.txrh.entity.vo.TxrhZhmxJdVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 组合模型节点信息
 *
 * @author ppzz
 * @email 171712007@qq.com
 * @date 2022-08-02 19:47:02
 */
@Mapper
public interface TxrhZhmxJdDao extends BaseMapper<TxrhZhmxJdEntity> {

    List<TxrhZhmxJdVo> getList(@Param("search") TxrhZhmxJdEntity search);
}
