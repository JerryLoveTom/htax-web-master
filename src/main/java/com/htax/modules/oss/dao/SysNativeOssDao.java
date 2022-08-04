package com.htax.modules.oss.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.htax.modules.oss.entity.SysNativeOssEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 本地文件存储记录表
 *
 * @author ppzz
 * @email 171712007@qq.com
 * @date 2022-06-18 14:58:34
 */
@Mapper
public interface SysNativeOssDao extends BaseMapper<SysNativeOssEntity> {

}
