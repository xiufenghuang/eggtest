package net.maku.egg.mqtt.handler;

import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.util.concurrent.AtomicDouble;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.maku.egg.constants.ReceiveTopicConstants;
import net.maku.egg.dto.DeviceWeightDTO;
import net.maku.egg.entity.EggDeviceEntity;
import net.maku.egg.entity.EggDeviceWeightRecordEntity;
import net.maku.egg.mqtt.communication.MqttMessageHandler;
import net.maku.egg.service.EggDeviceRecordService;
import net.maku.egg.service.EggDeviceService;
import net.maku.egg.service.EggDeviceWeightRecordService;
import net.maku.egg.utils.DeviceParamsUtils;
import net.maku.egg.vo.EggDeviceRecordVO;
import net.maku.egg.vo.EggDeviceVO;
import org.springframework.stereotype.Component;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * @author : lenovo
 * @program : egg-api
 * @description :
 * @create : 2024-09-12 15:10
 **/
@Component
@Slf4j
@AllArgsConstructor
public class DeviceWeightHandler implements MqttMessageHandler {
    EggDeviceRecordService recordService;
    EggDeviceWeightRecordService eggDeviceWeightRecordService;
    EggDeviceService eggDeviceService;

    @Override
    public boolean supports(String topic) {
        return ReceiveTopicConstants.UPDATE_DEVICE_CURRENT_WEIGHT.equals(topic);
    }

    @Override
    public void handle(String topic, String message) {
        switch (topic) {
            case ReceiveTopicConstants.UPDATE_DEVICE_CURRENT_WEIGHT:
                handleRecordWeight(topic, message);
                break;
        }
    }


    private void handleRecordWeight(String topic, String message) {
        try {
            // 使用标准的JSON解析
            DeviceWeightDTO weightDTO = JSONUtil.toBean(message, DeviceWeightDTO.class);
            if (weightDTO != null) {
                // 处理重量数据
                log.info("收到设备重量数据: {}", weightDTO);
                String sn = weightDTO.getSn();
                Double currentWeight = weightDTO.getCurrentWeight();
//                        || currentWeight < 0
                if (currentWeight == null ) {
                    log.error("设备sn:{},当前重量:{}传参非法", sn, currentWeight);
                    return;
                }
                EggDeviceEntity deviceInfo = eggDeviceService.getDeviceInfoBySn(sn);
                EggDeviceWeightRecordEntity weightRecordEntity = new EggDeviceWeightRecordEntity();
                //储存当前设备重量记录
                log.info("设备sn:{},当前重量:{}", sn, currentWeight);
                weightRecordEntity.setSn(deviceInfo.getSn());
                weightRecordEntity.setCurrentWeight(currentWeight);
                weightRecordEntity.setCreateTime(LocalDateTime.now());
                eggDeviceWeightRecordService.save(weightRecordEntity);
                Optional.ofNullable(deviceInfo)
                        .map(EggDeviceEntity::getPreviousWeight)
                        .ifPresentOrElse((previousWeight) -> {
                            double weight = currentWeight - previousWeight;
                            if (weight < 0) {
                                //售货操作
                                weight = Math.abs(weight);
                                log.info("设备sn:{}在{}售出重量为:{}", sn, LocalDateTime.now(), weight);
                                AtomicDouble sumSoldWeight = new AtomicDouble(weight);
                                Optional.ofNullable(recordService.getRecordBySnAndDate(sn, LocalDate.now()))
                                        .ifPresentOrElse((deviceRecord) -> {
                                            Double soldWeight = deviceRecord.getSoldWeight();
                                            sumSoldWeight.addAndGet(soldWeight);
                                            recordService.updateSoldWeightById(sumSoldWeight.get(), deviceRecord.getId());
                                        }, () -> {
                                            log.info("设备sn:{}不存在今日:{}的重量记录 初始化记录", sn, LocalDate.now());
                                            EggDeviceRecordVO recordVO = new EggDeviceRecordVO();
                                            recordVO.setSn(sn);
                                            recordVO.setSoldWeight(sumSoldWeight.get());
                                            recordVO.setCreateTime(LocalDateTime.now());
                                            recordService.save(recordVO);
                                        });
                            } else if (weight > 0) {
                                log.info("设备sn:{}在{}补货重量为{}", sn, LocalDateTime.now(), weight);
                            }
                            //处理完传入的当前重量 需要将该重量设置为上一次的重量
                            if (weight != 0) {
                                EggDeviceVO deviceVO = new EggDeviceVO();
                                deviceVO.setId(deviceInfo.getId());
                                deviceVO.setPreviousWeight(currentWeight);
                                deviceVO.setCurrentWeight(currentWeight);
                                eggDeviceService.update(deviceVO);
                            } else {
                                log.info("设备sn:{}上次重量与当前重量相同不做任何处理....", sn);
                            }
                        }, () -> {
                            log.warn("设备不存在或上一次的重量不合法,设备信息:{}", deviceInfo);
                        });
            }
        } catch (Exception e) {
            log.error("解析设备重量数据失败: {}", message, e);
        }
    }
}
