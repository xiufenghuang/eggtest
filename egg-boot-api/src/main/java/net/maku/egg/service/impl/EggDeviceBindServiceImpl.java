package net.maku.egg.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.maku.egg.mqtt.sender.DeviceSender;
import net.maku.egg.service.EggDeviceBindService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author : lenovo
 * @program : egg-api
 * @description :
 * @create : 2024-11-05 14:52
 **/
@Service
@Slf4j
@AllArgsConstructor
public class EggDeviceBindServiceImpl implements EggDeviceBindService {
    private final DeviceSender deviceSender;

    @Override
    public void handlePriceTagUpdated(String sn, Long previousTagId, Long tagId) {
        if (previousTagId == null && tagId != null) {
            log.info("绑定电子价签, 标签ID:{}, 设备SN: {}", tagId, sn);
            deviceSender.sendRefreshPriceTag(sn, tagId);
        } else if (previousTagId != null && tagId == null) {
            log.info("解绑电子价签, 设备SN: {}", sn);
            deviceSender.sendRefreshPriceTag(sn, tagId);
        } else if (previousTagId != null && tagId != null && !previousTagId.equals(tagId)) {
            log.info("更换电子价签, 旧标签ID: {}, 新标签ID: {}, 设备SN: {}", previousTagId, tagId, sn);
            deviceSender.sendRefreshPriceTag(sn, tagId);
        } else if (previousTagId != null && tagId != null && previousTagId.equals(tagId)) {
            log.info("电子价签未发生变化, 标签ID: {}, 设备SN: {} 不做任何处理", tagId, sn);
        }
    }

    @Override
    public void handlePriceTagUpdatedBatch(String sn, Long previousTagId, Long tagId, String priceTagUrl) {
        if (previousTagId == null && tagId != null) {
            log.info("批量绑定电子价签, 标签ID:{}, 设备SN: {}", tagId, sn);
            deviceSender.sendRefreshPriceTag(sn, priceTagUrl);
        } else if (previousTagId != null && tagId == null) {
            log.info("批量解绑电子价签, 设备SN: {}", sn);
            deviceSender.sendRefreshPriceTag(sn, priceTagUrl);
        } else if (previousTagId != null && tagId != null && !previousTagId.equals(tagId)) {
            log.info("批量更换电子价签, 旧标签ID: {}, 新标签ID: {}, 设备SN: {}", previousTagId, tagId, sn);
            deviceSender.sendRefreshPriceTag(sn, priceTagUrl);
        } else if (previousTagId != null && tagId != null && previousTagId.equals(tagId)) {
            log.info("批量电子价签未发生变化, 标签ID: {}, 设备SN: {} 不做任何处理", tagId, sn);
        }
    }

    @Override
    public void handleTemplateUpdated(String sn, Long previousTemplateId, Long templateId) {
        if (previousTemplateId == null && templateId != null) {
            log.info("批量绑定模板, 模板ID:{}, 设备SN: {}", templateId, sn);
            deviceSender.sendRefreshTemplate(sn,templateId);
        } else if (previousTemplateId != null && templateId == null) {
            log.info("解绑模板, 设备SN: {}", sn);
            deviceSender.sendRefreshTemplate(sn,templateId);
        } else if (previousTemplateId != null && templateId != null && !previousTemplateId.equals(templateId)) {
            log.info("更换模板, 旧模板ID: {}, 新模板ID: {}, 设备SN: {}", previousTemplateId, templateId, sn);
            deviceSender.sendRefreshTemplate(sn,templateId);
        } else if (previousTemplateId != null && templateId != null && previousTemplateId.equals(templateId)) {
            log.info("模板未发生变化, 模板ID: {}, 设备SN: {} 不做任何处理", templateId, sn);
        }
    }

    @Override
    public void handleTemplateUpdated(String sn, List<Long> templateIds) {
        deviceSender.sendRefreshTemplate(sn,templateIds);
    }

}
