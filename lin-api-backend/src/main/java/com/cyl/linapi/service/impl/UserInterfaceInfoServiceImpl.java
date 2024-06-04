package com.cyl.linapi.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.cyl.common.entity.InterfaceInfo;
import com.cyl.common.entity.UserInterfaceInfo;
import com.cyl.linapi.common.ErrorCode;
import com.cyl.linapi.exception.BusinessException;
import com.cyl.linapi.mapper.UserInterfaceInfoMapper;
import com.cyl.linapi.model.dto.UserInterfaceInfo.UserInterfaceInfoQueryRequest;
import com.cyl.linapi.model.enums.InterfaceInfoStatusEnum;
import com.cyl.linapi.model.vo.UserInterfaceInfoVO;
import com.cyl.linapi.service.InterfaceInfoService;
import com.cyl.linapi.service.UserInterfaceInfoService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author chenyanglin
 * @description 针对表【user_interface_info(用户接口信息表)】的数据库操作Service实现
 * @createDate 2024-04-26 11:09:09
 */
@Service
public class UserInterfaceInfoServiceImpl extends ServiceImpl<UserInterfaceInfoMapper, UserInterfaceInfo>
        implements UserInterfaceInfoService {

    @Resource
    private InterfaceInfoService interfaceInfoService;

    /**
     * 校验参数
     *
     * @param userInterfaceInfo
     * @param add
     */
    @Override
    public void validUserInterfaceInfo(UserInterfaceInfo userInterfaceInfo, boolean add) {
        if (userInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long userId = userInterfaceInfo.getUserId();
        Long interfaceInfoId = userInterfaceInfo.getInterfaceInfoId();

        // 创建时，参数不能为空
        if (userId == null || userId <= 0) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "用户不存在");
        }
        if (interfaceInfoId == null || interfaceInfoId <= 0) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "接口不存在");
        }
        if (userInterfaceInfo.getLeftNum() < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "剩余调用次数不能小于0");
        }
    }

    @Override
    public UserInterfaceInfoVO getUserInterfaceInfoVO(UserInterfaceInfo userInterfaceInfo, HttpServletRequest request) {

        UserInterfaceInfoVO userInterfaceInfoVo = new UserInterfaceInfoVO();
        BeanUtils.copyProperties(userInterfaceInfo, userInterfaceInfoVo);

        return userInterfaceInfoVo;
    }

    /**
     * 返回查询条件Wrapper
     *
     * @param userInterfaceInfoQueryRequest
     * @return
     */
    @Override
    public Wrapper<UserInterfaceInfo> getQueryWrapper(UserInterfaceInfoQueryRequest userInterfaceInfoQueryRequest) {
        QueryWrapper<UserInterfaceInfo> infoQueryWrapper = new QueryWrapper<>();

        //从请求类中取出分页参数
        Long id = userInterfaceInfoQueryRequest.getId();
        Integer status = userInterfaceInfoQueryRequest.getStatus();
        Integer hasInvokeNum = userInterfaceInfoQueryRequest.getHasInvokeNum();
        Integer leftNum = userInterfaceInfoQueryRequest.getLeftNum();
        Long interfaceInfoId = userInterfaceInfoQueryRequest.getInterfaceInfoId();
        Long userId = userInterfaceInfoQueryRequest.getUserId();


        //设置查询queryWrapper //判断校验字符串建议使用isNotBlank null，"" 和 "  "都能校验
        if (id != null && id > 0) {
            infoQueryWrapper.eq("id", id);
        }

        if (userId != null && userId > 0) {
            infoQueryWrapper.eq("userId", userId);
        }
        if (interfaceInfoId != null && interfaceInfoId > 0) {
            infoQueryWrapper.eq("interfaceInfoId", interfaceInfoId);
        }

        if (status != null && !(status < 0)) {
            infoQueryWrapper.eq("status", status);
        }

        if (hasInvokeNum != null && hasInvokeNum > 0) {
            infoQueryWrapper.eq("hasInvokeNum", hasInvokeNum);
        }

        if (leftNum != null && leftNum >= 0) {
            infoQueryWrapper.eq("leftNum", leftNum);
        }

        return infoQueryWrapper;
    }

    @Override
    public boolean invokeNumPlusOne(long userId, long interfaceInfoId) {

        if (userId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if (interfaceInfoId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<InterfaceInfo> infoQueryWrapper = new QueryWrapper<>();
        infoQueryWrapper.eq("interfaceInfoId", interfaceInfoId);
        InterfaceInfo interfaceInfo = interfaceInfoService.getById(interfaceInfoId);
        if (interfaceInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "接口不存在");
        }

        if (interfaceInfo.getStatus() != InterfaceInfoStatusEnum.ONLINE.getValue()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "接口为启用");
        }

        //查询用户-接口表
        QueryWrapper<UserInterfaceInfo> queryWrapper = new QueryWrapper();
        queryWrapper.eq("userId", userId);
        queryWrapper.eq("interfaceInfoId", interfaceInfoId);
        UserInterfaceInfo userInterfaceInfo = this.getOne(queryWrapper);

        if (userInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "用户-接口信息不存在");
        }
        Integer hasInvokeNum = userInterfaceInfo.getHasInvokeNum();
        Integer leftNum = userInterfaceInfo.getLeftNum();

        if (leftNum <= 0) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "无调用次数");
        }

        UserInterfaceInfo newUserInterfaceInfo = new UserInterfaceInfo();
        BeanUtils.copyProperties(userInterfaceInfo, newUserInterfaceInfo);

        newUserInterfaceInfo.setHasInvokeNum(hasInvokeNum + 1);
        newUserInterfaceInfo.setLeftNum(leftNum - 1);

        return this.updateById(newUserInterfaceInfo);
    }
}




