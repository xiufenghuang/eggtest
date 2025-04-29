package net.maku.egg.mqtt.sender;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.maku.egg.constants.SendTopicConstants;
import net.maku.egg.entity.EggPriceTagInfoEntity;
import net.maku.egg.mqtt.gateway.MqttGateway;
import net.maku.egg.service.EggPriceTagInfoService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author : lenovo
 * @program : egg-api
 * @description :
 * @create : 2024-09-23 15:03
 **/
@Component
@Slf4j
@AllArgsConstructor
public class DeviceSender {
    private MqttGateway mqttGateway;

    private EggPriceTagInfoService priceTagInfoService;


    public void sendRefreshPriceTag(String sn, Long tagId) {
        String priceTagTopic = SendTopicConstants.getPriceTagTopic(sn);
        Optional.ofNullable(priceTagInfoService.getById(tagId))
                .map(EggPriceTagInfoEntity::getImageUrl)
                .filter(url -> !url.isBlank())
                .ifPresent((priceTagUrl) -> {
                    mqttGateway.sendToMqtt(priceTagTopic, priceTagUrl);
                });
    }

    public void sendRefreshPriceTag(String sn, String tagUrl) {
        String priceTagTopic = SendTopicConstants.getPriceTagTopic(sn);
        Optional.ofNullable(tagUrl)
                .filter(url -> !url.isBlank())
                .ifPresent((priceTagUrl) -> {
                    mqttGateway.sendToMqtt(priceTagTopic, priceTagUrl);
                });
    }

    public void sendRefreshTemplate(String sn, Long templateId) {
        String templateTopic = SendTopicConstants.getTemplateTopic(sn);
        Optional.ofNullable(templateId)
                .ifPresent((id) -> {
                    mqttGateway.sendToMqtt(templateTopic, id.toString());
                });
    }

    public void sendRefreshTemplate(String sn, List<Long> templateIds) {
        String templateTopic = SendTopicConstants.getTemplateTopic(sn);
        try {
            if (templateIds == null || templateIds.isEmpty()) {
                log.warn("模板ID列表为空，不发送消息");
                return;
            }

            // 将 templateIds 转换为逗号分隔的字符串
            String templateIdsString = templateIds.stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(","));

            log.info("准备发送MQTT消息，主题: {}, 内容: {}", templateTopic, templateIdsString);
            
            // 确保消息内容不为空
            if (templateIdsString == null || templateIdsString.trim().isEmpty()) {
                log.error("消息内容为空，不发送消息");
                return;
            }

            // 发送消息，设置QoS为1
            mqttGateway.sendToMqtt(templateTopic, 1, templateIdsString);
            log.info("MQTT消息发送成功，主题: {}, 内容: {}", templateTopic, templateIdsString);
        } catch (Exception e) {
            log.error("MQTT消息发送失败，主题: {}, 内容: {}, 异常: {}", templateTopic, templateIds, e.getMessage(), e);
            throw new RuntimeException("MQTT消息发送失败: " + e.getMessage(), e);
        }
    }

}
