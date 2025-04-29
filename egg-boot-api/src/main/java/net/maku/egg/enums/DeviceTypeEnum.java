package net.maku.egg.enums;

import cn.hutool.core.util.StrUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/**
 * 设备事件类型枚举
 *
 * @author LSF maku_lsf@163.com
 */
@Getter
@RequiredArgsConstructor
public enum DeviceTypeEnum {
    /**
     * 下线
     */
    SCALE(0, "称");


    /**
     * 类型值
     */
    private final Integer value;

    /**
     * 类型名称
     */
    private final String title;
}
