package com.htax.modules.txrh.utils;


import com.htax.modules.txrh.service.DataTree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: zzpp
 * @date: 2022-01-15 11:25
 * @description: 获取树形工具类
 */
public class TreeUtils {
    //获取顶层节点
    public static <T extends DataTree<T>> List<T> getTreeList(String topId, List<T> entityList){
        List<T> resultList = new ArrayList<>();//存储顶层的数据
        Map<Object, T> treeMap = new HashMap<>();
        T itemTree;
        for(int i=0;i<entityList.size()&&!entityList.isEmpty();i++) { // 判断列表为空不为空
            itemTree = entityList.get(i); // 取出一个元素
            treeMap.put(itemTree.getId().trim(),itemTree);//把所有的数据放到map当中，id为key
            if(topId.equals(itemTree.getPid().trim()) || itemTree.getPid().trim() == null) {//把顶层数据放到集合中
                resultList.add(itemTree);
            }
        }
        //循环数据，把数据放到上一级的childen属性中
        for(int i = 0; i< entityList.size()&&!entityList.isEmpty();i++) {
            itemTree = entityList.get(i);
            T data = treeMap.get(itemTree.getPid().trim());//在map集合中寻找父亲
            if(data != null) {//判断父亲有没有
                if(data.getChildren() == null) {
                    data.setChildren(new ArrayList<>());
                }
                data.getChildren().add(itemTree);//把子节点 放到父节点childList当中
                treeMap.put(itemTree.getPid(), data);//把放好的数据放回map当中
            }
        }
        return resultList;
    }
}
