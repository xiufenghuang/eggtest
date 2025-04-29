package net.maku.egg.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import net.maku.egg.entity.EggFileBinEntity;
import net.maku.egg.query.EggFileBinQuery;
import net.maku.egg.service.EggFileBinService;
import net.maku.egg.vo.EggFileBinVO;
import net.maku.framework.common.utils.PageResult;
import net.maku.framework.common.utils.Result;
import net.maku.framework.operatelog.annotations.OperateLog;
import net.maku.framework.operatelog.enums.OperateTypeEnum;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * bin文件版本控制
 *
 * @author 孙超 babamu@126.com
 * <a href="https://maku.net">MAKU</a>
 */
@RestController
@RequestMapping("/file/bin")
@Tag(name="bin文件版本控制")
@AllArgsConstructor
public class EggFileBinController {
    private final EggFileBinService eggFileBinService;

    @GetMapping("page")
    @Operation(summary = "分页")
    @PreAuthorize("hasAuthority('file:bin')")
    public Result<PageResult<EggFileBinVO>> page(@ParameterObject @Valid EggFileBinQuery query){
        PageResult<EggFileBinVO> page = eggFileBinService.page(query);

        return Result.ok(page);
    }


    @GetMapping("{id}")
    @Operation(summary = "信息")
    @PreAuthorize("hasAuthority('file:bin')")
    public Result<EggFileBinVO> get(@PathVariable("id") Long id){
        EggFileBinVO data = eggFileBinService.get(id);

        return Result.ok(data);
    }

    @PostMapping
    @Operation(summary = "保存")
    @OperateLog(type = OperateTypeEnum.INSERT)
    @PreAuthorize("hasAuthority('file:bin')")
    public Result<String> save(@RequestBody EggFileBinVO vo){
        eggFileBinService.save(vo);

        return Result.ok();
    }

    @PutMapping
    @Operation(summary = "修改")
    @OperateLog(type = OperateTypeEnum.UPDATE)
    @PreAuthorize("hasAuthority('file:bin')")
    public Result<String> update(@RequestBody @Valid EggFileBinVO vo){
        eggFileBinService.update(vo);

        return Result.ok();
    }

    @DeleteMapping
    @Operation(summary = "删除")
    @OperateLog(type = OperateTypeEnum.DELETE)
    @PreAuthorize("hasAuthority('file:bin')")
    public Result<String> delete(@RequestBody List<Long> idList){
        eggFileBinService.delete(idList);

        return Result.ok();
    }

    @GetMapping("file-bin")
    @Operation(summary = "获得最新bin文件")
    @OperateLog(type = OperateTypeEnum.DELETE)
    public Result<EggFileBinEntity> binaryFile(@RequestParam(required = true) int type){
        EggFileBinEntity entity = eggFileBinService.binaryFile(type);

        if(entity == null) {
            return Result.error("没有该类型的数据");
        }else {
            return Result.ok(entity);
        }
    }

    @GetMapping("download-chunk")
    @Operation(summary = "分片下载")
    public ResponseEntity<org.springframework.core.io.Resource> downloadFileInChunks(
            @RequestParam int type,
            @RequestParam(value = "Range", required = true) String rangeHeader) {

        EggFileBinEntity entity = eggFileBinService.binaryFile(type);
        return eggFileBinService.downloadFileInChunks(type,entity,rangeHeader);
    }
}