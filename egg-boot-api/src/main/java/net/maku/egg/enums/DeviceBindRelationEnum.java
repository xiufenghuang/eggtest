package net.maku.egg.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 设备运行状态枚举
 *
 * @author LSF maku_lsf@163.com
 */
@Getter
@RequiredArgsConstructor
public enum DeviceBindRelationEnum {
    /**
     * 未绑定店铺
     */
    UNBIND_SHOP(0, "未绑定店铺"),

    /**
     * 未绑定价签
     */
    UNBIND_PRICE_TAG(1, "未绑定价签"),


    /**
     * 未绑定价签
     */
    UNBIND_TEMPLATE(2, "未绑定模板");

    /**
     * 状态值
     */
    private final Integer value;

    /**
     * 状态显示名称
     */
    private final String desc;

}
