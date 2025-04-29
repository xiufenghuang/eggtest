package net.maku.egg.convert;

import net.maku.egg.entity.EggTemplateEntity;
import net.maku.egg.vo.EggTemplateVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 蛋品模板
 *
 * @author niitCDL babamu@126.com
 * <a href="https://maku.net">MAKU</a>
 */
@Mapper
public interface EggTemplateConvert {
    EggTemplateConvert INSTANCE = Mappers.getMapper(EggTemplateConvert.class);

    EggTemplateEntity convert(EggTemplateVO vo);

    EggTemplateVO convert(EggTemplateEntity entity);

    List<EggTemplateVO> convertList(List<EggTemplateEntity> list);

    List<EggTemplateEntity> convertList2(List<EggTemplateVO> list);

}