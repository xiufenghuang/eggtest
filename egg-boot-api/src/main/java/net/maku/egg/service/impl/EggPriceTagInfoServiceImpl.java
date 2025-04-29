package net.maku.egg.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.AllArgsConstructor;
import net.maku.egg.convert.EggShopConvert;
import net.maku.egg.entity.EggDeviceEntity;
import net.maku.egg.entity.EggShopEntity;
import net.maku.egg.vo.EggShopVO;
import net.maku.framework.common.utils.PageResult;
import net.maku.framework.mybatis.service.impl.BaseServiceImpl;
import net.maku.egg.convert.EggPriceTagInfoConvert;
import net.maku.egg.entity.EggPriceTagInfoEntity;
import net.maku.egg.query.EggPriceTagInfoQuery;
import net.maku.egg.vo.EggPriceTagInfoVO;
import net.maku.egg.dao.EggPriceTagInfoDao;
import net.maku.egg.service.EggPriceTagInfoService;
import cn.hutool.core.util.ObjectUtil;
import net.maku.system.vo.FileVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 电子价签表
 *
 * @author niitCDL babamu@126.com
 * <a href="https://maku.net">MAKU</a>
 */
@Service
@AllArgsConstructor
public class EggPriceTagInfoServiceImpl extends BaseServiceImpl<EggPriceTagInfoDao, EggPriceTagInfoEntity> implements EggPriceTagInfoService {

    @Override
    public PageResult<EggPriceTagInfoVO> page(EggPriceTagInfoQuery query) {
        IPage<EggPriceTagInfoEntity> page = baseMapper.selectPage(getPage(query), getWrapper(query));
        List<EggPriceTagInfoVO> tagPriceList = EggPriceTagInfoConvert.INSTANCE.convertList(page.getRecords());
        tagPriceList.stream().forEach(tagPrice -> handleImageFileList(tagPrice));
        return new PageResult<>(tagPriceList, page.getTotal());
    }


    private LambdaQueryWrapper<EggPriceTagInfoEntity> getWrapper(EggPriceTagInfoQuery query) {
        LambdaQueryWrapper<EggPriceTagInfoEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.like(ObjectUtil.isNotEmpty(query.getName()), EggPriceTagInfoEntity::getName, query.getName());

        return wrapper;
    }


    @Override
    public EggPriceTagInfoVO get(Long id) {
        EggPriceTagInfoEntity entity = baseMapper.selectById(id);
        EggPriceTagInfoVO vo = EggPriceTagInfoConvert.INSTANCE.convert(entity);
        handleImageFileList(vo);
        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(EggPriceTagInfoVO vo) {
        EggPriceTagInfoEntity entity = EggPriceTagInfoConvert.INSTANCE.convert(vo);
        handlePriceTagImageUrl(vo, entity);
        baseMapper.insert(entity);


    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(EggPriceTagInfoVO vo) {
        EggPriceTagInfoEntity entity = EggPriceTagInfoConvert.INSTANCE.convert(vo);
        handlePriceTagImageUrl(vo, entity);
        updateById(entity);


    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(List<Long> idList) {
        removeByIds(idList);

    }

    @Override
    public List<EggPriceTagInfoVO> getPriceTagList() {
        List<EggPriceTagInfoEntity> list = list();
        return EggPriceTagInfoConvert.INSTANCE.convertList(list);
    }

    private void handlePriceTagImageUrl(EggPriceTagInfoVO vo, EggPriceTagInfoEntity entity) {
        Optional.ofNullable(vo.getImageFileList())
                .map(Collection::stream)
                .ifPresent((fileListStream) -> {
                    String priceTagImageUrl = fileListStream.map(FileVO::getUrl).collect(Collectors.joining(";"));
                    entity.setImageUrl(priceTagImageUrl);
                });
    }

    private void handleImageFileList(EggPriceTagInfoVO vo) {
        vo.setImageFileList(Collections.emptyList());
        Optional.ofNullable(vo.getImageUrl())
                .filter(url -> url.contains(";") || StrUtil.isNotEmpty(url))
                .map(url -> Arrays.stream(url.split(";"))
                        .map((partUrl) -> new FileVO().setUrl(partUrl))
                        .toList()
                )
                .ifPresent(vo::setImageFileList);
    }
}