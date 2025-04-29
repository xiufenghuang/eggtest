package net.maku.egg.mqtt.factory;

import lombok.RequiredArgsConstructor;
import net.maku.egg.mqtt.communication.MqttMessageHandler;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author : lenovo
 * @program : egg-api
 * @description :
 * @create : 2024-09-10 15:04
 **/
@Component
@RequiredArgsConstructor
public class MqttMessageHandlerFactory {
    private final ApplicationContext applicationContext;

    /**
     * 所有消息处理器
     */
    private List<MqttMessageHandler> messageHandlers;

    private List<MqttMessageHandler> loadHandlers() {
        if (messageHandlers != null) {
            return messageHandlers;
        }
        messageHandlers = new ArrayList<>(applicationContext.getBeansOfType(MqttMessageHandler.class).values());
        return messageHandlers;
    }

    /**
     * 获取与主题对应的处理器
     *
     * @param topic 主题
     * @return 处理器列表
     */
    public List<MqttMessageHandler> getHandlersForTopic(String topic) {
        return Collections.unmodifiableList(loadHandlers().stream()
                .filter(handler -> handler.supports(topic))
                .collect(Collectors.toList()));
    }
}
