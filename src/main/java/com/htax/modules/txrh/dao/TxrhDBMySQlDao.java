package com.htax.modules.txrh.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.htax.modules.txrh.entity.vo.TxrhDBTabelVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @BelongsProject: htax-web-master
 * @BelongsPackage: com.htax.modules.txrh.dao
 * @Author: ppzz
 * @CreateTime: 2022-08-08  13:12
 * @Description: TODO： mysql数据库查询相关表结构
 * @Version: 1.0
 */
@Mapper
public interface TxrhDBMySQlDao {
    // 查询列表
    Page<TxrhDBTabelVo> queryList(Page<TxrhDBTabelVo> page, @Param("search") TxrhDBTabelVo search);

    List<TxrhDBTabelVo> queryList( @Param("search") TxrhDBTabelVo search);
    // 查询表信息
    TxrhDBTabelVo queryTable(@Param("tableName") String tableName);

    // 查询列信息
    List<TxrhDBTabelVo> queryColumns(@Param("tableName") String tableName);

}
