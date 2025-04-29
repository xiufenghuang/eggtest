package net.maku.egg.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import java.time.Year;
import java.time.YearMonth;

@Data
public class DateDto {
    @NotNull(message = "日期类型不能为空")
    @Range(min = 1,max = 3,message = "日期类型参数错误")
    private Integer dateTypeCode;
    private Year year;
    private YearMonth yearMonth;
    private Long shopId;
    @NotNull(message = "是否查看所有数据不能为空")
    private Boolean viewAllData;
}