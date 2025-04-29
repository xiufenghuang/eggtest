package net.maku.egg.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.maku.framework.mybatis.entity.BaseEntity;

/**
 * bin文件版本控制
 *
 * @author 孙超 babamu@126.com
 * <a href="https://maku.net">MAKU</a>
 */
@EqualsAndHashCode(callSuper=false)
@Data
@TableName("egg_file_bin")
public class EggFileBinEntity extends BaseEntity {

	/**
	* 文件URL
	*/
	@TableField(value = "file_url")
	private String fileUrl;

	/**
	* 文件大小
	*/
	@TableField(value = "file_size")
	private Long fileSize;

	/**
	* 文件类型
	*/
	@TableField(value = "file_type")
	private Integer fileType;

	/**
	* 文件版本
	*/
	@TableField(value = "file_version")
	private String fileVersion;







}