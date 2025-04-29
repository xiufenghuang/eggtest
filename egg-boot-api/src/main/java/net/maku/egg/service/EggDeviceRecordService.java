package net.maku.egg.service;

import net.maku.egg.dto.DateDto;
import net.maku.egg.entity.EggDeviceRecordEntity;
import net.maku.egg.query.EggDeviceRecordQuery;
import net.maku.egg.vo.EggDeviceRecordVO;
import net.maku.egg.vo.LineChartVO;
import net.maku.framework.common.utils.PageResult;
import net.maku.framework.mybatis.service.BaseService;

import java.time.LocalDate;
import java.util.List;

/**
 * 设备记录表
 *
 * @author 阿沐 babamu@126.com
 * <a href="https://maku.net">MAKU</a>
 */
public interface EggDeviceRecordService extends BaseService<EggDeviceRecordEntity> {

    PageResult<EggDeviceRecordVO> page(EggDeviceRecordQuery query);

    EggDeviceRecordVO get(Long id);


    void save(EggDeviceRecordVO vo);

    void update(EggDeviceRecordVO vo);

    void delete(List<Long> idList);


    void export();

    EggDeviceRecordEntity getRecordBySnAndDate(String sn, LocalDate time);

    void updateSoldWeightById(Double soldWeight,Long id);

    LineChartVO getSoldWeightChart(DateDto dateDto);
}