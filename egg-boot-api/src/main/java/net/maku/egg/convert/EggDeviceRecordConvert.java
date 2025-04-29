package net.maku.egg.convert;

import net.maku.egg.entity.EggDeviceRecordEntity;
import net.maku.egg.vo.EggDeviceRecordVO;
import net.maku.egg.vo.EggDeviceRecordExcelVO;
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
public interface EggDeviceRecordConvert {
    EggDeviceRecordConvert INSTANCE = Mappers.getMapper(EggDeviceRecordConvert.class);

    EggDeviceRecordEntity convert(EggDeviceRecordVO vo);

    EggDeviceRecordVO convert(EggDeviceRecordEntity entity);

    List<EggDeviceRecordVO> convertList(List<EggDeviceRecordEntity> list);

    List<EggDeviceRecordEntity> convertList2(List<EggDeviceRecordVO> list);

    List<EggDeviceRecordExcelVO> convertExcelList(List<EggDeviceRecordEntity> list);

    List<EggDeviceRecordEntity> convertExcelList2(List<EggDeviceRecordExcelVO> list);
}