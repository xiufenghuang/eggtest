package net.maku.egg.service;

import net.maku.framework.common.utils.PageResult;
import net.maku.framework.mybatis.service.BaseService;
import net.maku.egg.vo.EggShopVO;
import net.maku.egg.query.EggShopQuery;
import net.maku.egg.entity.EggShopEntity;
import net.maku.system.query.SysUserQuery;
import net.maku.system.vo.SysUserVO;

import java.util.List;

/**
 * 店铺表
 *
 * @author niitCDL babamu@126.com
 * <a href="https://maku.net">MAKU</a>
 */
public interface EggShopService extends BaseService<EggShopEntity> {

    PageResult<EggShopVO> page(EggShopQuery query);

    EggShopVO get(Long id);

    List<EggShopVO> getShopList();

    List<String> getShopNameList(List<Long> idList);

    List<Long> getShopIdListByName(String shopName);

    void save(EggShopVO vo);

    void update(EggShopVO vo);

    void delete(List<Long> idList);

    List<EggShopVO> getShopListFrWx();

    PageResult<SysUserVO> pageByShop(SysUserQuery query);
}