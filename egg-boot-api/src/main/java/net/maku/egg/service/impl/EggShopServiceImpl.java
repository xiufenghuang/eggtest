package net.maku.egg.service.impl;

import cn.hutool.core.lang.Opt;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.AllArgsConstructor;
import net.maku.egg.utils.ExplainPropsUtil;
import net.maku.framework.common.utils.PageResult;
import net.maku.framework.mybatis.service.impl.BaseServiceImpl;
import net.maku.egg.convert.EggShopConvert;
import net.maku.egg.entity.EggShopEntity;
import net.maku.egg.query.EggShopQuery;
import net.maku.egg.vo.EggShopVO;
import net.maku.egg.dao.EggShopDao;
import net.maku.egg.service.EggShopService;
import cn.hutool.core.util.ObjectUtil;
import net.maku.framework.security.user.SecurityUser;
import net.maku.framework.security.user.UserDetail;
import net.maku.system.entity.SysMailConfigEntity;
import net.maku.system.entity.SysUserEntity;
import net.maku.system.enums.SuperAdminEnum;
import net.maku.system.query.SysUserQuery;
import net.maku.system.service.SysOrgService;
import net.maku.system.service.SysUserService;
import net.maku.system.vo.FileVO;
import net.maku.system.vo.SysUserVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * 店铺表
 *
 * @author niitCDL babamu@126.com
 * <a href="https://maku.net">MAKU</a>
 */
@Service
@AllArgsConstructor
public class EggShopServiceImpl extends BaseServiceImpl<EggShopDao, EggShopEntity> implements EggShopService {

    private SysOrgService orgService;

    private SysUserService userService;

    @Override
    public PageResult<EggShopVO> page(EggShopQuery query) {
        IPage<EggShopEntity> page = baseMapper.selectPage(getPage(query), getWrapper(query));
        List<EggShopVO> eggShopList = EggShopConvert.INSTANCE.convertList(page.getRecords());
        eggShopList.stream().forEach(shop -> {
            String orgName = Optional.ofNullable(shop.getOrgId())
                    .map(orgId -> ExplainPropsUtil.getPropsLabel(orgService.getNameList(List.of(orgId)), "未知机构"))
                    .orElseGet(() -> "未绑定机构");
            String leaderName = Optional.ofNullable(shop.getLeaderId())
                    .map(leaderId -> ExplainPropsUtil.getPropsLabel(userService.getRealNameList(List.of(leaderId)), "未知负责人"))
                    .orElseGet(() -> "无负责人");
            shop.setOrgName(orgName);
            shop.setLeaderName(leaderName);
            //处理图片属性
            handleImageFileList(shop);
        });
        return new PageResult<>(eggShopList, page.getTotal());
    }


    private LambdaQueryWrapper<EggShopEntity> getWrapper(EggShopQuery query) {
        LambdaQueryWrapper<EggShopEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.like(ObjectUtil.isNotEmpty(query.getName()), EggShopEntity::getName, query.getName());
        wrapper.like(ObjectUtil.isNotEmpty(query.getAddress()), EggShopEntity::getAddress, query.getAddress());

        return wrapper;
    }


    @Override
    public EggShopVO get(Long id) {
        EggShopEntity entity = baseMapper.selectById(id);
        EggShopVO vo = EggShopConvert.INSTANCE.convert(entity);
        handleImageFileList(vo);
        return vo;
    }

    @Override
    public List<EggShopVO> getShopList() {
        UserDetail user = SecurityUser.getUser();
        if (user != null && SuperAdminEnum.YES.getValue().equals(user.getSuperAdmin())) {
            return EggShopConvert.INSTANCE.convertList(list());
        }
        return Optional.ofNullable(user)
                .map(UserDetail::getOrgId)
                .map(orgId -> {
                    LambdaQueryWrapper<EggShopEntity> queryWrapper = Wrappers.lambdaQuery();
                    queryWrapper.eq(EggShopEntity::getId, orgId);
                    return EggShopConvert.INSTANCE.convertList(list(queryWrapper));

                })
                .orElse(Collections.emptyList());
    }

    @Override
    public List<String> getShopNameList(List<Long> idList) {
        if (idList.isEmpty()) {
            return null;
        }

        return baseMapper.selectBatchIds(idList).stream().map(EggShopEntity::getName).toList();
    }

    @Override
    public List<Long> getShopIdListByName(String shopName) {
        LambdaQueryWrapper<EggShopEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.select(EggShopEntity::getId, EggShopEntity::getName)
                .like(EggShopEntity::getName, shopName);
        return list(queryWrapper).stream().map(EggShopEntity::getId).toList();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(EggShopVO vo) {
        EggShopEntity entity = EggShopConvert.INSTANCE.convert(vo);
        handleShopImageUrl(vo, entity);
        baseMapper.insert(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(EggShopVO vo) {
        EggShopEntity entity = EggShopConvert.INSTANCE.convert(vo);
        handleShopImageUrl(vo, entity);
        updateById(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(List<Long> idList) {
        removeByIds(idList);

    }

    @Override
    public List<EggShopVO> getShopListFrWx() {
        List<EggShopEntity> list = list();
        List<EggShopVO> eggShopVOS = EggShopConvert.INSTANCE.convertList(list);
        return eggShopVOS;
    }

    @Override
    public PageResult<SysUserVO> pageByShop(SysUserQuery query) {
        PageResult<SysUserVO> userPageS = userService.pageByShop(query);
        List<SysUserVO> userVOS = userPageS.getList();
        userVOS.forEach(vo->{
            EggShopEntity shop = getById(vo.getOrgId());
            if(ObjectUtil.isNull(shop)){
                vo.setShopName("暂无店铺");
            }else{
                vo.setShopName(shop.getName());
            }
        });
        userPageS.setList(userVOS);
        return userPageS;
    }


    private void handleShopImageUrl(EggShopVO vo, EggShopEntity entity) {
        Optional.ofNullable(vo.getImageFileList())
                .map(Collection::stream)
                .ifPresent((fileListStream) -> {
                    String shopImageUrl = fileListStream.map(FileVO::getUrl).collect(Collectors.joining(";"));
                    entity.setShopImageUrl(shopImageUrl);
                });
    }

    private void handleImageFileList(EggShopVO vo) {
        vo.setImageFileList(Collections.emptyList());
        Optional.ofNullable(vo.getShopImageUrl())
                .filter(url -> url.contains(";") || StrUtil.isNotEmpty(url))
                .map(url -> Arrays.stream(url.split(";"))
                        .map((partUrl) -> new FileVO().setUrl(partUrl))
                        .toList()
                )
                .ifPresent(vo::setImageFileList);
    }


}