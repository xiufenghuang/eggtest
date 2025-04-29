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
import net.maku.egg.dao.EggDeviceRecordDao;
import net.maku.egg.dto.DateDto;
import net.maku.egg.entity.EggDeviceRecordEntity;
import net.maku.egg.enums.DateTypeEnum;
import net.maku.egg.query.EggDeviceRecordQuery;
import net.maku.egg.service.EggDeviceRecordService;
import net.maku.egg.service.EggDeviceService;
import net.maku.egg.vo.EggDeviceRecordExcelVO;
import net.maku.egg.vo.EggDeviceRecordVO;
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
public class EggDeviceRecordServiceImpl extends BaseServiceImpl<EggDeviceRecordDao, EggDeviceRecordEntity> implements EggDeviceRecordService {
    private final TransService transService;

    private final EggDeviceService deviceService;

    private final SysOrgService orgService;

    @Override
    public PageResult<EggDeviceRecordVO> page(EggDeviceRecordQuery query) {
        LambdaQueryWrapper<EggDeviceRecordEntity> queryWrapper = getWrapper(query);
        if (queryWrapper == null) {
            return new PageResult<>(Collections.emptyList(), 0);
        }
        IPage<EggDeviceRecordEntity> page = baseMapper.selectPage(getPage(query), queryWrapper);
        List<EggDeviceRecordVO> record = EggDeviceRecordConvert.INSTANCE.convertList(page.getRecords());
        List<String> snList = record.stream().map(EggDeviceRecordVO::getSn).distinct().toList();
        Map<String, String> nameMap = deviceService.getNameMap(snList);
        record.stream().forEach(recordVO -> {
            String deviceName = nameMap.getOrDefault(recordVO.getSn(), "设备不存在");
            recordVO.setDeviceName(deviceName);
        });
        return new PageResult<>(record, page.getTotal());
    }


    private LambdaQueryWrapper<EggDeviceRecordEntity> getWrapper(EggDeviceRecordQuery query) {
        LambdaQueryWrapper<EggDeviceRecordEntity> wrapper = Wrappers.lambdaQuery();
        String deviceName = query.getDeviceName();
        if (StrUtil.isNotEmpty(deviceName)) {
            List<String> snList = deviceService.getSnList(deviceName);
            if (CollectionUtil.isEmpty(snList)) {
                return null;
            }
            wrapper.in(EggDeviceRecordEntity::getSn, snList);
        }
        wrapper.eq(ObjectUtil.isNotEmpty(query.getSn()), EggDeviceRecordEntity::getSn, query.getSn());
        wrapper.between(ObjectUtil.isNotEmpty(query.getCreateTime()), EggDeviceRecordEntity::getCreateTime, ArrayUtils.isNotEmpty(query.getCreateTime()) ? query.getCreateTime()[0] : null, ArrayUtils.isNotEmpty(query.getCreateTime()) ? DateUtils.setEndTimeOfDay(query.getCreateTime()[1]) : null);
        return wrapper;
    }


