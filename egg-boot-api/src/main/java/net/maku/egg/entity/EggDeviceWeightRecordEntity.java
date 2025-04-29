package net.maku.egg.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 设备记录表
 *
 * @author 阿沐 babamu@126.com
 * <a href="https://maku.net">MAKU</a>
 */
@EqualsAndHashCode(callSuper = false)
@Data
@TableName("egg_device_weight_record")
public class EggDeviceWeightRecordEntity {

    @TableId
    private Long id;
    /**
     * 设备SN编号
     */
    @TableField(value = "sn")
    private String sn;

    /**
     * 售卖重量
     */
    @TableField(value = "current_weight")
    private Double currentWeight;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime updateTime;

    /**
     * 删除标记
     */
    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    private Integer deleted;
}