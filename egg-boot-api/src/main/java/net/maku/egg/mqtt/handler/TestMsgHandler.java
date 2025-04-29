package net.maku.egg.mqtt.handler;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import lombok.AllArgsConstructor;
import net.maku.egg.mqtt.communication.MqttMessageHandler;
import net.maku.egg.mqtt.gateway.MqttGateway;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author : lenovo
 * @program : egg-api
 * @description :
 * @create : 2024-09-10 16:09
 **/
@Component
@AllArgsConstructor
public class TestMsgHandler implements MqttMessageHandler {

    private MqttGateway mqttGateway;

    @Override
    public boolean supports(String topic) {
        return StrUtil.isNotEmpty(topic) && "mqtt_receivedTopic/test".equals(topic);
    }

    @Override
    public void handle(String topic, String message) {
        Map<String, String> msgMap = new HashMap<>();
        msgMap.put("answer", "hello world");
        mqttGateway.sendToMqtt("test", JSONUtil.toJsonStr(msgMap));
    }

}
