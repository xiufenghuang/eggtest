package net.maku.egg.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.AllArgsConstructor;
import net.maku.framework.common.utils.PageResult;
import net.maku.framework.mybatis.service.impl.BaseServiceImpl;
import net.maku.egg.convert.EggShopWeightConvert;
import net.maku.egg.entity.EggShopWeightEntity;
import net.maku.egg.query.EggShopWeightQuery;
import net.maku.egg.vo.EggShopWeightVO;
import net.maku.egg.dao.EggShopWeightDao;
import net.maku.egg.service.EggShopWeightService;
import net.maku.egg.service.EggDeviceService;
import net.maku.egg.service.EggShopService;
import com.fhs.trans.service.impl.TransService;
import net.maku.framework.common.utils.ExcelUtils;
import net.maku.egg.vo.EggShopWeightExcelVO;
import net.maku.framework.common.excel.ExcelFinishCallBack;
import org.springframework.web.multipart.MultipartFile;
import cn.hutool.core.util.ObjectUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * 店铺设备重量记录
 *
 * @author 阿沐 babamu@126.com
 * <a href="https://maku.net">MAKU</a>
 */
@Slf4j
@Service
@AllArgsConstructor
public class EggShopWeightServiceImpl extends BaseServiceImpl<EggShopWeightDao, EggShopWeightEntity> implements EggShopWeightService {
    private final TransService transService;
    private final EggDeviceService eggDeviceService;
    private final EggShopService shopService;

    @Override
    public PageResult<EggShopWeightVO> page(EggShopWeightQuery query) {
        IPage<EggShopWeightEntity> page = baseMapper.selectPage(getPage(query), getWrapper(query));

        return new PageResult<>(EggShopWeightConvert.INSTANCE.convertList(page.getRecords()), page.getTotal());
    }

    private LambdaQueryWrapper<EggShopWeightEntity> getWrapper(EggShopWeightQuery query){
        LambdaQueryWrapper<EggShopWeightEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(ObjectUtil.isNotEmpty(query.getShopId()), EggShopWeightEntity::getShopId, query.getShopId());
        wrapper.ge(ObjectUtil.isNotEmpty(query.getStartTime()), EggShopWeightEntity::getCreateTime, query.getStartTime());
        wrapper.le(ObjectUtil.isNotEmpty(query.getEndTime()), EggShopWeightEntity::getCreateTime, query.getEndTime());

        return wrapper;
    }

    @Override
    public EggShopWeightVO get(Long id) {
        EggShopWeightEntity entity = baseMapper.selectById(id);
        EggShopWeightVO vo = EggShopWeightConvert.INSTANCE.convert(entity);

        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(List<Long> idList) {
        removeByIds(idList);
    }

    @Override
    public void export() {
        List<EggShopWeightExcelVO> excelList = EggShopWeightConvert.INSTANCE.convertExcelList(list());
        transService.transBatch(excelList);
        ExcelUtils.excelExport(EggShopWeightExcelVO.class, "店铺设备重量记录", null, excelList);
    }

    /**
     * 获取所有店铺ID列表
     */
    private List<Long> getAllShopIds() {
        return shopService.list().stream()
                .map(shop -> shop.getId())
                .toList();
    }

    /**
     * 记录所有店铺的设备总重量
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void recordAllShopDevicesWeight() {
        // 获取所有店铺ID列表
        List<Long> shopIds = getAllShopIds();

        // 遍历每个店铺，获取设备总重量并保存
        for (Long shopId : shopIds) {
            try {
                // 获取店铺设备总重量
                Double totalWeight = eggDeviceService.getShopDevicesTotalWeight(shopId);
                
                // 创建重量记录实体
                EggShopWeightEntity weightEntity = new EggShopWeightEntity();
                weightEntity.setShopId(shopId);
                weightEntity.setWeight(totalWeight);
                weightEntity.setCreateTime(LocalDateTime.now());
                
                // 保存到数据库
                save(weightEntity);
            } catch (Exception e) {
                log.error("记录店铺[{}]设备重量失败", shopId, e);
            }
        }
    }
}