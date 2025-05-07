package net.maku.egg.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.annotation.*;
import net.maku.framework.mybatis.entity.MyBaseEntity;

/**
 * 设备表
 *
 * @author niitCDL babamu@126.com
 * <a href="https://maku.net">MAKU</a>
 */
@EqualsAndHashCode(callSuper = false)
@Data
@TableName("egg_device")
public class EggDeviceEntity extends MyBaseEntity {

    /**
     * 店铺ID
     */
    @TableField(value = "shop_id")
    private Long shopId;

    /**
     * 店铺ID
     */
    @TableField(value = "price_tag_id")
    private Long priceTagId;

    /**
     * 店铺ID
     */
     @TableField(value = "template_id")
     private Long templateId;

    /**
     * 设备SN编号
     */
    @TableField(value = "sn")
    private String sn;

    /**
     * 设备名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * 设备状态
     */
    @TableField(value = "status")
    private Integer status;

    /**
     * 设备类型
     */
    @TableField(value = "type")
    private Integer type;

    /**
     * 上一次重量
     */
    @TableField(value = "previous_weight")
    private Double previousWeight;

    /**
     * 当前重量
     */
    @TableField(value = "current_weight")
    private Double currentWeight;

    /**
     * 微型价签的父设备id
     */
    @TableField(value = "parent_device")
    private Long parentDevice;


}