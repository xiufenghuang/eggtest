package net.maku.egg.listener;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.maku.egg.constants.RedisKeys;
import net.maku.egg.enums.DeviceRunningStatusEnum;
import net.maku.egg.service.EggDeviceService;
import net.maku.egg.vo.EggDeviceVO;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author : lenovo
 * @program : egg-api
 * @description :
 * @create : 2024-09-23 10:27o
 **/
@Component
@Slf4j
@AllArgsConstructor
public class DeviceHeartbeatListener implements MessageListener {

    EggDeviceService deviceService;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String expiredKey = message.toString();
        Optional.ofNullable(expiredKey)
                .filter(key -> !key.isBlank())
                .ifPresent(key -> {
                    if (key.startsWith(RedisKeys.DEVICE_HEART_BEAT_PREFIX)) {
                        log.warn("设备心跳停止 key:{}", key);
                        offlineDevice(key);
                    }
                });
    }

    private void offlineDevice(String expiredKey) {
        EggDeviceVO device = new EggDeviceVO();
        device.setStatus(DeviceRunningStatusEnum.OFFLINE.getValue());
        device.setSn(RedisKeys.getDeviceSn(expiredKey));
        deviceService.updateBySn(device);
    }

}
