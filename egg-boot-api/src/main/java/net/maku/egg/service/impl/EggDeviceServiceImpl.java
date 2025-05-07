package net.maku.egg.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fhs.trans.service.impl.TransService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.maku.egg.constants.RedisKeys;
import net.maku.egg.convert.EggDeviceConvert;
import net.maku.egg.convert.EggDeviceTemplateRelationConvert;
import net.maku.egg.dao.EggDeviceDao;
import net.maku.egg.dao.EggDeviceTemplateRelationDao;
import net.maku.egg.dao.EggTemplateDao;
import net.maku.egg.dto.DeviceBindBatchDTO;
import net.maku.egg.dto.DeviceWithTemplatesDTO;
import net.maku.egg.entity.*;
import net.maku.egg.enums.DeviceBindRelationEnum;
import net.maku.egg.enums.DeviceBindTypeEnum;
import net.maku.egg.enums.DeviceRunningStatusEnum;
import net.maku.egg.enums.DeviceTypeEnum;
import net.maku.egg.mqtt.sender.DeviceSender;
import net.maku.egg.query.EggDeviceQuery;
import net.maku.egg.service.*;
import net.maku.egg.utils.ExplainPropsUtil;
import net.maku.egg.vo.*;
import net.maku.framework.common.cache.RedisCache;
import net.maku.framework.common.exception.ServerException;
import net.maku.framework.common.utils.ExcelUtils;
import net.maku.framework.common.utils.PageResult;
import net.maku.framework.mybatis.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 设备表
 *
 * @author niitCDL babamu@126.com
 * <a href="https://maku.net">MAKU</a>
 */
@Service
@Slf4j
@AllArgsConstructor
public class EggDeviceServiceImpl extends BaseServiceImpl<EggDeviceDao, EggDeviceEntity> implements EggDeviceService {
    private final TransService transService;

    private final EggShopService shopService;

    private final EggPriceTagInfoService priceTagInfoService;

    private final DeviceSender deviceSender;

    private final EggDeviceBindService deviceBindService;

    private final RedisCache redisCache;

    private final EggTemplateService templateService;


    @Autowired
    private EggDeviceTemplateRelationDao eggDeviceTemplateRelationDao;
    @Autowired
    private EggTemplateDao eggTemplateDao;


//    public List<DeviceWithTemplatesDTO> getAllDevicesWithTemplates() {
//        // 1. 查询所有设备
//        List<EggDeviceEntity> devices = this.list(new QueryWrapper<>());
//
//
//        // 2. 为每个设备查询其绑定的模板信息
//        return devices.stream().map(device -> {
//            // 通过设备ID查询设备模板关联表
//            List<EggDeviceTemplateRelationEntity> deviceTemplates = eggDeviceTemplateRelationDao.selectList(
//                    new QueryWrapper<EggDeviceTemplateRelationEntity>().eq("device_id", device.getId())
//            );
//
//            // 获取所有模板ID
//            List<Integer> templateIds = deviceTemplates.stream()
//                    .map(EggDeviceTemplateRelationEntity::getTemplateId)
//                    .collect(Collectors.toList());
//
//            // 通过模板ID查询模板信息
//            List<EggTemplateEntity> templates = eggTemplateDao.selectList(
//                    new QueryWrapper<EggTemplateEntity>().in("id", templateIds)
//            );
//
//            // 返回设备和模板信息封装对象
//            return new DeviceWithTemplatesDTO(device, templates);
//        }).collect(Collectors.toList());
//    }


