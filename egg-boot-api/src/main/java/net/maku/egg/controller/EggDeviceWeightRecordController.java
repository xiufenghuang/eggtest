package net.maku.egg.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import net.maku.egg.dto.DateDto;
import net.maku.egg.entity.EggDeviceWeightRecordEntity;
import net.maku.egg.query.EggDeviceRecordQuery;
import net.maku.egg.query.EggDeviceWeightRecordQuery;
import net.maku.egg.service.EggDeviceRecordService;
import net.maku.egg.service.EggDeviceWeightRecordService;
import net.maku.egg.vo.EggDeviceRecordVO;
import net.maku.egg.vo.EggDeviceWeightRecordVO;
import net.maku.egg.vo.LineChartVO;
import net.maku.framework.common.utils.PageResult;
import net.maku.framework.common.utils.Result;
import net.maku.framework.operatelog.annotations.OperateLog;
import net.maku.framework.operatelog.enums.OperateTypeEnum;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 设备记录表
 *
 * @author 阿沐 babamu@126.com
 * <a href="https://maku.net">MAKU</a>
 */
@RestController
@RequestMapping("/business/record")
@Tag(name="设备重量记录表")
@AllArgsConstructor
public class EggDeviceWeightRecordController {
    private final EggDeviceWeightRecordService eggDeviceWeightRecordService;

    @GetMapping("page")
    @Operation(summary = "分页")
    @PreAuthorize("hasAuthority('device-record:record')")
    public Result<PageResult<EggDeviceWeightRecordVO>> page(@ParameterObject @Valid EggDeviceWeightRecordQuery query){
        PageResult<EggDeviceWeightRecordVO> page = eggDeviceWeightRecordService.page(query);

        return Result.ok(page);
    }


    @GetMapping("{id}")
    @Operation(summary = "信息")
    @PreAuthorize("hasAuthority('device-record:record')")
    public Result<EggDeviceWeightRecordVO> get(@PathVariable("id") Long id){
        EggDeviceWeightRecordVO data = eggDeviceWeightRecordService.get(id);

        return Result.ok(data);
    }

    @PostMapping
    @Operation(summary = "保存")
    @OperateLog(type = OperateTypeEnum.INSERT)
    @PreAuthorize("hasAuthority('device-record:record')")
    public Result<String> save(@RequestBody EggDeviceWeightRecordVO vo){
        eggDeviceWeightRecordService.save(vo);

        return Result.ok();
    }

    @PutMapping
    @Operation(summary = "修改")
    @OperateLog(type = OperateTypeEnum.UPDATE)
    @PreAuthorize("hasAuthority('device-record:record')")
    public Result<String> update(@RequestBody @Valid EggDeviceWeightRecordVO vo){
        eggDeviceWeightRecordService.update(vo);

        return Result.ok();
    }

    @DeleteMapping
    @Operation(summary = "删除")
    @OperateLog(type = OperateTypeEnum.DELETE)
    @PreAuthorize("hasAuthority('device-record:record')")
    public Result<String> delete(@RequestBody List<Long> idList){
        eggDeviceWeightRecordService.delete(idList);

        return Result.ok();
    }


}