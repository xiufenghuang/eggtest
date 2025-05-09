package net.maku.egg.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import net.maku.egg.dto.DeviceBindBatchDTO;
import net.maku.egg.dto.DeviceWithTemplatesDTO;
import net.maku.egg.entity.EggDeviceEntity;
import net.maku.egg.utils.DeviceParamsUtils;
import net.maku.egg.vo.EggBindVO;
import net.maku.egg.vo.EggDeviceTemplateVO;
import net.maku.framework.common.utils.PageResult;
import net.maku.framework.common.utils.Result;
import net.maku.framework.operatelog.annotations.OperateLog;
import net.maku.framework.operatelog.enums.OperateTypeEnum;
import net.maku.egg.service.EggDeviceService;
import net.maku.egg.query.EggDeviceQuery;
import net.maku.egg.vo.EggDeviceVO;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

/**
 * 设备表
 *
 * @author niitCDL babamu@126.com
 * <a href="https://maku.net">MAKU</a>
 */
@RestController
@RequestMapping("/device")
@Tag(name = "设备表")
@AllArgsConstructor
public class EggDeviceController {
    private final EggDeviceService eggDeviceService;

    @GetMapping("page")
    @Operation(summary = "分页")
//    @PreAuthorize("hasAuthority('business:device')")
    public Result<PageResult<DeviceWithTemplatesDTO>> page(@ParameterObject @Valid EggDeviceQuery query) {
   //   PageResult<EggDeviceVO> page = eggDeviceService.page(query);
      PageResult<DeviceWithTemplatesDTO> page =eggDeviceService.pageWithTemplates(query);
        return Result.ok(page);
    }


    @GetMapping("{id}")
    @Operation(summary = "信息")
    @PreAuthorize("hasAuthority('business:device')")
    public Result<EggDeviceVO> get(@PathVariable("id") Long id) {
        EggDeviceVO data = eggDeviceService.get(id);
        return Result.ok(data);
    }

    @PostMapping
    @Operation(summary = "保存")
    @OperateLog(type = OperateTypeEnum.INSERT)
    @PreAuthorize("hasAuthority('business:device')")
    public Result<String> save(@RequestBody EggDeviceTemplateVO vo) {
        eggDeviceService.saveDeviceTemplate(vo);

        return Result.ok();
    }

    @PutMapping
    @Operation(summary = "修改")
    @OperateLog(type = OperateTypeEnum.UPDATE)
//    @PreAuthorize("hasAuthority('business:device')")
    public Result<String> update(@RequestBody @Valid EggDeviceTemplateVO vo) {
        Result<String> result = eggDeviceService.updateDeviceTemplate(vo);
//        eggDeviceService.update(vo);

        return result;
    }

    @DeleteMapping
    @Operation(summary = "删除")
    @OperateLog(type = OperateTypeEnum.DELETE)
    @PreAuthorize("hasAuthority('business:device')")
    public Result<String> delete(@RequestBody List<Long> idList) {
        eggDeviceService.deleteDeviceTemplate(idList);

        return Result.ok();
    }


    @GetMapping("export")
    @Operation(summary = "导出")
    @OperateLog(type = OperateTypeEnum.EXPORT)
    @PreAuthorize("hasAuthority('business:device')")
    public void export() {
        eggDeviceService.export();
    }


    @PostMapping("heart-beat")
    @Operation(summary = "心跳连接")
    public Result<String> heartBeat(@RequestBody String jsonStr) throws JsonProcessingException {
        EggDeviceVO deviceVO = DeviceParamsUtils.decodeString(jsonStr, EggDeviceVO.class);
        eggDeviceService.heartBeat(deviceVO);
        return Result.ok();
    }

    @GetMapping("get/latest/price-tag/{sn}")
    @Operation(summary = "获取最新的电子价签")
    public Result<String> getLatestPriceTag(@PathVariable String sn) throws JsonProcessingException {
        String priceTagUrl = eggDeviceService.getLatestPriceTag(sn);
        return Result.ok(priceTagUrl);
    }


    @PostMapping("bindBatch")
    @Operation(summary = "批量绑定")
    public Result<String> bindBatch(@Valid @RequestBody DeviceBindBatchDTO bindBatchDTO){
        eggDeviceService.bindBatch(bindBatchDTO);
        return Result.ok("批量绑定成功");
    }

    @PostMapping("bindDevice")
    @Operation(summary = "根据已有设备进行绑定店铺")
    public Result<String> bindDevice(@RequestBody EggBindVO bindVO){
        eggDeviceService.bindDevice(bindVO);
        return Result.ok("绑定成功");
    }

    @GetMapping("org/{id}")
    @Operation(summary = "根据orgId获取设备列表")
    public Result<List<EggDeviceVO>> getDeviceByOrg(@PathVariable("id") Long shopId){
        List<EggDeviceVO> list = eggDeviceService.getDeviceListByShop(shopId);
        return Result.ok(list);
    }

    @GetMapping("mini/no-bind")
    @Operation(summary = "获取未绑定的微型价签设备列表")
    public Result<List<EggDeviceVO>> getNoBindMiniList() {
        List<EggDeviceVO> list = eggDeviceService.getNoBindMiniList();
        return Result.ok(list);
    }

    @GetMapping("mini/bind/{id}")
    @Operation(summary = "根据网关ID获取已绑定的迷你设备列表")
    public Result<List<EggDeviceVO>> getHaveBindMiniListByGatewayId(@PathVariable("id") Long id) {
        List<EggDeviceVO> list = eggDeviceService.getHaveBindMiniListByGatewayId(id);
        return Result.ok(list);
    }

    @DeleteMapping("mini/havebind/{id}")
    @Operation(summary = "删除迷你设备绑定关系")
    @OperateLog(type = OperateTypeEnum.DELETE)
    public Result<String> deleteBindMiniRelation(@PathVariable("id") Long id) {
        eggDeviceService.deleteBindMiniRelation(id);
        return Result.ok("删除绑定关系成功");
    }

    @PostMapping("mini/bind/{parentDeviceId}")
    @Operation(summary = "保存迷你设备绑定关系")
    @OperateLog(type = OperateTypeEnum.INSERT)
    public Result<String> saveBindMini(@RequestBody List<Long> ids, @PathVariable("parentDeviceId") Long parentDeviceId) {
        eggDeviceService.saveBindMini(ids, parentDeviceId);
        return Result.ok("绑定成功");
    }
}