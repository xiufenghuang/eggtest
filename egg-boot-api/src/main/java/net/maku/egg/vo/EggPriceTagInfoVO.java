package net.maku.egg.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.List;
import lombok.Data;
import java.io.Serializable;
import net.maku.framework.common.utils.DateUtils;
import net.maku.system.vo.FileVO;

import java.util.Date;

/**
 * 电子价签表
 *
 * @author niitCDL babamu@126.com
 * <a href="https://maku.net">MAKU</a>
 */
@Data
@Schema(description = "电子价签表")
public class EggPriceTagInfoVO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Schema(description = "电子价签ID")
	private Long id;

	@Schema(description = "电子价签名称")
	private String name;

	@Schema(description = "电子价签图片路径")
	private String imageUrl;

	@Schema(description = "店铺图片路径对象列表")
	private List<FileVO> imageFileList;

	@Schema(description = "创建时间")
	@JsonFormat(pattern = DateUtils.DATE_TIME_PATTERN)
	private Date createTime;

	@Schema(description = "修改人")
	private Long updater;

	@Schema(description = "修改时间")
	@JsonFormat(pattern = DateUtils.DATE_TIME_PATTERN)
	private Date updateTime;

	@Schema(description = "删除标识符 (0-未删除, 1-已删除)")
	private Integer deleted;

}