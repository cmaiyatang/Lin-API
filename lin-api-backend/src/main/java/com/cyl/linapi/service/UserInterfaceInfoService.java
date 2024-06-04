package com.cyl.linapi.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cyl.common.entity.UserInterfaceInfo;
import com.cyl.linapi.model.dto.UserInterfaceInfo.UserInterfaceInfoQueryRequest;
import com.cyl.linapi.model.vo.UserInterfaceInfoVO;


import javax.servlet.http.HttpServletRequest;

/**
* @author chenyanglin
* @description 针对表【user_interface_info(用户接口信息表)】的数据库操作Service
* @createDate 2024-04-26 11:09:09
*/
public interface UserInterfaceInfoService extends IService<UserInterfaceInfo> {

    void validUserInterfaceInfo(UserInterfaceInfo userInterfaceInfo, boolean add);

    UserInterfaceInfoVO getUserInterfaceInfoVO(UserInterfaceInfo userInterfaceInfo, HttpServletRequest request);

    Wrapper<UserInterfaceInfo> getQueryWrapper(UserInterfaceInfoQueryRequest userInterfaceInfoQueryRequest);

    public boolean invokeNumPlusOne(long userId, long interfaceInfoId);
}
