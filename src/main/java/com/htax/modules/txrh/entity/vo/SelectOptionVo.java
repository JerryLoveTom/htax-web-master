package com.htax.modules.txrh.entity.vo;

import com.htax.modules.txrh.service.DataTree;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @BelongsProject: htax-web-master
 * @BelongsPackage: com.htax.modules.txrh.entity.vo
 * @Author: ppzz
 * @CreateTime: 2022-08-02  19:51
 * @Description: TODO
 * @Version: 1.0
 */
@Data
@ToString
public class SelectOptionVo implements DataTree<SelectOptionVo> {
    private String id;

    private String pid;

    private String label;

    private String value;

    private List<SelectOptionVo> children;
}
