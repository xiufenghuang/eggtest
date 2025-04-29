package net.maku.egg.service;

import net.maku.framework.common.utils.PageResult;
import net.maku.framework.mybatis.service.BaseService;
import net.maku.egg.vo.EggDeviceTemplateRelationVO;
import net.maku.egg.query.EggDeviceTemplateRelationQuery;
import net.maku.egg.entity.EggDeviceTemplateRelationEntity;
import java.util.List;

/**
 * 设备模版关联表
 *
 * @author xiufenguhuang babamu@126.com
 * <a href="https://maku.net">MAKU</a>
 */
public interface EggDeviceTemplateRelationService extends BaseService<EggDeviceTemplateRelationEntity> {

    PageResult<EggDeviceTemplateRelationVO> page(EggDeviceTemplateRelationQuery query);

    EggDeviceTemplateRelationVO get(Long id);


    void save(EggDeviceTemplateRelationVO vo);

    void update(EggDeviceTemplateRelationVO vo);

    void delete(List<Long> idList);


//    void export();
}