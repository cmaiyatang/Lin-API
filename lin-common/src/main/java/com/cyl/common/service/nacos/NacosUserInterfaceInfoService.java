package com.cyl.common.service.nacos;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cyl.common.entity.InterfaceInfo;

/**
 * @author chenyanglin
 */
public interface NacosUserInterfaceInfoService extends IService<InterfaceInfo> {


    boolean invokeNumPlusOne(long userId, long interfaceInfoId);
}
