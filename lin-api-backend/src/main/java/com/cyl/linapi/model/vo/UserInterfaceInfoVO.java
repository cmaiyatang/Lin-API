package com.cyl.linapi.model.vo;

import com.cyl.common.entity.UserInterfaceInfo;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;

/**
 * 帖子视图
 *
 * @author <a href="https://github.com/cmaiyatang">YoungLin</a>

 */
@Data
public class UserInterfaceInfoVO implements Serializable {
    /**
     * 主键
     */
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 接口id
     */
    private Long interfaceInfoId;

    /**
     * 总调用次数
     */
    private Integer hasInvokeNum;

    /**
     * 剩余调用次数
     */
    private Integer leftNum;

    /**
     * 0-正常，1-禁用
     */
    private Integer status;

    /**
     * 包装类转对象
     *
     * @param UserInterfaceInfoVO
     * @return
     */
    public static UserInterfaceInfo voToObj(UserInterfaceInfoVO UserInterfaceInfoVO) {
        if (UserInterfaceInfoVO == null) {
            return null;
        }
        UserInterfaceInfo UserInterfaceInfo = new UserInterfaceInfo();
        BeanUtils.copyProperties(UserInterfaceInfoVO, UserInterfaceInfo);
        return UserInterfaceInfo;
    }

    /**
     * 对象转包装类
     *
     * @param userInterfaceInfo
     * @return
     */
    public static UserInterfaceInfoVO objToVo(UserInterfaceInfo userInterfaceInfo) {
        if (userInterfaceInfo == null) {
            return null;
        }
        UserInterfaceInfoVO UserInterfaceInfoVO = new UserInterfaceInfoVO();
        BeanUtils.copyProperties(userInterfaceInfo, UserInterfaceInfoVO);
        return UserInterfaceInfoVO;
    }
}
