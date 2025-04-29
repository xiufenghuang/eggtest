package net.maku.egg.constants;

import java.util.Optional;

/**
 * @author : lenovo
 * @program : egg-api
 * @description :
 * @create : 2024-09-23 14:54
 **/
public class SendTopicConstants {
    public static final String UPDATE_DEVICE_PRICE_TAG = "update_device_price_tag";
    public static final String UPDATE_DEVICE_TEMPLATE = "update_device_template";

    public static String getPriceTagTopic(String sn) {
        return UPDATE_DEVICE_PRICE_TAG + "/" + sn;
    }

    public static String getTemplateTopic(String sn) {
        return UPDATE_DEVICE_TEMPLATE + "/" + sn;
    }

    public static String getSnFromTopic(String topic) {
        return Optional.ofNullable(topic)
                .filter(priceTagTopic -> !priceTagTopic.isBlank())
                .map(priceTagTopic -> priceTagTopic.split("/"))
                .filter(topicParts -> topicParts.length == 2)
                .map(topicParts -> topicParts[1])
                .orElse("");
    }


}
