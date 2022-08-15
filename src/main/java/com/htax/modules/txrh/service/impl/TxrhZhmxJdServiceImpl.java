package com.htax.modules.txrh.service.impl;

import com.htax.modules.txrh.entity.vo.TxrhZhmxJdVo;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.htax.modules.txrh.dao.TxrhZhmxJdDao;
import com.htax.modules.txrh.entity.TxrhZhmxJdEntity;
import com.htax.modules.txrh.service.TxrhZhmxJdService;

import java.util.List;


@Service("txrhZhmxJdService")
public class TxrhZhmxJdServiceImpl extends ServiceImpl<TxrhZhmxJdDao, TxrhZhmxJdEntity> implements TxrhZhmxJdService {
    @Override
    public List<TxrhZhmxJdVo> getList(TxrhZhmxJdEntity search) {
        return baseMapper.getList(search);
    }
}
