package com.htax.modules.product.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import dm.jdbc.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.htax.common.utils.PageUtils;
import com.htax.common.utils.Query;

import com.htax.modules.product.dao.HisOutProductDao;
import com.htax.modules.product.entity.HisOutProductEntity;
import com.htax.modules.product.service.HisOutProductService;


@Service("hisOutProductService")
public class HisOutProductServiceImpl extends ServiceImpl<HisOutProductDao, HisOutProductEntity> implements HisOutProductService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<HisOutProductEntity> page = this.page(
                new Query<HisOutProductEntity>().getPage(params),
                new QueryWrapper<HisOutProductEntity>()
        );

        return new PageUtils(page);
    }
    @Override
    public Page<HisOutProductEntity> queryPage(Long current , Long limit , HisOutProductEntity search){
        Page<HisOutProductEntity> page = new Page<>(current,limit);
        QueryWrapper<HisOutProductEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(StringUtil.isNotEmpty(search.getThing()) , "thing",search.getThing());
        Page<HisOutProductEntity> items = baseMapper.selectPage(page , queryWrapper);
        return items;
    }

}
