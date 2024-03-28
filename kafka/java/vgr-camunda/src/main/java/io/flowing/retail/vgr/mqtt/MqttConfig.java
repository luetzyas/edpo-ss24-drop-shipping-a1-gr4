package io.flowing.retail.vgr.mqtt;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.MessageChannel;

@Configuration
@IntegrationComponentScan
public class MqttConfig {

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(MqttConfig.class);
    @Value("${mqtt.host}")
    private String mqttHost;
    @Value("${mqtt.port}")
    private String mqttPort;
    @Value("${mqtt.username}")
    private String mqttUsername;
    @Value("${mqtt.password}")
    private String mqttPassword;
    @Value("${mqtt.topic}")
    private String mqttTopic;

    @Bean
    public DefaultMqttPahoClientFactory clientFactory() {
        try {
            logger.info("Configuring MQTT client factory with host: {}:{} and user: {}", mqttHost, mqttPort, mqttUsername);
            MqttConnectOptions options = new MqttConnectOptions();
            options.setServerURIs(new String[]{mqttHost + ":" + mqttPort});
            options.setUserName(mqttUsername);
            options.setPassword(mqttPassword.toCharArray());
            DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
            factory.setConnectionOptions(options);
            return factory;
        } catch (Exception e) {
            logger.error("Error configuring MQTT client factory", e);
            return null;
        }

    }

    @Bean
    public MqttPahoMessageDrivenChannelAdapter inboundAdapter(DefaultMqttPahoClientFactory clientFactory) {
        try {
            logger.info("Creating MQTT inbound channel adapter for topic: {}", mqttTopic);
            MqttPahoMessageDrivenChannelAdapter adapter =
                    new MqttPahoMessageDrivenChannelAdapter(mqttHost, clientFactory);
            adapter.addTopic(mqttTopic);
            adapter.setCompletionTimeout(5000);
            adapter.setConverter(new DefaultPahoMessageConverter());
            adapter.setOutputChannel(mqttInputChannel());
            return adapter;
        } catch (Exception e) {
            logger.error("Error creating MQTT inbound channel adapter", e);
            return null;
        }

    }

    @Bean
    public MessageChannel mqttInputChannel() {
        logger.info("Configuring MQTT input channel");
        return new DirectChannel();
    }

}
