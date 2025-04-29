package net.maku.egg.service;

import net.maku.egg.dto.DateDto;
import net.maku.egg.entity.EggDeviceRecordEntity;
import net.maku.egg.entity.EggDeviceWeightRecordEntity;
import net.maku.egg.query.EggDeviceRecordQuery;
import net.maku.egg.query.EggDeviceWeightRecordQuery;
import net.maku.egg.vo.EggDeviceRecordVO;
import net.maku.egg.vo.EggDeviceWeightRecordVO;
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
public interface EggDeviceWeightRecordService extends BaseService<EggDeviceWeightRecordEntity> {

    PageResult<EggDeviceWeightRecordVO> page(EggDeviceWeightRecordQuery query);

    EggDeviceWeightRecordVO get(Long id);


    void save(EggDeviceWeightRecordVO vo);

    void update(EggDeviceWeightRecordVO vo);

    void delete(List<Long> idList);

}