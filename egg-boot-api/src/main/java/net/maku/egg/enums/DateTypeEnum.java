package net.maku.egg.enums;

/**
 * @author : lenovo
 * @program : egg-api
 * @description :
 * @create : 2024-10-12 14:49
 **/
public enum DateTypeEnum {
    DAY(1),
    MONTH(2),
    YEAR(3);

    private Integer code;

    DateTypeEnum(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}
