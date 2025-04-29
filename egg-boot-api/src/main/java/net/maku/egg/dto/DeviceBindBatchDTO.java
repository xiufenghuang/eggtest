package net.maku.egg.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import java.util.List;

/**
 * @author : lenovo
 * @program : egg-api
 * @description :
 * @create : 2024-09-25 15:42
 **/
@Data
public class DeviceBindBatchDTO {
    private Long shopId;
    private Long priceTagId;
    private Long templateId;
    //绑定的操作类型 1为绑定店铺 2为绑定价签 3为绑定模板
    @Range(min = 1, max = 3)
    private Integer bindOptionType;
    @Size(message = "至少要有一个设备ID")
    private List<Long> idList;
}
