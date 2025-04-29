package net.maku.egg.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;

import net.maku.framework.mybatis.entity.BaseEntity;
import net.maku.framework.mybatis.entity.MyBaseEntity;

/**
 * 店铺表
 *
 * @author niitCDL babamu@126.com
 * <a href="https://maku.net">MAKU</a>
 */
@EqualsAndHashCode(callSuper = false)
@Data
@TableName("egg_shop")
public class EggShopEntity extends MyBaseEntity {

    /**
     * 机构ID
     */
    @TableField(value = "org_id")
    private Long orgId;


    /**
     * 负责人Id
     */
    @TableField(value = "leader_id",updateStrategy = FieldStrategy.IGNORED)
    private Long leaderId;

    /**
     * 店铺名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * 店铺地址
     */
    @TableField(value = "address")
    private String address;

    @TableField(value = "shop_image_url")
    private String shopImageUrl;


}