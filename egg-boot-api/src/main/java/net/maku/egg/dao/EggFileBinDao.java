package net.maku.egg.dao;

import net.maku.egg.entity.EggFileBinEntity;
import net.maku.framework.mybatis.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

/**
 * bin文件版本控制
 *
 * @author 孙超 babamu@126.com
 * <a href="https://maku.net">MAKU</a>
 */
@Mapper
public interface EggFileBinDao extends BaseDao<EggFileBinEntity> {

}