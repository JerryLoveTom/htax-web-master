package com.htax.modules.txrh.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.htax.datasource.annotation.DataSource;
import com.htax.modules.txrh.dao.TxrhDBMySQlDao;
import com.htax.modules.txrh.entity.vo.SelectOptionVo;
import com.htax.modules.txrh.entity.vo.TxrhDBTabelVo;
import com.htax.modules.txrh.service.TxrhDBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @BelongsProject: htax-web-master
 * @BelongsPackage: com.htax.modules.txrh.service.impl
 * @Author: ppzz
 * @CreateTime: 2022-08-08  13:07
 * @Description: TODO
 * @Version: 1.0
 */
@Service
public class TxrhDBServiceImpl implements TxrhDBService {
    @Autowired
    private TxrhDBMySQlDao dbDao;
    @Override
    public Page<TxrhDBTabelVo> queryList(Long current, Long limit, TxrhDBTabelVo search) {
        Page<TxrhDBTabelVo>page = new Page<>(current, limit);
        return dbDao.queryList(page, search);
    }

    @DataSource("slave1")
    @Override
    public Page<TxrhDBTabelVo> slave1QueryList(Long current, Long limit, TxrhDBTabelVo search) {
        Page<TxrhDBTabelVo>page = new Page<>(current, limit);
        return dbDao.queryList(page, search);
    }
    @DataSource("slave2")
    @Override
    public Page<TxrhDBTabelVo> slave2QueryList(Long current, Long limit, TxrhDBTabelVo search) {
        Page<TxrhDBTabelVo>page = new Page<>(current, limit);
        return dbDao.queryList(page, search);
    }

    @DataSource("slave1")
    @Override
    public List<TxrhDBTabelVo> slave1SelOptions() {
        return dbDao.queryList(new TxrhDBTabelVo());
    }

    @Override
    public List<TxrhDBTabelVo> masterList() {
        return dbDao.queryList(new TxrhDBTabelVo());
    }
    @DataSource("slave2")
    @Override
    public List<TxrhDBTabelVo> slave2SelOptions() {
        return dbDao.queryList(new TxrhDBTabelVo());
    }

    @Override
    public List<TxrhDBTabelVo> masterQueryColumns(String tableName) {
        return dbDao.queryColumns(tableName);
    }

    @DataSource("slave1")
    @Override
    public List<TxrhDBTabelVo> slave1QueryColumns(String tableName) {
        return dbDao.queryColumns(tableName);
    }

    @DataSource("slave2")
    @Override
    public List<TxrhDBTabelVo> slave2QueryColumns(String tableName) {
        return dbDao.queryColumns(tableName);
    }
}
