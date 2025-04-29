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
    //page.getRecords() 获取查询结果中的数据记录，即查询到的 EggTemplateEntity 列表。
    //EggTemplateConvert.INSTANCE.convertList(page.getRecords()) 使用 MapStruct 的映射器实例将 EggTemplateEntity 列表转换为 EggTemplateVO 列表。这里假设 convertList 是在 EggTemplateConvert 接口中定义的一个方法，用于批量转换。
    //这种转换的好处是，EggTemplateVO 可能是一个 View Object（视图对象），与数据库实体对象 EggTemplateEntity 结构不同，因此通过 MapStruct 可以方便地将数据库查询结果转换为适合返回给前端的对象。
    EggTemplateConvert INSTANCE = Mappers.getMapper(EggTemplateConvert.class);

    EggTemplateEntity convert(EggTemplateVO vo);

    EggTemplateVO convert(EggTemplateEntity entity);

    List<EggTemplateVO> convertList(List<EggTemplateEntity> list);

    List<EggTemplateEntity> convertList2(List<EggTemplateVO> list);

}