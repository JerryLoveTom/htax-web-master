/**
 * Copyright (c) 2016-2019 HTAX All rights reserved.
 *
 * 北京华泰安信科技有限公司
 *
 * 版权所有，侵权必究！
 */

package com.htax.modules.app.annotation;

import java.lang.annotation.*;

/**
 * app登录效验
 *
 * @author Mark ppzz
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Login {
}
