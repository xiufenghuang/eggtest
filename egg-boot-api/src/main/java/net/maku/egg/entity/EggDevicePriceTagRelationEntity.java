package net.maku.egg.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.annotation.*;

/**
 * 设备电子价签关系表
 *
 * @author 阿沐 babamu@126.com
 * <a href="https://maku.net">MAKU</a>
 */

@Data
@TableName("egg_device_price_tag_relation")
public class EggDevicePriceTagRelationEntity {
	/**
	* 关系ID
	*/
	@TableId
	@TableField(value = "id")
	private Long id;

	/**
	* 设备SN编号
	*/
	@TableField(value = "sn")
	private String sn;

	/**
	* 电子价签ID
	*/
	@TableField(value = "price_tag_id")
	private Long priceTagId;

}