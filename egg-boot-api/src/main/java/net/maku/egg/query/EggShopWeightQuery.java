package net.maku.egg.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.maku.framework.common.query.Query;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 店铺重量记录查询
 *
 * @author 阿沐 babamu@126.com
 * <a href="https://maku.net">MAKU</a>
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "店铺重量记录查询")
public class EggShopWeightQuery extends Query {
    @Schema(description = "店铺ID")
    private Long shopId;

    @Schema(description = "开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startTime;

    @Schema(description = "结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endTime;
}