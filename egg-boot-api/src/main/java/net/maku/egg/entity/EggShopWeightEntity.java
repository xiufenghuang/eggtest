package net.maku.egg.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.maku.framework.mybatis.entity.MyBaseEntity;

import java.util.Date;

/**
 * 店铺重量记录表
 */
@Data
@EqualsAndHashCode(callSuper=false)
@TableName("egg_shop_weight")
public class EggShopWeightEntity  extends MyBaseEntity {
	@TableId
	private Long id;
	
	/**
	 * 店铺ID
	 */
	private Long shopId;
	
	/**
	 * 总重量
	 */
	private Double weight;
	

}