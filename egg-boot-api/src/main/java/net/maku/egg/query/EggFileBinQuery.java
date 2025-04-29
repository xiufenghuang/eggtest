package net.maku.egg.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.maku.framework.common.query.Query;

/**
 * bin文件版本控制查询
 *
 * @author 孙超 babamu@126.com
 * <a href="https://maku.net">MAKU</a>
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "bin文件版本控制查询")
public class EggFileBinQuery extends Query {
    @Schema(description = "bin文件id")
    private Long id;

    @Schema(description = "文件类型")
    private Integer fileType;

    @Schema(description = "文件版本")
    private String fileVersion;

}