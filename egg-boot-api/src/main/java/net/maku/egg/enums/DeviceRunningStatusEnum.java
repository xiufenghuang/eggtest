package net.maku.egg.enums;

import cn.hutool.core.util.StrUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/**
 * 设备运行状态枚举
 *
 * @author LSF maku_lsf@163.com
 */
@Getter
@RequiredArgsConstructor
public enum DeviceRunningStatusEnum {
    /**
     * 离线状态
     */
    OFFLINE(0, "离线"),

    /**
     * 在线状态
     */
    ONLINE(1, "在线"),

    /**
     * 在线状态
     */
    FAULT(2, "故障");

    /**
     * 状态值
     */
    private final Integer value;

    /**
     * 状态显示名称
     */
    private final String title;

}
