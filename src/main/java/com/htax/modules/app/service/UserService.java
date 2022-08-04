/**
 * Copyright (c) 2016-2019 HTAX All rights reserved.
 *
 * 北京华泰安信科技有限公司
 *
 * 版权所有，侵权必究！
 */

package com.htax.modules.app.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.htax.modules.app.entity.UserEntity;
import com.htax.modules.app.form.LoginForm;

/**
 * 用户
 *
 * @author Mark ppzz
 */
public interface UserService extends IService<UserEntity> {

	UserEntity queryByMobile(String mobile);

	/**
	 * 用户登录
	 * @param form    登录表单
	 * @return        返回用户ID
	 */
	long login(LoginForm form);
}
