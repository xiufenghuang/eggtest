package net.maku.egg.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.annotation.*;
import net.maku.framework.mybatis.entity.MyBaseEntity;

/**
 * 蛋品模板
 *
 * @author niitCDL babamu@126.com
 * <a href="https://maku.net">MAKU</a>
 */
@EqualsAndHashCode(callSuper=false)
@Data
@TableName("egg_template")
public class EggTemplateEntity extends MyBaseEntity {

	/**
	* 模板名称
	*/
	@TableField(value = "name")
	private String name;

	/**
	* 模板JSON参数
	*/
	@TableField(value = "options")
	private String options;

	@TableField(value = "type")
	private Integer type;

	@TableField(value = "device_type")
	private Integer deviceType;
}