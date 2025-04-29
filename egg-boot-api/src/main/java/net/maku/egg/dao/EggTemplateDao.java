package net.maku.egg.dao;

import net.maku.framework.mybatis.dao.BaseDao;
import net.maku.egg.entity.EggTemplateEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 蛋品模板
 *
 * @author niitCDL babamu@126.com
 * <a href="https://maku.net">MAKU</a>
 */
@Mapper
public interface EggTemplateDao extends BaseDao<EggTemplateEntity> {

}