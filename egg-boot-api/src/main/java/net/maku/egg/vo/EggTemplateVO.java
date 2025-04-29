package net.maku.egg.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 蛋品模板
 *
 * @author niitCDL babamu@126.com
 * <a href="https://maku.net">MAKU</a>
 */
@Data
@Schema(description = "蛋品模板")
public class EggTemplateVO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Schema(description = "主键")
	private Long id;

	@Schema(description = "模板名称")
	private String name;

	@Schema(description = "模板类别")
	private Integer type;

	@Schema(description = "模板JSON参数")
	private String options;

	@Schema(description = "模板归属模版类别")
	private Integer deviceType;

}