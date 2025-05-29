package net.maku.egg.service;

import net.maku.framework.common.utils.PageResult;
import net.maku.framework.mybatis.service.BaseService;
import net.maku.egg.vo.EggShopWeightVO;
import net.maku.egg.query.EggShopWeightQuery;
import net.maku.egg.entity.EggShopWeightEntity;
import java.util.List;

/**
 * 店铺设备重量记录
 *
 * @author 阿沐 babamu@126.com
 * <a href="https://maku.net">MAKU</a>
 */
public interface EggShopWeightService extends BaseService<EggShopWeightEntity> {

    PageResult<EggShopWeightVO> page(EggShopWeightQuery query);

    EggShopWeightVO get(Long id);




    void delete(List<Long> idList);


    void export();

    /**
     * 记录所有店铺的设备总重量
     */
    void recordAllShopDevicesWeight();
}