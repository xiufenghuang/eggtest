package net.maku.egg.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.List;
import lombok.Data;
import java.io.Serializable;
import net.maku.framework.common.utils.DateUtils;
import java.util.Date;

/**
 * 店铺设备重量记录
 *
 * @author 阿沐 babamu@126.com
 * <a href="https://maku.net">MAKU</a>
 */
@Data
@Schema(description = "店铺设备重量记录")
public class EggShopWeightVO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Schema(description = "记录id")
	private Long id;

	@Schema(description = "店铺id")
	private Long shopId;

	@Schema(description = "店铺设备重量")
	private Double weight;

	@Schema(description = "创建时间")
	private Date createTime;

	@Schema(description = "修改人")
	private Long updater;

	@Schema(description = "修改时间")
	private Date updateTime;

	@Schema(description = "删除标识符 (0-未删除, 1-已删除)")
	private Integer deleted;

}