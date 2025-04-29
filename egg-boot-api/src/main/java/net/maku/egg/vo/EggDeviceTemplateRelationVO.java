package net.maku.egg.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.List;
import lombok.Data;
import java.io.Serializable;
import net.maku.framework.common.utils.DateUtils;
import java.util.Date;

/**
 * 设备模版关联表
 *
 * @author xiufenguhuang babamu@126.com
 * <a href="https://maku.net">MAKU</a>
 */
@Data
@Schema(description = "设备模版关联表")
public class EggDeviceTemplateRelationVO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Schema(description = "主键")
	private Long id;

	@Schema(description = "设备ID")
	private Integer deviceId;

	@Schema(description = "模板ID")
	private Integer templateId;

	private Date createTime;

	private Date updateTime;

	@Schema(description = "删除标识符 (0-未删除, 1-已删除)")
	private Integer deleteda;

}