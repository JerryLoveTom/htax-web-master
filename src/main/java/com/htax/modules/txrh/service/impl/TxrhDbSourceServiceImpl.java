package com.htax.modules.txrh.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.htax.common.utils.uuid.IdUtils;
import com.htax.modules.txrh.entity.TxrhMxYzmxEntity;
import com.htax.modules.txrh.entity.vo.NodeMenuVo;
import com.htax.modules.txrh.utils.TreeUtils;
import dm.jdbc.util.StringUtil;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.htax.modules.txrh.dao.TxrhDbSourceDao;
import com.htax.modules.txrh.entity.TxrhDbSourceEntity;
import com.htax.modules.txrh.service.TxrhDbSourceService;

import java.util.ArrayList;
import java.util.List;


@Service("txrhDbSourceService")
public class TxrhDbSourceServiceImpl extends ServiceImpl<TxrhDbSourceDao, TxrhDbSourceEntity> implements TxrhDbSourceService {

    @Override
    public Page<TxrhDbSourceEntity> queryPage(Long current, Long limit, TxrhDbSourceEntity search) {
        Page<TxrhDbSourceEntity> page = new Page<>(current, limit);
        QueryWrapper<TxrhDbSourceEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtil.isNotEmpty(search.getSlaveName()), "slave_name", search.getSlaveName());
        queryWrapper.like(StringUtil.isNotEmpty(search.getUrl()), "url", search.getUrl());
        queryWrapper.like(StringUtil.isNotEmpty(search.getMlJd()), "ml_jd", search.getMlJd());
        return baseMapper.selectPage(page, queryWrapper);
    }

    @Override
    public List<NodeMenuVo> getTreeList(TxrhMxYzmxEntity search) {
        QueryWrapper<TxrhDbSourceEntity> queryWrapper = new QueryWrapper<TxrhDbSourceEntity>()
                .like(StringUtil.isNotEmpty(search.getMxMc()), "mx_mc", search.getMxMc());
        List<TxrhDbSourceEntity> dataSourceList = this.list(queryWrapper);
        List<NodeMenuVo>list = new ArrayList<>();
        if (dataSourceList.size() > 0){
            dataSourceList.stream().forEach(i -> {
                NodeMenuVo vo = new NodeMenuVo();
                vo.setId(i.getId().toString());
                vo.setPid(i.getPid());
                vo.setName(i.getMxMc());
                vo.setMold("0");
                vo.setSourceName(i.getSlaveName());
                vo.setType(IdUtils.simpleUUID());
                if ("0".equals(i.getPid())){
                    vo.setIco("el-icon-video-pause");
                    vo.setOpen(true);
                }else{
                    vo.setIco("el-icon-coin");
                }
                list.add(vo);
            });
            List<NodeMenuVo>vos = TreeUtils.getTreeList("0",list);
            return vos;
        }
        return list;
    }
}
