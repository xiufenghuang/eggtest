package net.maku.egg.dao;

import net.maku.framework.mybatis.dao.BaseDao;
import net.maku.egg.entity.EggShopEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 店铺表
 *
 * @author niitCDL babamu@126.com
 * <a href="https://maku.net">MAKU</a>
 */
@Mapper
public interface EggShopDao extends BaseDao<EggShopEntity> {

}