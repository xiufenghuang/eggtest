package net.maku.egg.service.impl;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.AllArgsConstructor;

import net.maku.framework.common.utils.PageResult;
import net.maku.framework.mybatis.service.impl.BaseServiceImpl;
import net.maku.egg.convert.EggDeviceTemplateRelationConvert;
import net.maku.egg.entity.EggDeviceTemplateRelationEntity;
import net.maku.egg.query.EggDeviceTemplateRelationQuery;
import net.maku.egg.vo.EggDeviceTemplateRelationVO;
import net.maku.egg.dao.EggDeviceTemplateRelationDao;
import net.maku.egg.service.EggDeviceTemplateRelationService;
import com.fhs.trans.service.impl.TransService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 设备模版关联表
 *
 * @author xiufenguhuang babamu@126.com
 * <a href="https://maku.net">MAKU</a>
 */
@Service
@AllArgsConstructor
public class EggDeviceTemplateRelationServiceImpl extends BaseServiceImpl<EggDeviceTemplateRelationDao, EggDeviceTemplateRelationEntity> implements EggDeviceTemplateRelationService {
    private final TransService transService;

    @Override
    public PageResult<EggDeviceTemplateRelationVO> page(EggDeviceTemplateRelationQuery query) {
        IPage<EggDeviceTemplateRelationEntity> page = baseMapper.selectPage(getPage(query), getWrapper(query));

        return new PageResult<>(EggDeviceTemplateRelationConvert.INSTANCE.convertList(page.getRecords()), page.getTotal());
    }


    private LambdaQueryWrapper<EggDeviceTemplateRelationEntity> getWrapper(EggDeviceTemplateRelationQuery query){
        LambdaQueryWrapper<EggDeviceTemplateRelationEntity> wrapper = Wrappers.lambdaQuery();

        return wrapper;
    }


    @Override
    public EggDeviceTemplateRelationVO get(Long id) {
        EggDeviceTemplateRelationEntity entity = baseMapper.selectById(id);
        EggDeviceTemplateRelationVO vo = EggDeviceTemplateRelationConvert.INSTANCE.convert(entity);

        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(EggDeviceTemplateRelationVO vo) {
        EggDeviceTemplateRelationEntity entity = EggDeviceTemplateRelationConvert.INSTANCE.convert(vo);

        baseMapper.insert(entity);


    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(EggDeviceTemplateRelationVO vo) {
        EggDeviceTemplateRelationEntity entity = EggDeviceTemplateRelationConvert.INSTANCE.convert(vo);

        updateById(entity);


    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(List<Long> idList) {
        removeByIds(idList);

    }


}