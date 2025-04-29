package net.maku.egg.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 设备记录表
 *
 * @author 阿沐 babamu@126.com
 * <a href="https://maku.net">MAKU</a>
 */
@Data
@Schema(description = "设备记录表")
public class EggDeviceWeightRecordVO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Schema(description = "设备ID")
	private Long id;

	@Schema(description = "设备SN编号")
	private String sn;

	@Schema(description = "设备名称")
	private String deviceName;

	@Schema(description = "售卖重量")
	private Double currentWeight;

	@Schema(description = "创建时间")
	private LocalDateTime createTime;

	@Schema(description = "创建时间")
	private LocalDateTime updateTime;

	@Schema(description = "删除标识符 (0-未删除, 1-已删除)")
	private Integer deleted;

}