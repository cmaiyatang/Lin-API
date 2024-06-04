package com.cyl.linapi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cyl.common.entity.UserInterfaceInfo;

import java.util.List;

/**
* @author chenyanglin
* @description 针对表【user_interface_info(用户接口信息表)】的数据库操作Mapper
* @createDate 2024-04-26 11:09:09
* @Entity import com.cyl.springbootinit.model.entity.UserInterfaceInfo;
*/
public interface UserInterfaceInfoMapper extends BaseMapper<UserInterfaceInfo> {

    List<UserInterfaceInfo> getInvokeTopInterfaceInfoList(int limit);

}




