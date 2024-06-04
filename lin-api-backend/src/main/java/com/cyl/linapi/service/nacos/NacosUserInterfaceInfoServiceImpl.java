package com.cyl.linapi.service.nacos;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cyl.common.entity.InterfaceInfo;
import com.cyl.common.service.nacos.NacosUserInterfaceInfoService;
import com.cyl.linapi.mapper.InterfaceInfoMapper;
import com.cyl.linapi.service.UserInterfaceInfoService;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

/**
 * @author chenyanglin
 */
@DubboService
public class NacosUserInterfaceInfoServiceImpl extends ServiceImpl<InterfaceInfoMapper, InterfaceInfo>
        implements NacosUserInterfaceInfoService {

    @Resource
    private UserInterfaceInfoService userInterfaceInfoService;


    /**
     * 接口调用次数加一
     * @param userId
     * @param interfaceInfoId
     * @return
     */
    @Override
    public boolean invokeNumPlusOne(long userId, long interfaceInfoId) {
        return userInterfaceInfoService.invokeNumPlusOne(userId, interfaceInfoId);
    }
}




