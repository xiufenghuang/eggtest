package net.maku.egg.service;

import net.maku.egg.dto.DeviceBindBatchDTO;
import net.maku.egg.dto.DeviceWithTemplatesDTO;
import net.maku.egg.entity.EggDeviceEntity;
import net.maku.egg.query.EggDeviceQuery;
import net.maku.egg.vo.EggBindVO;
import net.maku.egg.vo.EggDeviceTemplateVO;
import net.maku.egg.vo.EggDeviceVO;
import net.maku.framework.common.utils.PageResult;
import net.maku.framework.common.utils.Result;
import net.maku.framework.mybatis.service.BaseService;

import java.util.List;
import java.util.Map;

/**
 * 设备表
 *
 * @author niitCDL babamu@126.com
 * <a href="https://maku.net">MAKU</a>
 */
public interface EggDeviceService extends BaseService<EggDeviceEntity> {

    PageResult<EggDeviceVO> page(EggDeviceQuery query);

    EggDeviceVO get(Long id);
//    List<DeviceWithTemplatesDTO>  getAllDevicesWithTemplates();
//    DeviceWithTemplatesDTO getDeviceWithTemplatesInfoById(Integer deviceId);

    void save(EggDeviceVO vo);


    void update(EggDeviceVO vo);


    void updateBySn(EggDeviceVO vo);

    void delete(List<Long> idList);

    void export();

    EggDeviceEntity getDeviceInfoBySn(String sn);

    List<String> getNameList(List<String> snList);

    List<String> getAllSnListByLoginUser();

    List<String> getAllSnListByShopId(Long shopId);

    //
    Map<String,String> getNameMap(List<String> snList);

    List<String> getSnList(String deviceName);

    void heartBeat(EggDeviceVO vo);

    String getLatestPriceTag(String sn);

    void bindBatch(DeviceBindBatchDTO bindBatchDTO);

    void bindDevice(EggBindVO bindVO);

    List<EggDeviceVO> getDeviceListByShop(Long shopId);
//    List<EggDeviceVO> getGatewayList();
//    List<EggDeviceVO> getGatewayListByParentDeviceId(Long parentDeviceId);
    List<EggDeviceVO> getNoBindMiniList();
    List<EggDeviceVO> getHaveBindMiniListByGatewayId(Long id);
    void deleteBindMiniRelation(Long id);

    void saveBindMini(List<Long> ids,Long parentDeviceId);

    PageResult<DeviceWithTemplatesDTO> pageWithTemplates(EggDeviceQuery query);
    void deleteDeviceTemplate(List<Long> idList);
    void saveDeviceTemplate(EggDeviceTemplateVO vo);
    Result updateDeviceTemplate(EggDeviceTemplateVO vo);
}