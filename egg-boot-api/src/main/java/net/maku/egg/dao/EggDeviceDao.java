package net.maku.egg.dao;

import net.maku.framework.mybatis.dao.BaseDao;
import net.maku.egg.entity.EggDeviceEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 设备表
 *
 * @author niitCDL babamu@126.com
 * <a href="https://maku.net">MAKU</a>
 */
@Mapper
public interface EggDeviceDao extends BaseDao<EggDeviceEntity> {

    EggDeviceEntity getByID();
}