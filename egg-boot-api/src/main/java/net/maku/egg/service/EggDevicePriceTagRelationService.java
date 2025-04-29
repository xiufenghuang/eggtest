package net.maku.egg.service;

import net.maku.framework.common.utils.PageResult;
import net.maku.framework.mybatis.service.BaseService;
import net.maku.egg.vo.EggDevicePriceTagRelationVO;
import net.maku.egg.query.EggDevicePriceTagRelationQuery;
import net.maku.egg.entity.EggDevicePriceTagRelationEntity;
import java.util.List;

/**
 * 设备电子价签关系表
 *
 * @author 阿沐 babamu@126.com
 * <a href="https://maku.net">MAKU</a>
 */
public interface EggDevicePriceTagRelationService extends BaseService<EggDevicePriceTagRelationEntity> {

    PageResult<EggDevicePriceTagRelationVO> page(EggDevicePriceTagRelationQuery query);

    EggDevicePriceTagRelationVO get(Long id);


    void save(EggDevicePriceTagRelationVO vo);

    void update(EggDevicePriceTagRelationVO vo);

    void delete(List<Long> idList);


    void export();
}