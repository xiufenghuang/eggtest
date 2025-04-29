package net.maku.egg.convert;

import net.maku.egg.entity.EggDevicePriceTagRelationEntity;
import net.maku.egg.vo.EggDevicePriceTagRelationVO;
import net.maku.egg.vo.EggDevicePriceTagRelationExcelVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 设备电子价签关系表
 *
 * @author 阿沐 babamu@126.com
 * <a href="https://maku.net">MAKU</a>
 */
@Mapper
public interface EggDevicePriceTagRelationConvert {
    EggDevicePriceTagRelationConvert INSTANCE = Mappers.getMapper(EggDevicePriceTagRelationConvert.class);

    EggDevicePriceTagRelationEntity convert(EggDevicePriceTagRelationVO vo);

    EggDevicePriceTagRelationVO convert(EggDevicePriceTagRelationEntity entity);

    List<EggDevicePriceTagRelationVO> convertList(List<EggDevicePriceTagRelationEntity> list);

    List<EggDevicePriceTagRelationEntity> convertList2(List<EggDevicePriceTagRelationVO> list);

    List<EggDevicePriceTagRelationExcelVO> convertExcelList(List<EggDevicePriceTagRelationEntity> list);

    List<EggDevicePriceTagRelationEntity> convertExcelList2(List<EggDevicePriceTagRelationExcelVO> list);
}