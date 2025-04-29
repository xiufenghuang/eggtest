package net.maku.egg.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.maku.framework.common.query.Query;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 设备表查询
 *
 * @author niitCDL babamu@126.com
 * <a href="https://maku.net">MAKU</a>
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "设备表查询")
public class EggDeviceQuery extends Query {

    @Schema(description = "绑定关系")
    private Integer bindRelation;

    @Schema(description = "店铺Id")
    private Long shopId;

    @Schema(description = "店铺名称")
    private String shopName;

    @Schema(description = "设备SN编号")
    private String sn;

    @Schema(description = "设备名称")
    private String name;

    @Schema(description = "设备状态")
    private Integer status;

}