package net.maku.egg.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import net.maku.framework.common.utils.PageResult;
import net.maku.framework.common.utils.Result;
import net.maku.framework.operatelog.annotations.OperateLog;
import net.maku.framework.operatelog.enums.OperateTypeEnum;
import net.maku.egg.service.EggShopService;
import net.maku.egg.query.EggShopQuery;
import net.maku.egg.vo.EggShopVO;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

/**
 * 店铺表
 *
 * @author niitCDL babamu@126.com
 * <a href="https://maku.net">MAKU</a>
 */
@RestController
@RequestMapping("/egg/shop")
@Tag(name="店铺表")
@AllArgsConstructor
public class EggShopController {
    private final EggShopService eggShopService;

    @GetMapping("page")
    @Operation(summary = "分页")
    @PreAuthorize("hasAuthority('egg:shop')")
    public Result<PageResult<EggShopVO>> page(@ParameterObject @Valid EggShopQuery query){
        PageResult<EggShopVO> page = eggShopService.page(query);

        return Result.ok(page);
    }


    @GetMapping("{id}")
    @Operation(summary = "信息")
    @PreAuthorize("hasAuthority('egg:shop')")
    public Result<EggShopVO> get(@PathVariable("id") Long id){
        EggShopVO data = eggShopService.get(id);

        return Result.ok(data);
    }

    @GetMapping("list")
    @Operation(summary = "列表")
    public Result<List<EggShopVO>> list(){
        List<EggShopVO> list = eggShopService.getShopList();
        return Result.ok(list);
    }

    @PostMapping
    @Operation(summary = "保存")
    @OperateLog(type = OperateTypeEnum.INSERT)
    @PreAuthorize("hasAuthority('egg:shop')")
    public Result<String> save(@RequestBody EggShopVO vo){
        eggShopService.save(vo);

        return Result.ok();
    }

    @PutMapping
    @Operation(summary = "修改")
    @OperateLog(type = OperateTypeEnum.UPDATE)
    @PreAuthorize("hasAuthority('egg:shop')")
    public Result<String> update(@RequestBody @Valid EggShopVO vo){
        eggShopService.update(vo);

        return Result.ok();
    }

    @DeleteMapping
    @Operation(summary = "删除")
    @OperateLog(type = OperateTypeEnum.DELETE)
    @PreAuthorize("hasAuthority('egg:shop')")
    public Result<String> delete(@RequestBody List<Long> idList){
        eggShopService.delete(idList);

        return Result.ok();
    }
    @GetMapping("wx-shopList")
    @Operation(summary = "列表")
    public Result<List<EggShopVO>> wxShopList(){
        List<EggShopVO> list = eggShopService.getShopListFrWx();
        return Result.ok(list);
    }

}