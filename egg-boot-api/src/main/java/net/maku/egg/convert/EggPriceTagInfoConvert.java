package net.maku.egg.convert;

import net.maku.egg.entity.EggPriceTagInfoEntity;
import net.maku.egg.vo.EggPriceTagInfoVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 电子价签表
 *
 * @author niitCDL babamu@126.com
 * <a href="https://maku.net">MAKU</a>
 */
@Mapper
public interface EggPriceTagInfoConvert {
    EggPriceTagInfoConvert INSTANCE = Mappers.getMapper(EggPriceTagInfoConvert.class);

    EggPriceTagInfoEntity convert(EggPriceTagInfoVO vo);

    EggPriceTagInfoVO convert(EggPriceTagInfoEntity entity);

    List<EggPriceTagInfoVO> convertList(List<EggPriceTagInfoEntity> list);

    List<EggPriceTagInfoEntity> convertList2(List<EggPriceTagInfoVO> list);

}