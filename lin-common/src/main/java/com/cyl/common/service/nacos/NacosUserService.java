package com.cyl.common.service.nacos;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cyl.common.entity.InterfaceInfo;
import com.cyl.common.entity.User;

/**
 * @author chenyanglin
 */
public interface NacosUserService extends IService<InterfaceInfo> {


    User getInvokeUser(String accessKey);
}
