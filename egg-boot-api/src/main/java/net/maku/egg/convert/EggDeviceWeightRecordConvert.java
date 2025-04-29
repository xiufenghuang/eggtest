package net.maku.egg.convert;

import net.maku.egg.entity.EggDeviceRecordEntity;
import net.maku.egg.entity.EggDeviceWeightRecordEntity;
import net.maku.egg.vo.EggDeviceRecordExcelVO;
import net.maku.egg.vo.EggDeviceRecordVO;
import net.maku.egg.vo.EggDeviceWeightRecordVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 设备记录表
 *
 * @author 阿沐 babamu@126.com
 * <a href="https://maku.net">MAKU</a>
 */
@Mapper
public interface EggDeviceWeightRecordConvert {
    EggDeviceWeightRecordConvert INSTANCE = Mappers.getMapper(EggDeviceWeightRecordConvert.class);

    EggDeviceWeightRecordEntity convert(EggDeviceWeightRecordVO vo);

    EggDeviceWeightRecordVO convert(EggDeviceWeightRecordEntity entity);

    List<EggDeviceWeightRecordVO> convertList(List<EggDeviceWeightRecordEntity> list);

    List<EggDeviceWeightRecordEntity> convertList2(List<EggDeviceWeightRecordVO> list);

}