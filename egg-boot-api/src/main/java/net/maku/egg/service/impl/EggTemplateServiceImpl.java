package net.maku.egg.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import net.maku.egg.convert.EggTemplateConvert;
import net.maku.egg.dao.EggTemplateDao;
import net.maku.egg.entity.EggDeviceEntity;
import net.maku.egg.entity.EggTemplateEntity;
import net.maku.egg.query.EggTemplateQuery;
import net.maku.egg.service.EggDeviceBindService;
import net.maku.egg.service.EggDeviceService;
import net.maku.egg.service.EggTemplateService;
import net.maku.egg.vo.EggTemplateVO;
import net.maku.framework.common.exception.ServerException;
import net.maku.framework.common.utils.PageResult;
import net.maku.framework.mybatis.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 蛋品模板
 *
 * @author niitCDL babamu@126.com
 * <a href="https://maku.net">MAKU</a>
 */
@Service
public class EggTemplateServiceImpl extends BaseServiceImpl<EggTemplateDao, EggTemplateEntity> implements EggTemplateService {

    private EggDeviceService deviceService;

    private EggDeviceBindService deviceBindService;

    @Autowired
    public EggTemplateServiceImpl(@Lazy EggDeviceService deviceService,EggDeviceBindService deviceBindService) {
        this.deviceService = deviceService;
        this.deviceBindService = deviceBindService;
    }

    @Override
    public PageResult<EggTemplateVO> page(EggTemplateQuery query) {
        IPage<EggTemplateEntity> page = baseMapper.selectPage(getPage(query), getWrapper(query));

        return new PageResult<>(EggTemplateConvert.INSTANCE.convertList(page.getRecords()), page.getTotal());
    }


    private LambdaQueryWrapper<EggTemplateEntity> getWrapper(EggTemplateQuery query) {
        LambdaQueryWrapper<EggTemplateEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.like(ObjectUtil.isNotEmpty(query.getName()), EggTemplateEntity::getName, query.getName());

        return wrapper;
    }


    @Override
    public EggTemplateVO get(Long id) {
        EggTemplateEntity entity = baseMapper.selectById(id);
        EggTemplateVO vo = EggTemplateConvert.INSTANCE.convert(entity);

        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(EggTemplateVO vo) {
        checkForDuplicateName(vo.getName());
        EggTemplateEntity entity = EggTemplateConvert.INSTANCE.convert(vo);
        baseMapper.insert(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(EggTemplateVO vo) {
        Long templateId = vo.getId();
        String templateName = vo.getName();
        checkForDuplicateName(templateId, templateName);
        EggTemplateEntity entity = EggTemplateConvert.INSTANCE.convert(vo);
        updateById(entity);

        LambdaQueryWrapper<EggDeviceEntity> deviceQueryWrapper = Wrappers.<EggDeviceEntity>lambdaQuery()
                .select(EggDeviceEntity::getId, EggDeviceEntity::getTemplateId, EggDeviceEntity::getSn)
                .eq(EggDeviceEntity::getTemplateId, templateId);

        List<EggDeviceEntity> deviceList = deviceService.list(deviceQueryWrapper);

        if (CollectionUtil.isNotEmpty(deviceList)) {
            deviceList.stream().map(EggDeviceEntity::getSn).forEach(sn -> {
                deviceBindService.handleTemplateUpdated(sn, 0L, templateId);
            });
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(List<Long> idList) {
        removeByIds(idList);

    }

    private void checkForDuplicateName(Long id, String name) {
        Optional.ofNullable(getById(id))
                .map(EggTemplateEntity::getName)
                .filter(templateName -> !templateName.equals(name))
                .map(templateName -> Wrappers.<EggTemplateEntity>lambdaQuery().eq(EggTemplateEntity::getName, name))
                .map(this::getOne)
                .ifPresent(template -> {
                    throw new ServerException("模板名称相同");
                });
    }

    private void checkForDuplicateName(String name) {
        Optional.ofNullable(name)
                .map(templateName -> Wrappers.<EggTemplateEntity>lambdaQuery().eq(EggTemplateEntity::getName, name))
                .map(this::getOne)
                .ifPresent(template -> {
                    throw new ServerException("模板名称相同");
                });
    }


}