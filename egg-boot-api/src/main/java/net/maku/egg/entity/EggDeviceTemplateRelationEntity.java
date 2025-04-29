package net.maku.egg.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.annotation.*;

import java.time.LocalDateTime;
import java.util.Date;
import net.maku.framework.mybatis.entity.BaseEntity;

/**
 * 设备模版关联表
 *
 * @author xiufenguhuang babamu@126.com
 * <a href="https://maku.net">MAKU</a>
 */
@EqualsAndHashCode(callSuper=false)
@Data
@TableName("egg_device_template_relation")
public class EggDeviceTemplateRelationEntity {
	@TableId
	private Long id;
	/**
	* 设备ID
	*/
	@TableField(value = "device_id")
	private Long deviceId;

	/**
	* 模板ID
	*/
	@TableField(value = "template_id")
	private Long templateId;
	/**
	 * 创建时间
	 */
	@TableField(fill = FieldFill.INSERT)
	private LocalDateTime createTime;


	/**
	 * 更新时间
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	private LocalDateTime updateTime;

	/**
	* 删除标识符 (0-未删除, 1-已删除)
	*/
	@TableField(value = "deleted")
	private Integer deleted;

}