package net.maku.egg.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import net.maku.egg.service.EggDeviceTemplateRelationService;
import net.maku.framework.common.utils.PageResult;
import net.maku.framework.common.utils.Result;
import net.maku.framework.operatelog.annotations.OperateLog;
import net.maku.framework.operatelog.enums.OperateTypeEnum;

import net.maku.egg.query.EggDeviceTemplateRelationQuery;
import net.maku.egg.vo.EggDeviceTemplateRelationVO;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

/**
 * 设备模版关联表
 *
 * @author xiufenguhuang babamu@126.com
 * <a href="https://maku.net">MAKU</a>
 */
@RestController
@RequestMapping("/devicetemplate/relation")
@Tag(name="设备模版关联表")
@AllArgsConstructor
public class EggDeviceTemplateRelationController {
    private final EggDeviceTemplateRelationService eggDeviceTemplateRelationService;

    @GetMapping("page")
    @Operation(summary = "分页")
    @PreAuthorize("hasAuthority('devicetemplate:relation')")
    public Result<PageResult<EggDeviceTemplateRelationVO>> page(@ParameterObject @Valid EggDeviceTemplateRelationQuery query){
        PageResult<EggDeviceTemplateRelationVO> page = eggDeviceTemplateRelationService.page(query);

        return Result.ok(page);
    }


    @GetMapping("{id}")
    @Operation(summary = "信息")
    @PreAuthorize("hasAuthority('devicetemplate:relation')")
    public Result<EggDeviceTemplateRelationVO> get(@PathVariable("id") Long id){
        EggDeviceTemplateRelationVO data = eggDeviceTemplateRelationService.get(id);

        return Result.ok(data);
    }

    @PostMapping
    @Operation(summary = "保存")
    @OperateLog(type = OperateTypeEnum.INSERT)
    @PreAuthorize("hasAuthority('devicetemplate:relation')")
    public Result<String> save(@RequestBody EggDeviceTemplateRelationVO vo){
        eggDeviceTemplateRelationService.save(vo);

        return Result.ok();
    }

    @PutMapping
    @Operation(summary = "修改")
    @OperateLog(type = OperateTypeEnum.UPDATE)
    @PreAuthorize("hasAuthority('devicetemplate:relation')")
    public Result<String> update(@RequestBody @Valid EggDeviceTemplateRelationVO vo){
        eggDeviceTemplateRelationService.update(vo);

        return Result.ok();
    }

    @DeleteMapping
    @Operation(summary = "删除")
    @OperateLog(type = OperateTypeEnum.DELETE)
    @PreAuthorize("hasAuthority('devicetemplate:relation')")
    public Result<String> delete(@RequestBody List<Long> idList){
        eggDeviceTemplateRelationService.delete(idList);

        return Result.ok();
    }

}