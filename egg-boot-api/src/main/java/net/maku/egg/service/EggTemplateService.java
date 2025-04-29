package net.maku.egg.service;

import net.maku.framework.common.utils.PageResult;
import net.maku.framework.mybatis.service.BaseService;
import net.maku.egg.vo.EggTemplateVO;
import net.maku.egg.query.EggTemplateQuery;
import net.maku.egg.entity.EggTemplateEntity;
import java.util.List;

/**
 * 蛋品模板
 *
 * @author niitCDL babamu@126.com
 * <a href="https://maku.net">MAKU</a>
 */
public interface EggTemplateService extends BaseService<EggTemplateEntity> {

    PageResult<EggTemplateVO> page(EggTemplateQuery query);

    EggTemplateVO get(Long id);


    void save(EggTemplateVO vo);

    void update(EggTemplateVO vo);

    void delete(List<Long> idList);


}