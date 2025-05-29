package net.maku.egg.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import net.maku.framework.common.utils.PageResult;
import net.maku.framework.common.utils.Result;
import net.maku.framework.operatelog.annotations.OperateLog;
import net.maku.framework.operatelog.enums.OperateTypeEnum;
import net.maku.egg.service.EggShopWeightService;
import net.maku.egg.query.EggShopWeightQuery;
import net.maku.egg.vo.EggShopWeightVO;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

/**
 * 店铺设备重量记录
 *
 * @author 阿沐 babamu@126.com
 * <a href="https://maku.net">MAKU</a>
 */
@RestController
@RequestMapping("/business/shopweight")
@Tag(name="店铺设备重量记录")
@AllArgsConstructor
public class EggShopWeightController {
    private final EggShopWeightService eggShopWeightService;

    @GetMapping("page")
    @Operation(summary = "分页")
    @PreAuthorize("hasAuthority('business:shopweight')")
    public Result<PageResult<EggShopWeightVO>> page(@ParameterObject @Valid EggShopWeightQuery query){
        PageResult<EggShopWeightVO> page = eggShopWeightService.page(query);

        return Result.ok(page);
    }


    @GetMapping("{id}")
    @Operation(summary = "信息")
    @PreAuthorize("hasAuthority('business:shopweight')")
    public Result<EggShopWeightVO> get(@PathVariable("id") Long id){
        EggShopWeightVO data = eggShopWeightService.get(id);

        return Result.ok(data);
    }



    @DeleteMapping
    @Operation(summary = "删除")
    @OperateLog(type = OperateTypeEnum.DELETE)
    @PreAuthorize("hasAuthority('business:shopweight')")
    public Result<String> delete(@RequestBody List<Long> idList){
        eggShopWeightService.delete(idList);

        return Result.ok();
    }


    @GetMapping("export")
    @Operation(summary = "导出")
    @OperateLog(type = OperateTypeEnum.EXPORT)
    @PreAuthorize("hasAuthority('business:shopweight')")
    public void export() {
        eggShopWeightService.export();
    }
}