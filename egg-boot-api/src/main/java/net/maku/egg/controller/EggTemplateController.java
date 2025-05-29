package net.maku.egg.controller;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import net.maku.egg.convert.EggTemplateConvert;
import net.maku.egg.entity.EggTemplateEntity;
import net.maku.egg.query.EggTemplateQuery;
import net.maku.egg.service.EggTemplateService;
import net.maku.egg.vo.EggTemplateVO;
import net.maku.framework.common.utils.PageResult;
import net.maku.framework.common.utils.Result;
import net.maku.framework.operatelog.annotations.OperateLog;
import net.maku.framework.operatelog.enums.OperateTypeEnum;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 蛋品模板
 *
 * @author niitCDL babamu@126.com
 * <a href="https://maku.net">MAKU</a>
 */
@RestController
@RequestMapping("/template")
@Tag(name="蛋品模板")
@AllArgsConstructor
public class EggTemplateController {
    private final EggTemplateService eggTemplateService;

    @GetMapping("page")
    @Operation(summary = "分页")
    public Result<PageResult<EggTemplateVO>> page(@ParameterObject @Valid EggTemplateQuery query){
        PageResult<EggTemplateVO> page = eggTemplateService.page(query);

        return Result.ok(page);
    }


    @GetMapping("{id}")
    @Operation(summary = "信息")
    public Result<EggTemplateVO> get(@PathVariable("id") Long id){
        EggTemplateVO data = eggTemplateService.get(id);
        return Result.ok(data);
    }

    @GetMapping("list")
    @Operation(summary = "信息")
    public Result<List<EggTemplateVO>> list(@RequestParam(value = "deviceType", required = false) String deviceType){
        LambdaQueryWrapper<EggTemplateEntity> queryWrapper = Wrappers.lambdaQuery();
        // 如果deviceType不为空，则添加查询条件
        if (deviceType != null && !deviceType.isEmpty()) {
            queryWrapper.eq(EggTemplateEntity::getDeviceType, deviceType);
        }
//        queryWrapper.select(EggTemplateEntity::getId,EggTemplateEntity::getName);
        List<EggTemplateEntity> list = eggTemplateService.list(queryWrapper);
        return Result.ok(EggTemplateConvert.INSTANCE.convertList(list));
    }

    @PostMapping
    @Operation(summary = "保存")
    @OperateLog(type = OperateTypeEnum.INSERT)
    public Result<String> save(@RequestBody EggTemplateVO vo){
        eggTemplateService.save(vo);

        return Result.ok();
    }

    @PutMapping
    @Operation(summary = "修改")
    @OperateLog(type = OperateTypeEnum.UPDATE)
    public Result<String> update(@RequestBody @Valid EggTemplateVO vo){
        eggTemplateService.update(vo);

        return Result.ok();
    }

    @DeleteMapping
    @Operation(summary = "删除")
    @OperateLog(type = OperateTypeEnum.DELETE)
    public Result<String> delete(@RequestBody List<Long> idList){
        eggTemplateService.delete(idList);

        return Result.ok();
    }


}