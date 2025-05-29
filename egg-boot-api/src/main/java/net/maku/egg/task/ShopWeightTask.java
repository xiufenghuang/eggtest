package net.maku.egg.task;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.maku.egg.service.EggShopWeightService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 店铺重量记录定时任务
 */
@Slf4j
@Component
@AllArgsConstructor
public class ShopWeightTask {
    private final EggShopWeightService eggShopWeightService;

    /**
     * 每一小时执行一次
     */
    @Scheduled(fixedRate = 3600000)
    public void recordShopWeight() {
        log.info("开始执行店铺重量记录任务");
        try {
            eggShopWeightService.recordAllShopDevicesWeight();
            log.info("店铺重量记录任务执行完成");
        } catch (Exception e) {
            log.error("店铺重量记录任务执行失败", e);
        }
    }
} 