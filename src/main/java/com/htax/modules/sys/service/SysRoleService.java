/**
 * Copyright (c) 2016-2019 HTAX All rights reserved.
 *
 * 北京华泰安信科技有限公司
 *
 * 版权所有，侵权必究！
 */

package com.htax.modules.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.htax.common.utils.PageUtils;
import com.htax.modules.sys.entity.SysRoleEntity;

import java.util.List;
import java.util.Map;


/**
 * 角色
 *
 * @author Mark ppzz
 */
public interface SysRoleService extends IService<SysRoleEntity> {

	PageUtils queryPage(Map<String, Object> params);

	void saveRole(SysRoleEntity role);

	void update(SysRoleEntity role);

	void deleteBatch(Long[] roleIds);


	/**
	 * 查询用户创建的角色ID列表
	 */
	List<Long> queryRoleIdList(Long createUserId);
}
