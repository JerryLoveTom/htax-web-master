package com.htax.modules.txrh.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.htax.common.utils.PageUtils;
import com.htax.common.utils.Query;

import com.htax.modules.txrh.dao.TxrhZhmxLxDao;
import com.htax.modules.txrh.entity.TxrhZhmxLxEntity;
import com.htax.modules.txrh.service.TxrhZhmxLxService;


@Service("txrhZhmxLxService")
public class TxrhZhmxLxServiceImpl extends ServiceImpl<TxrhZhmxLxDao, TxrhZhmxLxEntity> implements TxrhZhmxLxService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<TxrhZhmxLxEntity> page = this.page(
                new Query<TxrhZhmxLxEntity>().getPage(params),
                new QueryWrapper<TxrhZhmxLxEntity>()
        );

        return new PageUtils(page);
    }

}