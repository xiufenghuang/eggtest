package net.maku.egg.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.List;
import lombok.Data;
import java.io.Serializable;
import net.maku.framework.common.utils.DateUtils;

/**
 * 设备电子价签关系表
 *
 * @author 阿沐 babamu@126.com
 * <a href="https://maku.net">MAKU</a>
 */
@Data
@Schema(description = "设备电子价签关系表")
public class EggDevicePriceTagRelationVO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Schema(description = "关系ID")
	private Long id;

	@Schema(description = "设备SN编号")
	private String sn;

	@Schema(description = "电子价签ID")
	private Long priceTagId;

}