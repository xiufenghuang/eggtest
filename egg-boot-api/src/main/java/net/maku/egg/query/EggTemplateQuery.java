package net.maku.egg.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.maku.framework.common.query.Query;
import org.springframework.format.annotation.DateTimeFormat;


/**
 * 蛋品模板查询
 *
 * @author niitCDL babamu@126.com
 * <a href="https://maku.net">MAKU</a>
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "蛋品模板查询")
public class EggTemplateQuery extends Query {
    @Schema(description = "模板名称")
    private String name;
    @Schema(description = "模板归属模版类别")
    private Integer deviceType;

}