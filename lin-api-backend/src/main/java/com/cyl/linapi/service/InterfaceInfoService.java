package com.cyl.linapi.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.cyl.common.entity.InterfaceInfo;
import com.cyl.linapi.model.dto.interfaceInfo.InterfaceInfoQueryRequest;
import com.cyl.linapi.model.vo.InterfaceInfoVO;


import javax.servlet.http.HttpServletRequest;

/**
 * @author chenyanglin
 * @description 针对表【interface_info(接口信息表)】的数据库操作Service
 * @createDate 2024-04-24 14:42:05
 */
public interface InterfaceInfoService extends IService<InterfaceInfo> {
    public void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add);

    Wrapper<InterfaceInfo> getQueryWrapper(InterfaceInfoQueryRequest interfaceInfoQueryRequest);

    Page<InterfaceInfoVO> getInterfaceInfoVOPage(Page<InterfaceInfo> interfaceInfoPage, HttpServletRequest request);

    Page<InterfaceInfo> searchFromEs(InterfaceInfoQueryRequest interfaceInfoQueryRequest);

    InterfaceInfoVO getInterfaceInfoVO(InterfaceInfo interfaceInfo, HttpServletRequest request);
}
