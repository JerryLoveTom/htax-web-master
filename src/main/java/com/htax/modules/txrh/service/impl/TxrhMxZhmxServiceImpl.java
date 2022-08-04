package com.htax.modules.txrh.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.htax.modules.txrh.entity.TxrhMxYzmxEntity;
import com.htax.modules.txrh.entity.vo.NodeMenuVo;
import com.htax.modules.txrh.utils.TreeUtils;
import dm.jdbc.util.StringUtil;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.htax.modules.txrh.dao.TxrhMxZhmxDao;
import com.htax.modules.txrh.entity.TxrhMxZhmxEntity;
import com.htax.modules.txrh.service.TxrhMxZhmxService;

import java.util.ArrayList;
import java.util.List;


@Service("txrhMxZhmxService")
public class TxrhMxZhmxServiceImpl extends ServiceImpl<TxrhMxZhmxDao, TxrhMxZhmxEntity> implements TxrhMxZhmxService {


    // 带分页，带条件的列表查询
    @Override
    public Page<TxrhMxZhmxEntity>  queryPage(Long current, Long limit, TxrhMxZhmxEntity search) {
        Page<TxrhMxZhmxEntity> page = new Page<>(current,limit);
        QueryWrapper<TxrhMxZhmxEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtil.isNotEmpty(search.getMxMc()), "mx_mc", search.getMxMc());
        queryWrapper.eq(search.getFbZt() != null, "fb_zt",search.getFbZt());
        queryWrapper.eq(search.getShZt() != null, "sh_zt",search.getShZt());
        queryWrapper.eq(search.getCreateUser()!=null, "create_user", search.getCreateUser());
        queryWrapper.orderByDesc("update_time");
        return baseMapper.selectPage(page, queryWrapper);
    }
    // 获取组合模型tree接口模型数据
    @Override
    public List<NodeMenuVo> getTreeList(TxrhMxZhmxEntity search) {
        // TODO: 代码需要优化
        // 考虑几个问题：1 如果是管理员登陆，看到的是公开的模型，如果是本人登陆既能看到自己的，也能看到公开的
        // 管理员只能去审核模型
        List<NodeMenuVo>list = new ArrayList<>(2);
        List<TxrhMxZhmxEntity> resultList = baseMapper.getMineAndPublic(search);
        NodeMenuVo myModel = new NodeMenuVo();
        myModel.setId("1-1");
        myModel.setLabel("我的工作流");
        NodeMenuVo pubModel = new NodeMenuVo();
        pubModel.setId("1-2");
        pubModel.setLabel("公开工作流");
        if (resultList.size() > 0){
            List<NodeMenuVo>tempList = new ArrayList<>();
            List<NodeMenuVo>pubList = new ArrayList<>();
            if (search.getCreateUser() != null){
                resultList.stream().filter(i -> search.getCreateUser().equals(i.getCreateUser())).forEach(i -> {
                    NodeMenuVo vo = new NodeMenuVo();
                    vo.setId(i.getId());
                    vo.setPid(i.getPid());
                    vo.setLabel(i.getMxMc());
                    vo.setValue(i.getId());
                    tempList.add(vo);
                });
                myModel.setChildren(tempList);
                resultList.stream().filter(i -> !search.getCreateUser().equals(i.getCreateUser())).forEach(i -> {
                    NodeMenuVo vo = new NodeMenuVo();
                    vo.setId(i.getId());
                    vo.setPid(i.getPid());
                    vo.setLabel(i.getMxMc());
                    vo.setValue(i.getId());
                    pubList.add(vo);
                });
                pubModel.setChildren(pubList);
            }
            if (search.getCreateUser() == null){
                resultList.stream().forEach(i -> {
                    NodeMenuVo vo = new NodeMenuVo();
                    vo.setId(i.getId());
                    vo.setPid(i.getPid());
                    vo.setLabel(i.getMxMc());
                    vo.setValue(i.getId());
                    pubList.add(vo);
                });
                pubModel.setChildren(pubList);
            }
        }
        list.add(myModel);
        list.add(pubModel);
        return list;
    }
    @Override
    public TxrhMxZhmxEntity getInfoById(String id) {
        return baseMapper.getInfoById(id);
    }
}
