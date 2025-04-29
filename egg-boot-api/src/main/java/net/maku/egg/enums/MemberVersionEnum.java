package net.maku.egg.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberVersionEnum {
    MEMBER(9999,"店员");
    /**
     * 类型值
     */
    private final Integer value;

    /**
     * 类型名称
     */
    private final String title;
}
