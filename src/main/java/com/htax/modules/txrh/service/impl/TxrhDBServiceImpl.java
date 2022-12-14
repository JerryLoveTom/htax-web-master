package com.htax.modules.txrh.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.htax.common.constant.Constants;
import com.htax.datasource.annotation.DataSource;
import com.htax.modules.txrh.dao.TxrhDBMySQlDao;
import com.htax.modules.txrh.entity.TxrhDbSourceEntity;
import com.htax.modules.txrh.entity.vo.SelectOptionVo;
import com.htax.modules.txrh.entity.vo.TxrhDBTabelVo;
import com.htax.modules.txrh.service.TxrhDBService;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.List;
import java.util.Map;

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
    /**
     * dbSource????????????
     * sql:
     * */
    @Override
    public JSONArray getDataBySourceAndTable(TxrhDbSourceEntity dbSource, String sql) {
        try {
            JSONArray jsonArray = jdbcConnection(dbSource, sql);
            System.out.println("jsonArray = " + jsonArray);
            return jsonArray;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private JSONArray jdbcConnection(TxrhDbSourceEntity dbSource , String sql) throws ClassNotFoundException, SQLException {
        //1.????????????
        Class.forName(dbSource.getDriverClassName());
        //2. ???????????????url
        String url = dbSource.getUrl();
        String user = dbSource.getUsername();
        String password = dbSource.getPassword();
        //3.???????????? connection ??????????????????????????????
        Connection connection = DriverManager.getConnection(url, user, password);
        //4.????????????sql?????? statement ????????????sql
        Statement statement = connection.createStatement();
        //5.??????sql ??????????????????????????????sql
        //6.???????????????
        /**
         * statement.executeQuery ????????????
         * statement.executeUpdate ?????? ?????????
         * statement.execute ???????????? ?????????
         */
        // 6. ??????sql??????????????????
        ResultSet resultSet = statement.executeQuery(sql);
        ResultSetMetaData rsmd = resultSet.getMetaData();
        int colnum = rsmd.getColumnCount();
        String val = "";
        String colName = "";
        // 6.1 ?????????????????????json?????????
        JSONObject jobj = new JSONObject(); // ????????????
        JSONArray jArr = new JSONArray(); // ?????????
        while(resultSet.next()) {
            for(int i = 1; i<= colnum; i++) {
                colName = rsmd.getColumnLabel(i);
                if(1==i) {
                    val = new Integer(resultSet.getInt(colName)).toString();
                }else {
                    val = resultSet.getString(colName);
                }

                try {
                    jobj.put(colName, val);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            jArr.put(jobj);
        }
        //7.????????????
        resultSet.close();
        statement.close();
        connection.close();
        return jArr;
    }
}
