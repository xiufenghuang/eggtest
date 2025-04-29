package net.maku.egg.dto;

import lombok.Data;

/**
 * @author : lenovo
 * @program : egg-api
 * @description :
 * @create : 2024-09-12 15:12
 **/
@Data
public class DeviceWeightDTO {
    private String sn;
    private Double currentWeight;
}
