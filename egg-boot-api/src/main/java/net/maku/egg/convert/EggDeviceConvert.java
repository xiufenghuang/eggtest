package net.maku.egg.convert;

import net.maku.egg.entity.EggDeviceEntity;
import net.maku.egg.vo.EggDeviceTemplateVO;
import net.maku.egg.vo.EggDeviceVO;
import net.maku.egg.vo.EggDeviceExcelVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 设备表
 *
 * @author niitCDL babamu@126.com
 * <a href="https://maku.net">MAKU</a>
 */
@Mapper
public interface EggDeviceConvert {
    EggDeviceConvert INSTANCE = Mappers.getMapper(EggDeviceConvert.class);

    EggDeviceEntity convert(EggDeviceVO vo);

    EggDeviceVO convert(EggDeviceEntity entity);



    List<EggDeviceVO> convertList(List<EggDeviceEntity> list);

    List<EggDeviceEntity> convertList2(List<EggDeviceVO> list);

    List<EggDeviceExcelVO> convertExcelList(List<EggDeviceEntity> list);

    List<EggDeviceEntity> convertExcelList2(List<EggDeviceExcelVO> list);

    EggDeviceEntity convert(EggDeviceTemplateVO vo);
}