package com.cyl.linapi.service.nacos;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cyl.common.entity.InterfaceInfo;
import com.cyl.common.service.nacos.NacosInterfaceInfoService;
import com.cyl.linapi.common.ErrorCode;
import com.cyl.linapi.exception.BusinessException;
import com.cyl.linapi.mapper.InterfaceInfoMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

/**
 * @author chenyanglin
 */
@DubboService
public class NacosInterfaceInfoServiceImpl extends ServiceImpl<InterfaceInfoMapper, InterfaceInfo>
        implements NacosInterfaceInfoService {

    @Resource
    private InterfaceInfoMapper interfaceInfoMapper;

    /**
     * 获取接口
     *
     * @param url
     * @param method
     * @return
     */
    @Override
    public InterfaceInfo getInterface(String url, String method) {
        if (StringUtils.isAnyBlank(url, method)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        QueryWrapper<InterfaceInfo> infoQueryWrapper = new QueryWrapper<>();
        infoQueryWrapper.eq("url", url);
        infoQueryWrapper.eq("method", method);

        return interfaceInfoMapper.selectOne(infoQueryWrapper);
    }
}




