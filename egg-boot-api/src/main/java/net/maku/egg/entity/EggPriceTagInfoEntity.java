package net.maku.egg.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.annotation.*;
import java.util.Date;
import net.maku.framework.mybatis.entity.BaseEntity;
import net.maku.framework.mybatis.entity.MyBaseEntity;

/**
 * 电子价签表
 *
 * @author niitCDL babamu@126.com
 * <a href="https://maku.net">MAKU</a>
 */
@EqualsAndHashCode(callSuper=false)
@Data
@TableName("egg_price_tag_info")
public class EggPriceTagInfoEntity extends MyBaseEntity {

	/**
	* 电子价签名称
	*/
	@TableField(value = "name")
	private String name;

	/**
	* 电子价签图片路径
	*/
	@TableField(value = "image_url")
	private String imageUrl;





}