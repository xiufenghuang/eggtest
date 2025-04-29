package net.maku.egg.convert;

import net.maku.egg.entity.EggShopEntity;
import net.maku.egg.vo.EggShopVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 店铺表
 *
 * @author niitCDL babamu@126.com
 * <a href="https://maku.net">MAKU</a>
 */
@Mapper
public interface EggShopConvert {
    EggShopConvert INSTANCE = Mappers.getMapper(EggShopConvert.class);

    EggShopEntity convert(EggShopVO vo);

    EggShopVO convert(EggShopEntity entity);

    List<EggShopVO> convertList(List<EggShopEntity> list);

    List<EggShopEntity> convertList2(List<EggShopVO> list);

}