package net.maku.egg.dao;

import net.maku.framework.mybatis.dao.BaseDao;
import net.maku.egg.entity.EggDevicePriceTagRelationEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 设备电子价签关系表
 *
 * @author 阿沐 babamu@126.com
 * <a href="https://maku.net">MAKU</a>
 */
@Mapper
public interface EggDevicePriceTagRelationDao extends BaseDao<EggDevicePriceTagRelationEntity> {

}