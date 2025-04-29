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
 * 店铺表
 *
 * @author niitCDL babamu@126.com
 * <a href="https://maku.net">MAKU</a>
 */
@Data
@Schema(description = "店铺表")
public class EggShopVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "店铺ID")
    private Long id;

    @Schema(description = "机构ID")
    private Long orgId;

    @Schema(description = "所属机构名称")
    private String orgName;

    @Schema(description = "负责人ID")
    private Long leaderId;

    @Schema(description = "负责人名称")
    private String leaderName;

    @Schema(description = "店铺名称")
    private String name;

    @Schema(description = "店铺地址")
    private String address;

    @Schema(description = "店铺图片路径")
    private String shopImageUrl;

    @Schema(description = "店铺图片路径对象列表")
    private List<FileVO> imageFileList;

    @Schema(description = "创建人")
    private Long creator;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "修改人")
    private Long updater;

    @Schema(description = "修改时间")
    private Date updateTime;

    @Schema(description = "删除标识符 (0-未删除, 1-已删除)")
    private Integer deleted;

}