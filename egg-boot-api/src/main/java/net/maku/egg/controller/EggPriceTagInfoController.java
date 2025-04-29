package net.maku.egg.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import net.maku.egg.vo.EggShopVO;
import net.maku.framework.common.utils.PageResult;
import net.maku.framework.common.utils.Result;
import net.maku.framework.operatelog.annotations.OperateLog;
import net.maku.framework.operatelog.enums.OperateTypeEnum;
import net.maku.egg.service.EggPriceTagInfoService;
import net.maku.egg.query.EggPriceTagInfoQuery;
import net.maku.egg.vo.EggPriceTagInfoVO;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

/**
 * 电子价签表
 *
 * @author niitCDL babamu@126.com
 * <a href="https://maku.net">MAKU</a>
 */
@RestController
@RequestMapping("/price-tag/info")
@Tag(name="电子价签表")
@AllArgsConstructor
public class EggPriceTagInfoController {
    private final EggPriceTagInfoService eggPriceTagInfoService;

    @GetMapping("page")
    @Operation(summary = "分页")
    @PreAuthorize("hasAuthority('price-tag:info')")
    public Result<PageResult<EggPriceTagInfoVO>> page(@ParameterObject @Valid EggPriceTagInfoQuery query){
        PageResult<EggPriceTagInfoVO> page = eggPriceTagInfoService.page(query);

        return Result.ok(page);
    }


    @GetMapping("{id}")
    @Operation(summary = "信息")
    @PreAuthorize("hasAuthority('price-tag:info')")
    public Result<EggPriceTagInfoVO> get(@PathVariable("id") Long id){
        EggPriceTagInfoVO data = eggPriceTagInfoService.get(id);

        return Result.ok(data);
    }

    @GetMapping("list")
    @Operation(summary = "列表")
    @PreAuthorize("hasAuthority('price-tag:info')")
    public Result<List<EggPriceTagInfoVO>> list(){
        List<EggPriceTagInfoVO> list = eggPriceTagInfoService.getPriceTagList();
        return Result.ok(list);
    }

    @PostMapping
    @Operation(summary = "保存")
    @OperateLog(type = OperateTypeEnum.INSERT)
    @PreAuthorize("hasAuthority('price-tag:info')")
    public Result<String> save(@RequestBody EggPriceTagInfoVO vo){
        eggPriceTagInfoService.save(vo);

        return Result.ok();
    }

    @PutMapping
    @Operation(summary = "修改")
    @OperateLog(type = OperateTypeEnum.UPDATE)
    @PreAuthorize("hasAuthority('price-tag:info')")
    public Result<String> update(@RequestBody @Valid EggPriceTagInfoVO vo){
        eggPriceTagInfoService.update(vo);

        return Result.ok();
    }

    @DeleteMapping
    @Operation(summary = "删除")
    @OperateLog(type = OperateTypeEnum.DELETE)
    @PreAuthorize("hasAuthority('price-tag:info')")
    public Result<String> delete(@RequestBody List<Long> idList){
        eggPriceTagInfoService.delete(idList);

        return Result.ok();
    }


}