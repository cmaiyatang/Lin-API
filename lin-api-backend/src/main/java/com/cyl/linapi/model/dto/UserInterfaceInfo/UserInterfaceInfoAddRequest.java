package com.cyl.linapi.model.dto.UserInterfaceInfo;

import lombok.Data;

import java.io.Serializable;

/**
 * 创建请求
 *
 * @author <a href="https://github.com/cmaiyatang">YoungLin</a>

 */
@Data
public class UserInterfaceInfoAddRequest implements Serializable {

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
}