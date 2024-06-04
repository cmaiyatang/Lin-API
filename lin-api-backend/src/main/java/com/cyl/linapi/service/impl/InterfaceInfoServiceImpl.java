package com.cyl.linapi.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cyl.common.entity.InterfaceInfo;
import com.cyl.linapi.common.ErrorCode;
import com.cyl.linapi.exception.BusinessException;
import com.cyl.linapi.exception.ThrowUtils;
import com.cyl.linapi.mapper.InterfaceInfoMapper;
import com.cyl.linapi.model.dto.interfaceInfo.InterfaceInfoQueryRequest;
import com.cyl.linapi.model.vo.InterfaceInfoVO;
import com.cyl.linapi.service.InterfaceInfoService;
import com.cyl.linapi.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author chenyanglin
 * @description 针对表【interface_info(接口信息表)】的数据库操作Service实现
 * @createDate 2024-04-24 14:42:05
 */
@Service
public class InterfaceInfoServiceImpl extends ServiceImpl<InterfaceInfoMapper, InterfaceInfo>
        implements InterfaceInfoService {

    @Resource
    private UserService userService;


    @Override
    public void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add) {
        if (interfaceInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String name = interfaceInfo.getName();
        String description = interfaceInfo.getDescription();
        // 创建时，参数不能为空
        if (add) {
            ThrowUtils.throwIf(StringUtils.isAnyBlank(name), ErrorCode.PARAMS_ERROR);
        }
        // 有参数则校验
        if (StringUtils.isNotBlank(name) && name.length() > 80) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "接口名称太长");
        }
        if (StringUtils.isNotBlank(description) && description.length() > 8192) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "描述过长");
        }
    }

    @Override
    public Wrapper<InterfaceInfo> getQueryWrapper(InterfaceInfoQueryRequest interfaceInfoQueryRequest) {

        QueryWrapper<InterfaceInfo> infoQueryWrapper = new QueryWrapper<>();

        //从请求类中取出分页参数
        String name = interfaceInfoQueryRequest.getName();
        String description = interfaceInfoQueryRequest.getDescription();
        String method = interfaceInfoQueryRequest.getMethod();
        String url = interfaceInfoQueryRequest.getUrl();
        String requestHeader = interfaceInfoQueryRequest.getRequestHeader();
        String responseHeader = interfaceInfoQueryRequest.getResponseHeader();
        Integer status = interfaceInfoQueryRequest.getStatus();
        Long userId = interfaceInfoQueryRequest.getUserId();


        //设置查询queryWrapper //判断校验字符串建议使用isNotBlank null，"" 和 "  "都能校验
        if (StringUtils.isNotBlank(name)) {
            infoQueryWrapper.like("name", name);
        }
        if (StringUtils.isNotBlank(description)) {
            infoQueryWrapper.like("description", description);
        }
        if (StringUtils.isNotBlank(method)) {
            infoQueryWrapper.eq("method", method);
        }
        if (StringUtils.isNotBlank(url)) {
            infoQueryWrapper.like("url", url);
        }
        if (StringUtils.isNotBlank(requestHeader)) {
            infoQueryWrapper.like("requestHeader", requestHeader);
        }
        if (StringUtils.isNotBlank(responseHeader)) {
            infoQueryWrapper.like("responseHeader", responseHeader);
        }
        if (status != null && !(status < 0)) {
            infoQueryWrapper.eq("status", status);
        }

        if (userId != null && userId > 0) {
            infoQueryWrapper.eq("userId",userId);
        }

        return infoQueryWrapper;
    }

    @Override
    public Page<InterfaceInfoVO> getInterfaceInfoVOPage(Page<InterfaceInfo> interfaceInfoPage, HttpServletRequest request) {

        Page<InterfaceInfoVO> interfaceInfoVOPage = new Page<>();
        BeanUtils.copyProperties(interfaceInfoPage, interfaceInfoVOPage);

        //取出page对象中的records
        List<InterfaceInfo> interfaceInfoList = interfaceInfoPage.getRecords();
        //遍历List将interfaceinfo转为interfaceinfoVO
        List<InterfaceInfoVO> interfaceInfoVOList = interfaceInfoList.stream().map(InterfaceInfoVO::objToVo).collect(Collectors.toList());

        interfaceInfoVOPage.setRecords(interfaceInfoVOList);

        return interfaceInfoVOPage;
    }

    @Override
    public Page<InterfaceInfo> searchFromEs(InterfaceInfoQueryRequest interfaceInfoQueryRequest) {
        return null;
    }

    @Override
    public InterfaceInfoVO getInterfaceInfoVO(InterfaceInfo interfaceInfo, HttpServletRequest request) {

        InterfaceInfoVO interfaceInfoVO = new InterfaceInfoVO();
        BeanUtils.copyProperties(interfaceInfo, interfaceInfoVO);

        return interfaceInfoVO;
    }

}




