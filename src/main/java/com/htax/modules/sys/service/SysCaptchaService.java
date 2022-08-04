/**
 * Copyright (c) 2016-2019 HTAX All rights reserved.
 *
 * 北京华泰安信科技有限公司
 *
 * 版权所有，侵权必究！
 */

package com.htax.modules.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.htax.modules.sys.entity.SysCaptchaEntity;

import java.awt.image.BufferedImage;

/**
 * 验证码
 *
 * @author Mark ppzz
 */
public interface SysCaptchaService extends IService<SysCaptchaEntity> {

    /**
     * 获取图片验证码
     */
    BufferedImage getCaptcha(String uuid);

    /**
     * 验证码效验
     * @param uuid  uuid
     * @param code  验证码
     * @return  true：成功  false：失败
     */
    boolean validate(String uuid, String code);
}