    @Override
    public PageResult<EggDeviceVO> page(EggDeviceQuery query) {
        LambdaQueryWrapper<EggDeviceEntity> wrapper = getWrapper(query);
        if (wrapper == null) {
            return new PageResult<>(Collections.emptyList(), 0);
        }
        IPage<EggDeviceEntity> page = baseMapper.selectPage(getPage(query), wrapper);
        List<EggDeviceVO> deviceList = EggDeviceConvert.INSTANCE.convertList(page.getRecords());
        deviceList.stream().forEach(device -> {
            String shopName = Optional.ofNullable(device.getShopId())
                    .map(shopId -> ExplainPropsUtil.getPropsLabel(shopService.getShopNameList(List.of(shopId)), "未知店铺"))
                    .orElseGet(() -> "未绑定店铺");
            device.setShopName(shopName);

            String templateName = Optional.ofNullable(templateService.getById(device.getTemplateId()))
                    .map(EggTemplateEntity::getName)
                    .orElseGet(() -> "");

            device.setTemplateName(templateName);
        });
        return new PageResult<>(deviceList, page.getTotal());
    }
    @Override
    public PageResult<DeviceWithTemplatesDTO> pageWithTemplates(EggDeviceQuery query) {
        // 1. 获取分页查询的条件
        LambdaQueryWrapper<EggDeviceEntity> wrapper = getWrapper(query);
        if (wrapper == null) {
            return new PageResult<>(Collections.emptyList(), 0);
        }

        // 2. 查询设备的分页数据
        IPage<EggDeviceEntity> page = baseMapper.selectPage(getPage(query), wrapper);

        // 3. 将查询到的设备记录转换为DTO
        List<DeviceWithTemplatesDTO> deviceWithTemplatesDTOList = page.getRecords().stream().map(device -> {
            // 4. 通过设备ID查询设备模板关联表
            List<EggDeviceTemplateRelationEntity> deviceTemplates = eggDeviceTemplateRelationDao.selectList(
                    new QueryWrapper<EggDeviceTemplateRelationEntity>().eq("device_id", device.getId())
            );

            // 5. 获取所有模板ID
            List<Long> templateIds = deviceTemplates.stream()
                    .map(EggDeviceTemplateRelationEntity::getTemplateId)
                    .collect(Collectors.toList());

            // 6. 如果模板ID列表不为空，查询模板信息
            List<EggTemplateEntity> templates = Collections.emptyList();
            if (!templateIds.isEmpty()) {
                templates = eggTemplateDao.selectList(
                        new QueryWrapper<EggTemplateEntity>().in("id", templateIds)
                );
            }

            // 7. 获取店铺名称
            String shopName = Optional.ofNullable(device.getShopId())
                    .map(shopId -> ExplainPropsUtil.getPropsLabel(shopService.getShopNameList(List.of(shopId)), "未知店铺"))
                    .orElseGet(() -> "未绑定店铺");

            // 8. 获取模板名称列表
            List<String> templateNames = templates.stream()
                    .map(EggTemplateEntity::getName)
                    .collect(Collectors.toList());

            // 9. 封装成 DeviceWithTemplatesDTO
            return new DeviceWithTemplatesDTO(device, templates, shopName, templateNames);
        }).collect(Collectors.toList());
        // 10. 返回分页结果
        return new PageResult<>(deviceWithTemplatesDTOList, page.getTotal());
    }



    private LambdaQueryWrapper<EggDeviceEntity> getWrapper(EggDeviceQuery query) {
        LambdaQueryWrapper<EggDeviceEntity> wrapper = Wrappers.lambdaQuery();
        if (ObjectUtil.isEmpty(query.getShopId())) {
            List<EggShopVO> shopList = shopService.getShopList();
            wrapper.in(CollectionUtil.isNotEmpty(shopList), EggDeviceEntity::getShopId, shopList.stream().map(EggShopVO::getId).toList());
        } else {
            wrapper.eq(EggDeviceEntity::getShopId, query.getShopId());
        }

        wrapper.isNull(DeviceBindRelationEnum.UNBIND_SHOP.getValue().equals(query.getBindRelation()), EggDeviceEntity::getShopId);
        wrapper.isNull(DeviceBindRelationEnum.UNBIND_TEMPLATE.getValue().equals(query.getBindRelation()), EggDeviceEntity::getTemplateId);
        wrapper.eq(ObjectUtil.isNotEmpty(query.getSn()), EggDeviceEntity::getSn, query.getSn());
        wrapper.like(ObjectUtil.isNotEmpty(query.getName()), EggDeviceEntity::getName, query.getName());
        wrapper.eq(ObjectUtil.isNotEmpty(query.getStatus()), EggDeviceEntity::getStatus, query.getStatus());

        return wrapper;
    }


