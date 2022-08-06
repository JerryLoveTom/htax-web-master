package com.htax.modules.txrh.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.htax.common.utils.PageUtils;
import com.htax.common.utils.Query;

import com.htax.modules.txrh.dao.TxrhZhmxJdDao;
import com.htax.modules.txrh.entity.TxrhZhmxJdEntity;
import com.htax.modules.txrh.service.TxrhZhmxJdService;


@Service("txrhZhmxJdService")
public class TxrhZhmxJdServiceImpl extends ServiceImpl<TxrhZhmxJdDao, TxrhZhmxJdEntity> implements TxrhZhmxJdService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<TxrhZhmxJdEntity> page = this.page(
                new Query<TxrhZhmxJdEntity>().getPage(params),
                new QueryWrapper<TxrhZhmxJdEntity>()
        );

        return new PageUtils(page);
    }

}