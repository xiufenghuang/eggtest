package net.maku.egg.controller;

import net.maku.egg.mqtt.gateway.MqttGateway;
import net.maku.framework.common.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : lenovo
 * @program : egg-api
 * @description :
 * @create : 2024-09-10 15:41
 **/
@RequestMapping("/mqtt")
@RestController
public class TestMqttMsgController {

    @Autowired
    MqttGateway mqttGateway;

    @GetMapping("/send/topic/{topic}/msg-content/{content}")
    public Result testMqttSendMsg(@PathVariable String topic, @PathVariable String content) {
        mqttGateway.sendToMqtt(topic, content);
        return Result.ok("发送成功");
    }
}
