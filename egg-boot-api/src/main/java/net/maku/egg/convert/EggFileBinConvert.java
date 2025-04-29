package net.maku.egg.convert;

import net.maku.egg.entity.EggFileBinEntity;
import net.maku.egg.vo.EggFileBinVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * bin文件版本控制
 *
 * @author 孙超 babamu@126.com
 * <a href="https://maku.net">MAKU</a>
 */
@Mapper
public interface EggFileBinConvert {
    EggFileBinConvert INSTANCE = Mappers.getMapper(EggFileBinConvert.class);

    EggFileBinEntity convert(EggFileBinVO vo);

    EggFileBinVO convert(EggFileBinEntity entity);

    List<EggFileBinVO> convertList(List<EggFileBinEntity> list);

    List<EggFileBinEntity> convertList2(List<EggFileBinVO> list);

}