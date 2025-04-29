package net.maku.egg.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * bin文件版本控制
 *
 * @author 孙超 babamu@126.com
 * <a href="https://maku.net">MAKU</a>
 */
@Data
@Schema(description = "bin文件版本控制")
public class EggFileBinVO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Schema(description = "bin文件id")
	private Long id;

	@Schema(description = "文件URL")
	private String fileUrl;

	@Schema(description = "文件大小")
	private Long fileSize;

	@Schema(description = "文件类型")
	private Integer fileType;

	@Schema(description = "文件版本")
	private String fileVersion;

	@Schema(description = "版本")
	private String version;

	@Schema(description = "创建人")
	private Long creator;

	@Schema(description = "创建时间")
	private Date createTime;

	@Schema(description = "修改人")
	private Long updater;

	@Schema(description = "修改时间")
	private Date updateTime;

	@Schema(description = "删除标识符 (0-未删除, 1-已删除)")
	private Integer deleted;

}