    @Override
    public EggDeviceRecordVO get(Long id) {
        EggDeviceRecordEntity entity = baseMapper.selectById(id);
        EggDeviceRecordVO vo = EggDeviceRecordConvert.INSTANCE.convert(entity);

        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(EggDeviceRecordVO vo) {
        EggDeviceRecordEntity entity = EggDeviceRecordConvert.INSTANCE.convert(vo);

        baseMapper.insert(entity);


    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(EggDeviceRecordVO vo) {
        EggDeviceRecordEntity entity = EggDeviceRecordConvert.INSTANCE.convert(vo);

        updateById(entity);


    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(List<Long> idList) {
        removeByIds(idList);

    }


    @Override
    public void export() {
        List<EggDeviceRecordExcelVO> excelList = EggDeviceRecordConvert.INSTANCE.convertExcelList(list());
        transService.transBatch(excelList);
        ExcelUtils.excelExport(EggDeviceRecordExcelVO.class, "设备记录表", null, excelList);
    }

    @Override
    public EggDeviceRecordEntity getRecordBySnAndDate(String sn, LocalDate time) {
        LambdaQueryWrapper<EggDeviceRecordEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(EggDeviceRecordEntity::getSn, sn)
                .between(EggDeviceRecordEntity::getCreateTime,
                        LocalDateTime.of(time, LocalTime.MIN),
                        LocalDateTime.of(time, LocalTime.MAX));
        return getOne(queryWrapper);
    }

    @Override
    public void updateSoldWeightById(Double soldWeight, Long id) {
        LambdaUpdateWrapper<EggDeviceRecordEntity> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.set(EggDeviceRecordEntity::getSoldWeight, soldWeight)
                .set(EggDeviceRecordEntity::getUpdateTime, LocalDateTime.now())
                .eq(EggDeviceRecordEntity::getId, id);
        update(updateWrapper);
    }

    @Override
    public LineChartVO getSoldWeightChart(DateDto dateDto) {
        Integer dateTypeCode = dateDto.getDateTypeCode();
        Year year = dateDto.getYear();
        YearMonth yearMonth = dateDto.getYearMonth();
        Long shopId = dateDto.getShopId();
        Boolean viewAllData = dateDto.getViewAllData();
        LineChartVO lineChart = new LineChartVO();
        List<String> snList = viewAllData ? deviceService.getAllSnListByLoginUser() : deviceService.getAllSnListByShopId(shopId);
        if (CollectionUtil.isEmpty(snList)) {
            return null;
        }
        LambdaQueryWrapper<EggDeviceRecordEntity> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.in(EggDeviceRecordEntity::getSn, snList);
        if (DateTypeEnum.DAY.getCode().equals(dateTypeCode)) {

        } else if (DateTypeEnum.MONTH.getCode().equals(dateTypeCode) && yearMonth != null) {
            int dayOfMonth = yearMonth.atEndOfMonth().getDayOfMonth();
            initChartData(lineChart, dayOfMonth);

            LocalDateTime[] times = DateUtils.getMonthStartAndEndDateTime(yearMonth);
            LocalDateTime startDateTime = times[0];
            LocalDateTime endDateTime = times[1];
            queryWrapper.between(EggDeviceRecordEntity::getCreateTime, startDateTime, endDateTime);
            List<EggDeviceRecordEntity> deviceRecord = list(queryWrapper);
            List<Double> y = lineChart.getY();
            deviceRecord.stream().forEach(record -> {
                Double soldWeight = record.getSoldWeight();
                int index = record.getCreateTime().getDayOfMonth() - 1;
                Double sumSoldWeight = y.get(index);
                y.set(index, sumSoldWeight + soldWeight);
            });
        } else if (DateTypeEnum.YEAR.getCode().equals(dateTypeCode) && year != null) {
            //一年有十二个月
            initChartData(lineChart, 12);
            LocalDateTime[] times = DateUtils.getYearStartAndEndDateTime(year);
            LocalDateTime startDateTime = times[0];
            LocalDateTime endDateTime = times[1];
            queryWrapper.between(EggDeviceRecordEntity::getCreateTime, startDateTime, endDateTime);
            List<EggDeviceRecordEntity> deviceRecord = list(queryWrapper);
            List<Double> y = lineChart.getY();
            deviceRecord.stream().forEach(record -> {
                Double soldWeight = record.getSoldWeight();
                int index = record.getCreateTime().getMonthValue() - 1;
                Double sumSoldWeight = y.get(index);
                y.set(index, sumSoldWeight + soldWeight);
            });
        }
        return lineChart;
    }

    private void initChartData(LineChartVO target, int generationLength) {
        List<Integer> x = new ArrayList<>();
        List<Double> y = new ArrayList<>();
        for (int i = 1; i <= generationLength; i++) {
            x.add(i);
            y.add(0.0);
        }
        target.setX(x);
        target.setY(y);
    }

}