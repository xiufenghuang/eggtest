package net.maku.egg.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import net.maku.framework.common.utils.DateUtils;
import org.hibernate.validator.constraints.Range;

import java.io.Serializable;
import java.util.Date;

/**
 * 设备表
 *
 * @author niitCDL babamu@126.com
 * <a href="https://maku.net">MAKU</a>
 */
@Data
@Schema(description = "设备表")
public class EggDeviceVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "设备ID")
    private Long id;

    @Schema(description = "店铺ID")
    private Long shopId;

    @Schema(description = "店铺名称")
    private String shopName;

    @Schema(description = "价签ID")
    private Long priceTagId;

    @Schema(description = "电子价签路径")
    private String priceTagUrl;

     @Schema(description = "模板ID")
     private Long templateId;

    @Schema(description = "模板名称")
    private String templateName;

    @Schema(description = "设备SN编号")
    @NotBlank(message = "sn编号不能为空")
    private String sn;

    @Schema(description = "设备名称")
    @NotBlank(message = "设备名称不能为空")
    private String name;

    @Schema(description = "设备状态")
    @NotNull(message = "设备状态不能为空")
    @Range(min = 0, max = 2, message = "设备状态参数错误")
    private Integer status;

    @Schema(description = "设备类型")
    private Integer type;

    @Schema(description = "上一次重量")
    private Double previousWeight;

    @Schema(description = "当前重量")
    private Double currentWeight;

    @Schema(description = "创建人")
    private Long creator;

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
    @Schema(description = "微型价签的父设备id")
    private Long parentDevice;
}