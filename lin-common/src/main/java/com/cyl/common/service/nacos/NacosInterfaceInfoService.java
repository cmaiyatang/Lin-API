package com.cyl.common.service.nacos;

import com.cyl.common.entity.InterfaceInfo;

/**
 * @author chenyanglin
 */
public interface NacosInterfaceInfoService extends IService<InterfaceInfo> {

    InterfaceInfo getInterface(String path,String method);

}
