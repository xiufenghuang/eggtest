package net.maku.egg.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fhs.trans.service.impl.TransService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.maku.egg.convert.EggDeviceRecordConvert;
import net.maku.egg.convert.EggDeviceWeightRecordConvert;
import net.maku.egg.dao.EggDeviceRecordDao;
import net.maku.egg.dao.EggDeviceWeightRecordDao;
import net.maku.egg.dto.DateDto;
import net.maku.egg.entity.EggDeviceRecordEntity;
import net.maku.egg.entity.EggDeviceWeightRecordEntity;
import net.maku.egg.enums.DateTypeEnum;
import net.maku.egg.query.EggDeviceRecordQuery;
import net.maku.egg.query.EggDeviceWeightRecordQuery;
import net.maku.egg.service.EggDeviceRecordService;
import net.maku.egg.service.EggDeviceService;
import net.maku.egg.service.EggDeviceWeightRecordService;
import net.maku.egg.vo.EggDeviceRecordExcelVO;
import net.maku.egg.vo.EggDeviceRecordVO;
import net.maku.egg.vo.EggDeviceWeightRecordVO;
import net.maku.egg.vo.LineChartVO;
import net.maku.framework.common.utils.DateUtils;
import net.maku.framework.common.utils.ExcelUtils;
import net.maku.framework.common.utils.PageResult;
import net.maku.framework.mybatis.service.impl.BaseServiceImpl;
import net.maku.system.service.SysOrgService;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 设备记录表
 *
 * @author 阿沐 babamu@126.com
 * <a href="https://maku.net">MAKU</a>
 */
@Service
@Slf4j
@AllArgsConstructor
public class EggDeviceWeightRecordServiceImpl extends BaseServiceImpl<EggDeviceWeightRecordDao, EggDeviceWeightRecordEntity> implements EggDeviceWeightRecordService {

    private final EggDeviceService deviceService;


    @Override
    public PageResult<EggDeviceWeightRecordVO> page(EggDeviceWeightRecordQuery query) {
        LambdaQueryWrapper<EggDeviceWeightRecordEntity> queryWrapper = getWrapper(query);
        if (queryWrapper == null) {
            return new PageResult<>(Collections.emptyList(), 0);
        }
        IPage<EggDeviceWeightRecordEntity> page = baseMapper.selectPage(getPage(query), queryWrapper);
        List<EggDeviceWeightRecordVO> record = EggDeviceWeightRecordConvert.INSTANCE.convertList(page.getRecords());
        List<String> snList = record.stream().map(EggDeviceWeightRecordVO::getSn).distinct().toList();
        Map<String, String> nameMap = deviceService.getNameMap(snList);
        record.stream().forEach(recordVO -> {
            String deviceName = nameMap.getOrDefault(recordVO.getSn(), "设备不存在");
            recordVO.setDeviceName(deviceName);
        });
        return new PageResult<>(record, page.getTotal());
    }


    private LambdaQueryWrapper<EggDeviceWeightRecordEntity> getWrapper(EggDeviceWeightRecordQuery query) {
        LambdaQueryWrapper<EggDeviceWeightRecordEntity> wrapper = Wrappers.lambdaQuery();
        String deviceName = query.getDeviceName();
        if (StrUtil.isNotEmpty(deviceName)) {
            List<String> snList = deviceService.getSnList(deviceName);
            if (CollectionUtil.isEmpty(snList)) {
                return null;
            }
            wrapper.in(EggDeviceWeightRecordEntity::getSn, snList);
        }
        wrapper.eq(ObjectUtil.isNotEmpty(query.getSn()), EggDeviceWeightRecordEntity::getSn, query.getSn());
        wrapper.between(ObjectUtil.isNotEmpty(query.getCreateTime()), EggDeviceWeightRecordEntity::getCreateTime, ArrayUtils.isNotEmpty(query.getCreateTime()) ? query.getCreateTime()[0] : null, ArrayUtils.isNotEmpty(query.getCreateTime()) ? DateUtils.setEndTimeOfDay(query.getCreateTime()[1]) : null);
        return wrapper;
    }


    @Override
    public EggDeviceWeightRecordVO get(Long id) {
        EggDeviceWeightRecordEntity entity = baseMapper.selectById(id);
        EggDeviceWeightRecordVO vo = EggDeviceWeightRecordConvert.INSTANCE.convert(entity);

        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(EggDeviceWeightRecordVO vo) {
        EggDeviceWeightRecordEntity entity = EggDeviceWeightRecordConvert.INSTANCE.convert(vo);

        baseMapper.insert(entity);


    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(EggDeviceWeightRecordVO vo) {
        EggDeviceWeightRecordEntity entity = EggDeviceWeightRecordConvert.INSTANCE.convert(vo);

        updateById(entity);


    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(List<Long> idList) {
        removeByIds(idList);

    }

}