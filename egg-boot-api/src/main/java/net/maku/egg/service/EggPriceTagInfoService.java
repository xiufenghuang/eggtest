package net.maku.egg.service;

import net.maku.framework.common.utils.PageResult;
import net.maku.framework.mybatis.service.BaseService;
import net.maku.egg.vo.EggPriceTagInfoVO;
import net.maku.egg.query.EggPriceTagInfoQuery;
import net.maku.egg.entity.EggPriceTagInfoEntity;
import java.util.List;

/**
 * 电子价签表
 *
 * @author niitCDL babamu@126.com
 * <a href="https://maku.net">MAKU</a>
 */
public interface EggPriceTagInfoService extends BaseService<EggPriceTagInfoEntity> {

    PageResult<EggPriceTagInfoVO> page(EggPriceTagInfoQuery query);

    EggPriceTagInfoVO get(Long id);


    void save(EggPriceTagInfoVO vo);

    void update(EggPriceTagInfoVO vo);

    void delete(List<Long> idList);


    List<EggPriceTagInfoVO> getPriceTagList();
}