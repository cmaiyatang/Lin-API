package com.cyl.linapi.service.nacos;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cyl.common.entity.InterfaceInfo;
import com.cyl.common.entity.User;
import com.cyl.common.service.nacos.NacosUserService;
import com.cyl.linapi.common.ErrorCode;
import com.cyl.linapi.exception.BusinessException;
import com.cyl.linapi.mapper.InterfaceInfoMapper;
import com.cyl.linapi.mapper.UserMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

/**
 * @author chenyanglin
 */
@DubboService
public class NacosUserServiceImpl extends ServiceImpl<InterfaceInfoMapper, InterfaceInfo>
        implements NacosUserService {

    @Resource
    private UserMapper userMapper;

    /**
     * 获取要调用接口的用户
     * @param accessKey
     * @return
     */
    @Override
    public User getInvokeUser(String accessKey) {

        if (StringUtils.isAnyBlank(accessKey)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("accessKey",accessKey);

        return userMapper.selectOne(userQueryWrapper);
    }
}




