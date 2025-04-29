package net.maku.egg.mqtt.communication;

/**
 * @author : lenovo
 * @program : egg-api
 * @description :
 * @create : 2024-09-10 15:05
 **/
public interface MqttMessageHandler {
    /**
     * 是否支持处理指定的topic
     *
     * @param topic
     * @return
     */
    boolean supports(String topic);

    /**
     * mqtt消息处理接口
     *
     * @param topic
     * @param message
     */
    void handle(String topic, String message);
}
