package net.maku.egg.convert;

import net.maku.egg.entity.EggShopWeightEntity;
import net.maku.egg.vo.EggShopWeightVO;
import net.maku.egg.vo.EggShopWeightExcelVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 店铺设备重量记录
 *
 * @author 阿沐 babamu@126.com
 * <a href="https://maku.net">MAKU</a>
 */
@Mapper
public interface EggShopWeightConvert {
    EggShopWeightConvert INSTANCE = Mappers.getMapper(EggShopWeightConvert.class);

    EggShopWeightEntity convert(EggShopWeightVO vo);

    EggShopWeightVO convert(EggShopWeightEntity entity);

    List<EggShopWeightVO> convertList(List<EggShopWeightEntity> list);

    List<EggShopWeightEntity> convertList2(List<EggShopWeightVO> list);

    List<EggShopWeightExcelVO> convertExcelList(List<EggShopWeightEntity> list);

    List<EggShopWeightEntity> convertExcelList2(List<EggShopWeightExcelVO> list);
}