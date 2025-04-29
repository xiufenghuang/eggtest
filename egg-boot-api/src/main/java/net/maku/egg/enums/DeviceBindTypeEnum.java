package net.maku.egg.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
//设备绑定类型
public enum DeviceBindTypeEnum {


    SHOP(1,"店铺"),
    PRICE_TAG(2,"价签"),
    TEMPLATE(3,"模板");

    /**
     * 类型值
     */
    private final Integer value;

    /**
     * 类型名称
     */
    private final String desc;
}
