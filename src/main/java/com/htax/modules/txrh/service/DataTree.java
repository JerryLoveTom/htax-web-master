package com.htax.modules.txrh.service;

import java.util.List;

/**
 * @author: zzpp
 * @date: 2022-01-15 11:19
 * @description: 树形数据实体类接口
 */
public interface DataTree<T> {
    public String getId();
    public String getPid();
    public void setChildren(List<T> children);
    public List<T>getChildren();
}
