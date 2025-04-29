package net.maku.egg.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.AllArgsConstructor;
import net.maku.framework.common.utils.PageResult;
import net.maku.framework.mybatis.service.impl.BaseServiceImpl;
import net.maku.egg.convert.EggDevicePriceTagRelationConvert;
import net.maku.egg.entity.EggDevicePriceTagRelationEntity;
import net.maku.egg.query.EggDevicePriceTagRelationQuery;
import net.maku.egg.vo.EggDevicePriceTagRelationVO;
import net.maku.egg.dao.EggDevicePriceTagRelationDao;
import net.maku.egg.service.EggDevicePriceTagRelationService;
import com.fhs.trans.service.impl.TransService;
import net.maku.framework.common.utils.ExcelUtils;
import net.maku.egg.vo.EggDevicePriceTagRelationExcelVO;
import net.maku.framework.common.excel.ExcelFinishCallBack;
import org.springframework.web.multipart.MultipartFile;
import cn.hutool.core.util.ObjectUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 设备电子价签关系表
 *
 * @author 阿沐 babamu@126.com
 * <a href="https://maku.net">MAKU</a>
 */
@Service
@AllArgsConstructor
public class EggDevicePriceTagRelationServiceImpl extends BaseServiceImpl<EggDevicePriceTagRelationDao, EggDevicePriceTagRelationEntity> implements EggDevicePriceTagRelationService {
    private final TransService transService;

    @Override
    public PageResult<EggDevicePriceTagRelationVO> page(EggDevicePriceTagRelationQuery query) {
        IPage<EggDevicePriceTagRelationEntity> page = baseMapper.selectPage(getPage(query), getWrapper(query));

        return new PageResult<>(EggDevicePriceTagRelationConvert.INSTANCE.convertList(page.getRecords()), page.getTotal());
    }


    private LambdaQueryWrapper<EggDevicePriceTagRelationEntity> getWrapper(EggDevicePriceTagRelationQuery query){
        LambdaQueryWrapper<EggDevicePriceTagRelationEntity> wrapper = Wrappers.lambdaQuery();

        return wrapper;
    }


    @Override
    public EggDevicePriceTagRelationVO get(Long id) {
        EggDevicePriceTagRelationEntity entity = baseMapper.selectById(id);
        EggDevicePriceTagRelationVO vo = EggDevicePriceTagRelationConvert.INSTANCE.convert(entity);

        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(EggDevicePriceTagRelationVO vo) {
        EggDevicePriceTagRelationEntity entity = EggDevicePriceTagRelationConvert.INSTANCE.convert(vo);

        baseMapper.insert(entity);


    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(EggDevicePriceTagRelationVO vo) {
        EggDevicePriceTagRelationEntity entity = EggDevicePriceTagRelationConvert.INSTANCE.convert(vo);

        updateById(entity);


    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(List<Long> idList) {
        removeByIds(idList);

    }


    @Override
    public void export() {
    List<EggDevicePriceTagRelationExcelVO> excelList = EggDevicePriceTagRelationConvert.INSTANCE.convertExcelList(list());
        transService.transBatch(excelList);
        ExcelUtils.excelExport(EggDevicePriceTagRelationExcelVO.class, "设备电子价签关系表", null, excelList);
    }

}