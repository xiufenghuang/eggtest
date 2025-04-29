package net.maku.egg.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import net.maku.egg.dto.DateDto;
import net.maku.egg.vo.LineChartVO;
import net.maku.framework.common.utils.PageResult;
import net.maku.framework.common.utils.Result;
import net.maku.framework.operatelog.annotations.OperateLog;
import net.maku.framework.operatelog.enums.OperateTypeEnum;
import net.maku.egg.service.EggDeviceRecordService;
import net.maku.egg.query.EggDeviceRecordQuery;
import net.maku.egg.vo.EggDeviceRecordVO;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

/**
 * 设备记录表
 *
 * @author 阿沐 babamu@126.com
 * <a href="https://maku.net">MAKU</a>
 */
@RestController
@RequestMapping("/device-record")
@Tag(name="设备记录表")
@AllArgsConstructor
public class EggDeviceRecordController {
    private final EggDeviceRecordService eggDeviceRecordService;

    @GetMapping("page")
    @Operation(summary = "分页")
    @PreAuthorize("hasAuthority('device-record:record')")
    public Result<PageResult<EggDeviceRecordVO>> page(@ParameterObject @Valid EggDeviceRecordQuery query){
        PageResult<EggDeviceRecordVO> page = eggDeviceRecordService.page(query);

        return Result.ok(page);
    }


    @GetMapping("{id}")
    @Operation(summary = "信息")
    @PreAuthorize("hasAuthority('device-record:record')")
    public Result<EggDeviceRecordVO> get(@PathVariable("id") Long id){
        EggDeviceRecordVO data = eggDeviceRecordService.get(id);

        return Result.ok(data);
    }

    @PostMapping
    @Operation(summary = "保存")
    @OperateLog(type = OperateTypeEnum.INSERT)
    @PreAuthorize("hasAuthority('device-record:record')")
    public Result<String> save(@RequestBody EggDeviceRecordVO vo){
        eggDeviceRecordService.save(vo);

        return Result.ok();
    }

    @PutMapping
    @Operation(summary = "修改")
    @OperateLog(type = OperateTypeEnum.UPDATE)
    @PreAuthorize("hasAuthority('device-record:record')")
    public Result<String> update(@RequestBody @Valid EggDeviceRecordVO vo){
        eggDeviceRecordService.update(vo);

        return Result.ok();
    }

    @DeleteMapping
    @Operation(summary = "删除")
    @OperateLog(type = OperateTypeEnum.DELETE)
    @PreAuthorize("hasAuthority('device-record:record')")
    public Result<String> delete(@RequestBody List<Long> idList){
        eggDeviceRecordService.delete(idList);

        return Result.ok();
    }


    @GetMapping("export")
    @Operation(summary = "导出")
    @OperateLog(type = OperateTypeEnum.EXPORT)
    @PreAuthorize("hasAuthority('device-record:record')")
    public void export() {
        eggDeviceRecordService.export();
    }

    @PostMapping("/sold-weight/chart")
    public Result chart(@RequestBody @Valid DateDto dateDto) {
        LineChartVO lineChart = eggDeviceRecordService.getSoldWeightChart(dateDto);
        return Result.ok(lineChart);
    }
}