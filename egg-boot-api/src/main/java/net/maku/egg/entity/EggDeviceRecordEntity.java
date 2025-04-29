package net.maku.egg.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.annotation.*;

import java.time.LocalDateTime;
import java.util.Date;

import net.maku.framework.mybatis.entity.BaseEntity;
import net.maku.framework.mybatis.entity.MyBaseEntity;

/**
 * 设备记录表
 *
 * @author 阿沐 babamu@126.com
 * <a href="https://maku.net">MAKU</a>
 */
@EqualsAndHashCode(callSuper = false)
@Data
@TableName("egg_device_record")
public class EggDeviceRecordEntity {

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
    @TableField(value = "sold_weight")
    private Double soldWeight;

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