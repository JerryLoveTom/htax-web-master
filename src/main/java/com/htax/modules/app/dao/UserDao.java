/**
 * Copyright (c) 2016-2019 HTAX All rights reserved.
 *
 * 北京华泰安信科技有限公司
 *
 * 版权所有，侵权必究！
 */

package com.htax.modules.app.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.htax.modules.app.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户
 *
 * @author Mark ppzz
 */
@Mapper
public interface UserDao extends BaseMapper<UserEntity> {

}