    @Override
    public EggDeviceVO get(Long id) {
        EggDeviceEntity entity = baseMapper.selectById(id);
        EggDeviceVO vo = EggDeviceConvert.INSTANCE.convert(entity);

        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(EggDeviceVO vo) {
        String sn = vo.getSn();

        Optional.ofNullable(getDeviceInfoBySn(sn))
                .ifPresent(device -> {
                    throw new ServerException("设备sn重复");
                });

        if (DeviceRunningStatusEnum.ONLINE.getValue().equals(vo.getStatus())) {
            setDeviceHeartbeat(sn);
        }

        EggDeviceEntity entity = EggDeviceConvert.INSTANCE.convert(vo);
        if (entity.getStatus() == null) {
            entity.setStatus(0);
        }
        baseMapper.insert(entity);

        deviceSender.sendRefreshPriceTag(sn, vo.getPriceTagId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(EggDeviceVO vo) {
        EggDeviceEntity preModificationDevice = getById(vo.getId());

        Long tagId = vo.getPriceTagId();
        Long templateId = vo.getTemplateId();

        String sn = preModificationDevice.getSn();
        Long previousTagId = preModificationDevice.getPriceTagId();
        Long previousTemplateId = preModificationDevice.getTemplateId();

        EggDeviceEntity entity = EggDeviceConvert.INSTANCE.convert(vo);
        updateById(entity);
        //发送mqtt给订阅者
        deviceBindService.handlePriceTagUpdated(sn, previousTagId, tagId);

        deviceBindService.handleTemplateUpdated(sn, previousTemplateId, templateId);
    }

//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public void updateDeviceTemplate(EggDeviceTemplateVO vo) {
//        EggDeviceEntity preModificationDevice = getById(vo.getId());
//        String sn = preModificationDevice.getSn();
//        LambdaQueryWrapper<EggDeviceEntity> deviceQueryWrapper = Wrappers.lambdaQuery();
//        deviceQueryWrapper.eq(EggDeviceEntity::getSn, sn);
//        EggDeviceEntity entity = getOne(deviceQueryWrapper);
//        Optional.ofNullable(entity)
//
//        // 修改原来的数据
//        EggDeviceEntity entity = EggDeviceConvert.INSTANCE.convert(vo);
//        updateById(entity);
//
//
//        // 1. 删除原有的设备模板关联
//        eggDeviceTemplateRelationDao.delete(
//            new QueryWrapper<EggDeviceTemplateRelationEntity>()
//                .eq("device_id", vo.getId())
//        );
//
//        // 2. 如果新的模板ID集合不为空，创建新的关联
//        if (vo.getTemplateIds() != null && !vo.getTemplateIds().isEmpty()) {
//            // 使用MyBatis-Plus的批量插入
//            List<EggDeviceTemplateRelationEntity> relations = vo.getTemplateIds().stream()
//                .map(templateId -> {
//                    EggDeviceTemplateRelationEntity relation = new EggDeviceTemplateRelationEntity();
//                    relation.setDeviceId(vo.getId().longValue());
//                    relation.setTemplateId(templateId);
//                    return relation;
//                })
//                .collect(Collectors.toList());
//
//                // 批量插入新的关联关系
//            for (EggDeviceTemplateRelationEntity relation : relations) {
//                eggDeviceTemplateRelationDao.insert(relation);
//            }
//        }
//
@Override
@Transactional(rollbackFor = Exception.class)
public void updateDeviceTemplate(EggDeviceTemplateVO vo) {
    // 获取原始设备信息
    EggDeviceEntity preModificationDevice = getById(vo.getId());
    String sn = vo.getSn();  // 使用EggDeviceTemplateVO中的sn属性

    // 查询是否有其他设备的sn与当前sn相同
    LambdaQueryWrapper<EggDeviceEntity> deviceQueryWrapper = Wrappers.lambdaQuery();
    deviceQueryWrapper.eq(EggDeviceEntity::getSn, sn);
    EggDeviceEntity entity = getOne(deviceQueryWrapper);

    // 如果entity不为null且其ID与当前设备ID不同，表示sn重复，不进行更新
    if (entity != null && !entity.getId().equals(preModificationDevice.getId())) {
        // 如果sn重复，返回或抛出异常，避免更新
        throw new IllegalArgumentException("设备SN已存在，无法更新");
    }

    // 修改原来的设备信息
    EggDeviceEntity updatedEntity = EggDeviceConvert.INSTANCE.convert(vo);
    updateById(updatedEntity);

    // 1. 删除原有的设备模板关联
    eggDeviceTemplateRelationDao.delete(
            new QueryWrapper<EggDeviceTemplateRelationEntity>()
                    .eq("device_id", vo.getId())
    );

    // 2. 如果新的模板ID集合不为空，创建新的关联
    if (vo.getTemplateIds() != null && !vo.getTemplateIds().isEmpty()) {
        // 使用MyBatis-Plus的批量插入
        List<EggDeviceTemplateRelationEntity> relations = vo.getTemplateIds().stream()
                .map(templateId -> {
                    EggDeviceTemplateRelationEntity relation = new EggDeviceTemplateRelationEntity();
                    relation.setDeviceId(vo.getId().longValue());
                    relation.setTemplateId(templateId);
                    return relation;
                })
                .collect(Collectors.toList());

        // 批量插入新的关联关系
        for (EggDeviceTemplateRelationEntity relation : relations) {
            eggDeviceTemplateRelationDao.insert(relation);
        }
    }
    try {
        log.info("准备发送MQTT消息，设备SN: {}, 模板IDs: {}", sn, vo.getTemplateIds());
        deviceBindService.handleTemplateUpdated(sn, vo.getTemplateIds());
        log.info("MQTT消息发送成功");
    } catch (Exception e) {
        log.error("MQTT消息发送失败，设备SN: {}, 模板IDs: {}, 异常: {}", sn, vo.getTemplateIds(), e.getMessage(), e);
        throw new ServerException("设备模板更新失败: " + e.getMessage());
    }

}

//        // 3. 发送MQTT消息更新设备模板
//        try {
//            log.info("准备发送MQTT消息，设备SN: {}, 模板IDs: {}", sn, vo.getTemplateIds());
//            deviceBindService.handleTemplateUpdated(sn, vo.getTemplateIds());
//            log.info("MQTT消息发送成功");
//        } catch (Exception e) {
//            log.error("MQTT消息发送失败，设备SN: {}, 模板IDs: {}, 异常: {}", sn, vo.getTemplateIds(), e.getMessage(), e);
//            throw new ServerException("设备模板更新失败: " + e.getMessage());
//        }
//    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveDeviceTemplate(EggDeviceTemplateVO vo) {
        String sn = vo.getSn();

        Optional.ofNullable(getDeviceInfoBySn(sn))
                .ifPresent(device -> {
                    throw new ServerException("设备sn重复");
                });

        if (DeviceRunningStatusEnum.ONLINE.getValue().equals(vo.getStatus())) {
            setDeviceHeartbeat(sn);
        }

        EggDeviceEntity entity = EggDeviceConvert.INSTANCE.convert(vo);
        if (entity.getStatus() == null) {
            entity.setStatus(0);
        }
        baseMapper.insert(entity);

        // 1. 如果模板ID集合不为空，创建新的关联
        if (vo.getTemplateIds() != null && !vo.getTemplateIds().isEmpty()) {
            // 使用MyBatis-Plus的批量插入
            List<EggDeviceTemplateRelationEntity> relations = vo.getTemplateIds().stream()
                .map(templateId -> {
                    EggDeviceTemplateRelationEntity relation = new EggDeviceTemplateRelationEntity();
                    relation.setDeviceId(entity.getId().longValue());
                    relation.setTemplateId(templateId);
                    return relation;
                })
                .collect(Collectors.toList());

            // 批量插入新的关联关系
            for (EggDeviceTemplateRelationEntity relation : relations) {
                eggDeviceTemplateRelationDao.insert(relation);
            }

            // 2. 发送MQTT消息更新设备模板
            try {
                log.info("准备发送MQTT消息，设备SN: {}, 模板IDs: {}", sn, vo.getTemplateIds());
                deviceBindService.handleTemplateUpdated(sn, vo.getTemplateIds());
                log.info("MQTT消息发送成功");
            } catch (Exception e) {
                log.error("MQTT消息发送失败，设备SN: {}, 模板IDs: {}, 异常: {}", sn, vo.getTemplateIds(), e.getMessage(), e);
                throw new ServerException("设备模板更新失败: " + e.getMessage());
            }
        }
    }


    @Override
    public void updateBySn(EggDeviceVO vo) {
        Optional.ofNullable(vo)
                .flatMap((device) -> device.getStatus() == null || device.getSn() == null ? Optional.empty() : Optional.of(device))
                .ifPresent((device) -> {
                    String sn = device.getSn();
                    LambdaUpdateWrapper<EggDeviceEntity> updateWrapper = Wrappers.lambdaUpdate();
                    updateWrapper.set(EggDeviceEntity::getStatus, device.getStatus())
                            .eq(EggDeviceEntity::getSn, sn);
                    update(updateWrapper);
                });
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(List<Long> idList) {
        removeByIds(idList);

    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteDeviceTemplate(List<Long> idList) {
        // 1. 删除设备模板关联关系
        eggDeviceTemplateRelationDao.delete(
            new QueryWrapper<EggDeviceTemplateRelationEntity>()
                .in("device_id", idList)
        );

        // 2. 删除设备
        removeByIds(idList);
    }


    @Override
    public void export() {
        List<EggDeviceExcelVO> excelList = EggDeviceConvert.INSTANCE.convertExcelList(list());
        transService.transBatch(excelList);
        ExcelUtils.excelExport(EggDeviceExcelVO.class, "设备表", null, excelList);
    }

    @Override
    public EggDeviceEntity getDeviceInfoBySn(String sn) {
        LambdaQueryWrapper<EggDeviceEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(EggDeviceEntity::getSn, sn);
        return getOne(queryWrapper);
    }

    @Override
    public List<String> getNameList(List<String> snList) {
        if (CollectionUtil.isEmpty(snList)) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<EggDeviceEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.in(EggDeviceEntity::getSn, snList);
        return list(queryWrapper).stream().map(EggDeviceEntity::getName).toList();
    }

    @Override
    public List<String> getAllSnListByLoginUser() {
        List<Long> shopIdList = shopService.getShopList().stream().map(EggShopVO::getId).toList();
        if (!shopIdList.isEmpty()) {
            LambdaQueryWrapper<EggDeviceEntity> queryWrapper = Wrappers.lambdaQuery();
            queryWrapper.in(EggDeviceEntity::getShopId, shopIdList);
            return list(queryWrapper).stream().map(EggDeviceEntity::getSn).toList();
        }
        return Collections.emptyList();
    }

    @Override
    public List<String> getAllSnListByShopId(Long shopId) {
        LambdaQueryWrapper<EggDeviceEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(EggDeviceEntity::getShopId, shopId);
        return list(queryWrapper).stream().map(EggDeviceEntity::getSn).toList();
    }

    @Override
    public Map<String, String> getNameMap(List<String> snList) {
        if (CollectionUtil.isEmpty(snList)) {
            return Collections.emptyMap();
        }
        LambdaQueryWrapper<EggDeviceEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.in(EggDeviceEntity::getSn, snList);
        List<EggDeviceEntity> deviceList = list(queryWrapper);
        return deviceList.stream().collect(Collectors.toMap(EggDeviceEntity::getSn, EggDeviceEntity::getName));
    }

    @Override
    public List<String> getSnList(String deviceName) {
        LambdaQueryWrapper<EggDeviceEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.like(EggDeviceEntity::getName, deviceName);
        return list(queryWrapper).stream().map(EggDeviceEntity::getSn).toList();
    }

    @Override
    public void heartBeat(EggDeviceVO vo) {
        String sn = vo.getSn();
        LambdaQueryWrapper<EggDeviceEntity> deviceOnlineQueryWrapper = Wrappers.lambdaQuery();
        deviceOnlineQueryWrapper.eq(EggDeviceEntity::getSn, sn);
        deviceOnlineQueryWrapper.eq(EggDeviceEntity::getStatus, DeviceRunningStatusEnum.ONLINE.getValue());
        EggDeviceEntity device = getOne(deviceOnlineQueryWrapper);
        Optional.ofNullable(device)
                .ifPresentOrElse((eggDevice) -> {
                    //若设备存在且是在线状态，只更新redis
                    log.info("设备心跳,sn编号{}", sn);
                    setDeviceHeartbeat(sn);
                }, () -> {
                    LambdaQueryWrapper<EggDeviceEntity> deviceQueryWrapper = Wrappers.lambdaQuery();
                    deviceQueryWrapper.eq(EggDeviceEntity::getSn, sn);
                    EggDeviceEntity entity = getOne(deviceQueryWrapper);
                    Optional.ofNullable(entity)
                            .ifPresentOrElse((eggDevice) -> {
                                //若设备存在，但不在线，则设置在线状态并更新redis

                                log.info("设备上线,sn编号{}", sn);
                                setDeviceHeartbeat(sn);
                                EggDeviceVO deviceVO = new EggDeviceVO();
                                deviceVO.setStatus(DeviceRunningStatusEnum.ONLINE.getValue());
                                deviceVO.setSn(eggDevice.getSn());
                                updateBySn(deviceVO);
                            },()->{
                                //若device不存在则做注册处理
                                setDeviceHeartbeat(sn);
                                vo.setStatus(DeviceRunningStatusEnum.ONLINE.getValue());
                                vo.setType(DeviceTypeEnum.SCALE.getValue());
                                vo.setPreviousWeight(0.0);
                                vo.setCurrentWeight(0.0);
                                log.warn("设备不存在,sn编号:{}作为注册处理,并设置上线", sn);
                                save(vo);
                            });
                });
    }

    private void setDeviceHeartbeat(String sn) {
        String heartBeatKey = RedisKeys.getHeartBeatKey(sn);
        redisCache.set(heartBeatKey, "", RedisCache.HALF_MIN_EXPIRE);
    }

    @Override
    public String getLatestPriceTag(String sn) {
        return Optional.ofNullable(getDeviceInfoBySn(sn))
                .map(EggDeviceEntity::getPriceTagId)
                .flatMap(id -> Optional.ofNullable(priceTagInfoService.getById(id)))
                .map(EggPriceTagInfoEntity::getImageUrl)
                .orElseThrow(() -> new ServerException("设备或其绑定的电子价签信息不存在"));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void bindBatch(DeviceBindBatchDTO bindBatchDTO) {
        Integer bindOptionType = bindBatchDTO.getBindOptionType();
        Long shopId = bindBatchDTO.getShopId();
        Long priceTagId = bindBatchDTO.getPriceTagId();
        Long templateId = bindBatchDTO.getTemplateId();
        boolean isBindShop = false;
        boolean isBindPriceTag = false;
        boolean isBindTemplate = false;
        if ((isBindShop = DeviceBindTypeEnum.SHOP.getValue().equals(bindOptionType) ) && shopId == null) {
            throw new ServerException("绑定的店铺Id不能为空");
        } else if ((isBindPriceTag = DeviceBindTypeEnum.PRICE_TAG.getValue().equals(bindOptionType)) && priceTagId == null) {
            throw new ServerException("绑定的价签Id不能为空");
        } else if ((isBindTemplate = DeviceBindTypeEnum.TEMPLATE.getValue().equals(bindOptionType)) && templateId == null) {
            throw new ServerException("绑定的模板Id不能为空");
        }

        List<Long> deviceIdList = bindBatchDTO.getIdList();
        List<EggDeviceEntity> deviceList = listByIds(deviceIdList);
        LambdaUpdateWrapper<EggDeviceEntity> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper
                .set(isBindShop, EggDeviceEntity::getShopId, shopId)
                .set(isBindPriceTag, EggDeviceEntity::getPriceTagId, priceTagId)
                .set(isBindTemplate, EggDeviceEntity::getTemplateId, templateId)
                .in(EggDeviceEntity::getId, deviceIdList);

        update(updateWrapper);

        if (isBindPriceTag) {
            Optional.ofNullable(priceTagInfoService.getById(priceTagId))
                    .map(EggPriceTagInfoEntity::getImageUrl)
                    .ifPresent(priceTagUrl -> {
                        deviceList.stream().forEach(device -> {
                            deviceBindService.handlePriceTagUpdatedBatch(device.getSn(), device.getPriceTagId(), priceTagId, priceTagUrl);
                        });
                    });
        }

        if (isBindTemplate) {
            deviceList.stream().forEach(device -> {
                deviceBindService.handleTemplateUpdated(device.getSn(), device.getTemplateId(), templateId);
            });
        }
    }

    @Override
    public void bindDevice(EggBindVO bindVO) {
        EggDeviceEntity deviceInfoBySn = getDeviceInfoBySn(bindVO.getSn());
        if(ObjectUtil.isNotNull(deviceInfoBySn.getShopId())){
            throw new ServerException("该设备已绑定");
        }
        EggShopEntity shop = shopService.getById(bindVO.getShopId());
        if(ObjectUtil.isNull(shop)){
            throw new ServerException("店铺不存在");
        }
        deviceInfoBySn.setShopId(bindVO.getShopId());
        deviceInfoBySn.setName(bindVO.getDeviceName());
        deviceInfoBySn.setCreator(bindVO.getCreator());
        updateById(deviceInfoBySn);
    }

    @Override
    public List<EggDeviceVO> getDeviceListByShop(Long shopId) {
        LambdaQueryWrapper<EggDeviceEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(EggDeviceEntity::getShopId,shopId);
        List<EggDeviceEntity> list = list(queryWrapper);
        List<EggDeviceVO> eggDeviceVOS = EggDeviceConvert.INSTANCE.convertList(list);
        return eggDeviceVOS;
    }

    @Override
    public List<EggDeviceVO> getNoBindMiniList() {
        LambdaQueryWrapper<EggDeviceEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(EggDeviceEntity::getType,6);
        queryWrapper.isNull(EggDeviceEntity::getParentDevice);
        List<EggDeviceEntity> eggDeviceEntityList=list(queryWrapper);
        List<EggDeviceVO> eggDeviceVOS = EggDeviceConvert.INSTANCE.convertList(eggDeviceEntityList);
        return  eggDeviceVOS;
    }

    @Override
    public List<EggDeviceVO> getHaveBindMiniListByGatewayId(Long id) {
        LambdaQueryWrapper<EggDeviceEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(EggDeviceEntity::getType,6);
        queryWrapper.eq(EggDeviceEntity::getParentDevice,id);
        List<EggDeviceEntity> eggDeviceEntityList=list(queryWrapper);
        List<EggDeviceVO> eggDeviceVOS = EggDeviceConvert.INSTANCE.convertList(eggDeviceEntityList);
        return  eggDeviceVOS;
    }

    @Override
    public void deleteBindMiniRelation(Long id) {
        // 直接更新找到的记录，将 parentDevice 设置为 null
        this.lambdaUpdate()
                .set(EggDeviceEntity::getParentDevice, null)  // 设置 parentDevice 为 null
                .eq(EggDeviceEntity::getType, 6)  // 条件：type 为 6
                .eq(EggDeviceEntity::getId, id)  // 条件：id 匹配
                .update();  // 执行更新操作
    }


    @Override
    public void saveBindMini(List<Long> ids, Long parentDeviceId) {
        // 遍历每个设备ID
        for (Long id : ids) {
            LambdaQueryWrapper<EggDeviceEntity> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(EggDeviceEntity::getType, 6);
            queryWrapper.eq(EggDeviceEntity::getId, id);  // 查找对应ID的设备

            // 创建更新对象，设置 ParentDevice 为 parentDeviceId
            EggDeviceEntity updateEntity = new EggDeviceEntity();
            updateEntity.setParentDevice(parentDeviceId);

            // 执行更新操作
            update(updateEntity, queryWrapper);
        }
    }

//    @Override
//    public List<EggDeviceVO> getGatewayList() {
//        LambdaQueryWrapper<EggDeviceEntity> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(EggDeviceEntity::getTemplateId,3);
//        List<EggDeviceEntity> eggDeviceEntities=list(queryWrapper);
//        List<EggDeviceVO> eggDeviceVOS = EggDeviceConvert.INSTANCE.convertList(eggDeviceEntities);
//        return  eggDeviceVOS;
//    }

//    public DeviceWithTemplatesDTO getDeviceWithTemplatesById(Integer deviceId) {
//        // 1. 根据设备 ID 查询设备
//        EggDeviceEntity device = this.getById(deviceId); // 通过设备ID直接查询设备
//
//        // 如果设备不存在，返回空或抛出异常
//        if (device == null) {
//            return null; // 或者可以抛出自定义异常
//        }
//
//        // 2. 查询该设备绑定的模板信息
//        List<EggDeviceTemplateRelationEntity> deviceTemplates = eggDeviceTemplateRelationDao.selectList(
//                new QueryWrapper<EggDeviceTemplateRelationEntity>().eq("device_id", deviceId)
//        );
//
//        // 获取所有模板ID
//        List<Integer> templateIds = deviceTemplates.stream()
//                .map(EggDeviceTemplateRelationEntity::getTemplateId)
//                .collect(Collectors.toList());
//
//        // 通过模板ID查询模板信息
//        List<EggTemplateEntity> templates = eggTemplateDao.selectList(
//                new QueryWrapper<EggTemplateEntity>().in("id", templateIds)
//        );
//
//        // 返回设备和模板信息封装对象
//        return new DeviceWithTemplatesDTO(device, templates);
//    }
//    public DeviceWithTemplatesDTO getDeviceWithTemplatesInfoById(Integer deviceId) {
//        // 1. 根据设备 ID 查询设备
//        EggDeviceEntity device = this.getById(deviceId); // 通过设备ID直接查询设备
//
//        // 如果设备不存在，返回空或抛出异常
//        if (device == null) {
//            return null; // 或者可以抛出自定义异常
//        }
//
//        // 2. 查询该设备绑定的模板信息
//        List<EggDeviceTemplateRelationEntity> deviceTemplates = eggDeviceTemplateRelationDao.selectList(
//                new QueryWrapper<EggDeviceTemplateRelationEntity>().eq("device_id", deviceId)
//        );
//
//        // 获取所有模板ID
//        List<Integer> templateIds = deviceTemplates.stream()
//                .map(EggDeviceTemplateRelationEntity::getTemplateId)
//                .collect(Collectors.toList());
//
//        // 通过模板ID查询模板信息
//        List<EggTemplateEntity> templates = eggTemplateDao.selectList(
//                new QueryWrapper<EggTemplateEntity>().in("id", templateIds)
//        );
//
//        // 返回设备和模板信息封装对象
//        return new DeviceWithTemplatesDTO(device, templates);
//    }



}