package com.htax.modules.txrh.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.htax.modules.txrh.entity.vo.TxrhDBTabelVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 达梦数据库查询相关表
 *
 * */
@Mapper
public interface TxrhDBDMDao {
    // 查询数据库中表
    Page<TxrhDBTabelVo> queryList(Page<TxrhDBTabelVo> page, @Param("search") TxrhDBTabelVo search);

    // 查询表信息
    TxrhDBTabelVo queryTable(@Param("tableName") String tableName);

    // 查询列信息
    List<TxrhDBTabelVo> queryColumns(@Param("tableName") String tableName);
}
