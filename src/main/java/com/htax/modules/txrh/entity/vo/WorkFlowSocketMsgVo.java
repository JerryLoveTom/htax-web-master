package com.htax.modules.txrh.entity.vo;

import com.htax.modules.txrh.entity.TxrhZhmxJdEntity;
import lombok.Data;

import java.util.List;

/**
 * @BelongsProject: htax-web-master
 * @BelongsPackage: com.htax.modules.txrh.entity.vo
 * @Author: ppzz
 * @CreateTime: 2022-08-12  13:14
 * @Description: TODO
 * @Version: 1.0
 */
@Data
public class WorkFlowSocketMsgVo {
    private String id;
    private List<TxrhZhmxJdEntity> zhmxJdList; //
}
