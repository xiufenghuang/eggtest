package net.maku.egg.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import net.maku.egg.service.EggShopService;
import net.maku.framework.common.utils.PageResult;
import net.maku.framework.common.utils.Result;
import net.maku.system.query.SysUserQuery;
import net.maku.system.service.SysUserService;
import net.maku.system.vo.SysUserVO;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/egg/shopAsi")
public class EggShopAsiController {

    private EggShopService eggShopService;

    @GetMapping("page")
    @Operation(summary = "分页")
    @PreAuthorize("hasAuthority('sys:user:page')")
    public Result<PageResult<SysUserVO>> pageByShop(@ParameterObject @Valid SysUserQuery query) {
        PageResult<SysUserVO> page = eggShopService.pageByShop(query);

        return Result.ok(page);
    }


}
