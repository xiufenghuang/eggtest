package net.maku.egg.vo;

import lombok.Data;

@Data
public class EggBindVO {
    private Long shopId;
    private String sn;
    private String deviceName;
    private Integer type;
    private Long creator;
}
