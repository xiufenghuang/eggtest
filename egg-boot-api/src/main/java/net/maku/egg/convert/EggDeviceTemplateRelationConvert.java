package net.maku.egg.convert;

import net.maku.egg.entity.EggDeviceTemplateRelationEntity;
import net.maku.egg.vo.EggDeviceTemplateRelationVO;
import net.maku.egg.vo.EggDeviceTemplateRelationExcelVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 设备模版关联表
 *
 * @author xiufenguhuang babamu@126.com
 * <a href="https://maku.net">MAKU</a>
 */
@Mapper
public interface EggDeviceTemplateRelationConvert {
    EggDeviceTemplateRelationConvert INSTANCE = Mappers.getMapper(EggDeviceTemplateRelationConvert.class);

    EggDeviceTemplateRelationEntity convert(EggDeviceTemplateRelationVO vo);

    EggDeviceTemplateRelationVO convert(EggDeviceTemplateRelationEntity entity);

    List<EggDeviceTemplateRelationVO> convertList(List<EggDeviceTemplateRelationEntity> list);

    List<EggDeviceTemplateRelationEntity> convertList2(List<EggDeviceTemplateRelationVO> list);

    List<EggDeviceTemplateRelationExcelVO> convertExcelList(List<EggDeviceTemplateRelationEntity> list);

    List<EggDeviceTemplateRelationEntity> convertExcelList2(List<EggDeviceTemplateRelationExcelVO> list);
}