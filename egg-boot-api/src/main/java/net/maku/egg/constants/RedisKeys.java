package net.maku.egg.constants;

import java.util.Optional;

/**
 * @author : lenovo
 * @program : egg-api
 * @description :
 * @create : 2024-09-23 10:30
 **/
public class RedisKeys {
    public static String DEVICE_HEART_BEAT_PREFIX = "heart-beat::";

    public static String getHeartBeatKey(String sn) {
        return DEVICE_HEART_BEAT_PREFIX + sn;
    }

    public static String getDeviceSn(String heartBeatKey) {
        return Optional.ofNullable(heartBeatKey)
                .filter(key -> !key.isBlank())
                .map(key -> key.split("::"))
                .filter(keyParts -> keyParts.length == 2)
                .map(keyParts -> keyParts[1])
                .orElse("");
    }
}
