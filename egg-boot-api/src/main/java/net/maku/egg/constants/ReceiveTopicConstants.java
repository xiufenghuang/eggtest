package net.maku.egg.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * @author : lenovo
 * @program : egg-api
 * @description :
 * @create : 2024-09-12 14:57
 **/
@Getter
@RequiredArgsConstructor
public class ReceiveTopicConstants {
    public static final String UPDATE_DEVICE_CURRENT_WEIGHT = "update_device_current_weight";

    // 获取所有主题的数组
    public static String[] getAllTopics() {
        List<String> topics = List.of(UPDATE_DEVICE_CURRENT_WEIGHT);
        return topics.toArray(new String[topics.size()]);
    }
}
