package net.maku.egg.service;

import java.util.List;

public interface EggDeviceBindService {
    void handlePriceTagUpdated(String sn, Long previousTagId, Long tagId);

    void handlePriceTagUpdatedBatch(String sn, Long previousTagId, Long tagId, String priceTagUrl);

    void handleTemplateUpdated(String sn, Long previousTemplateId, Long templateId);

    void handleTemplateUpdated(String sn, List<Long> templateIds);
}
