package com.htax.modules.txrh.entity.vo;

import lombok.Data;

/**
 * @BelongsProject: htax-web-master
 * @BelongsPackage: com.htax.modules.txrh.entity.vo
 * @Author: ppzz
 * @CreateTime: 2022-08-08  13:25
 * @Description: TODO
 * @Version: 1.0
 */
@Data
public class TxrhDBTabelVo {
    private String tableName;
    private String engine;
    private String tableComment;
    private String createTime;
    private String columnName;
    private String dataType;
    private String columnComment;
    private String columnKey;
    private String extra;
    private String id; // 参数id
    private String csZwMc; // 参数中文名
}
