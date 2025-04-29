package net.maku.egg.mqtt.config;

import cn.hutool.core.util.IdUtil;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import net.maku.egg.constants.ReceiveTopicConstants;
import net.maku.egg.mqtt.factory.MqttMessageHandlerFactory;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessageHeaders;

import java.util.Map;
import java.util.Set;

/**
 * MQTT 配置类，用于设置和管理 MQTT 连接和消息处理。
 *
 * @author LSF maku_lsf@163.com
 */
@Data
@Slf4j
@Configuration
@IntegrationComponentScan
@ConfigurationProperties(prefix = "spring.mqtt")
public class MqttConfig {
    public static final String OUTBOUND_CHANNEL = "mqttOutboundChannel";
    public static final String INPUT_CHANNEL = "mqttInputChannel";

    // MQTT 用户名
    private String username;

    // MQTT 密码
    private String password;

    // MQTT 服务器 URL
    private String host;

    // 客户端 ID
    private String clientId;

    // 默认主题
    private String defaultTopic;

    // 处理 MQTT 消息的工厂
    @Resource
    private MqttMessageHandlerFactory mqttMessageHandlerFactory;

    @PostConstruct
    public void init() {
        log.info("MQTT 主机: {} 客户端ID: {} 默认主题：{}", this.host, this.clientId, this.defaultTopic);
    }

    /**
     * 配置并返回一个 MqttPahoClientFactory 实例，用于创建 MQTT 客户端连接。
     *
     * @return MqttPahoClientFactory
     */
    @Bean
    public MqttPahoClientFactory mqttClientFactory() {
        // 设置连接选项，包括服务器 URI、用户名和密码。
        final MqttConnectOptions options = new MqttConnectOptions();
        options.setServerURIs(new String[]{host});
        options.setUserName(username);
        options.setPassword(password.toCharArray());
        final DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        factory.setConnectionOptions(options);
        return factory;
    }

    /**
     * 创建一个用于发送 MQTT 消息的 MessageChannel。
     *
     * @return MessageChannel
     */
    @Bean(OUTBOUND_CHANNEL)
    public MessageChannel mqttOutboundChannel() {
        return new DirectChannel();
    }

    /**
     * 配置用于发送 MQTT 消息的 MessageHandler。
     *
     * @return MessageHandler
     */
    @Bean
    @ServiceActivator(inputChannel = OUTBOUND_CHANNEL)
    public MessageHandler mqttOutboundHandler() {
        // 使用 MqttPahoMessageHandler 创建一个新的 MQTT 客户端连接，用于发布消息。
        final MqttPahoMessageHandler handler = new MqttPahoMessageHandler(getClientId(clientId + "_pub"), mqttClientFactory());
        handler.setDefaultQos(1);
        handler.setDefaultRetained(false);
        handler.setDefaultTopic(defaultTopic);
        handler.setAsync(true);
        return handler;
    }

    /**
     * 创建用于接收 MQTT 消息的 MessageChannel。
     *
     * @return MessageChannel
     */
    @Bean
    public MessageChannel mqttInputChannel() {
        return new DirectChannel();
    }

    /**
     * 配置  客户端，订阅的主题，
     * PROPERTY：设备属性上报主题，
     * COMMAND_RESPONSE：下发指令执行结果主题
     *
     * @return MqttPahoMessageDrivenChannelAdapter
     */
    @Bean
    public MqttPahoMessageDrivenChannelAdapter mqttInboundAdapter() {
        final MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(
                getClientId(clientId + "_sub"),
                mqttClientFactory(),
                ReceiveTopicConstants.getAllTopics()
        );
        adapter.setCompletionTimeout(15000);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(1);
        adapter.setOutputChannel(mqttInputChannel());
        return adapter;
    }

    /**
     * 通过通道获取数据并处理消息。
     *
     * @return MessageHandler
     */
    @Bean
    @ServiceActivator(inputChannel = INPUT_CHANNEL)
    public MessageHandler mqttMessageHandler() {
        return message -> {
            try {
                String topic = (String) message.getHeaders().get("mqtt_receivedTopic");
                log.info("接收到主题的消息: {}", topic);
                
                if (topic != null) {
                    // 获取消息内容
                    Object payload = message.getPayload();
                    String payloadStr = payload != null ? payload.toString() : "";
                    
                    log.info("消息内容: {}", payloadStr);
                    
                    // 获取并执行对应的处理器
                    mqttMessageHandlerFactory.getHandlersForTopic(topic).forEach(handler -> {
                        try {
                            handler.handle(topic, payloadStr);
                        } catch (Exception e) {
                            log.error("处理主题 {} 的消息时发生错误: {}", topic, e.getMessage(), e);
                        }
                    });
                } else {
                    log.warn("接收到主题为null的消息");
                }
            } catch (Exception e) {
                log.error("处理MQTT消息时发生错误: {}", e.getMessage(), e);
            }
        };
    }

    private String getClientId(String clientId) {
        return clientId + "_" + IdUtil.fastSimpleUUID();
    }
}
