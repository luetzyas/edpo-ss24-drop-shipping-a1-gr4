package io.flowing.retail.vgr.producer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class Producer {

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


    public void factoryState() { // method called from the endpoint

        // Implement a MQTT client that saves always the last state of the factory of the topic
        // this is a sample message: {"state": "WAITING_FOR_ORDER", "ts": "2023-02-06T08:37:36.636Z", "type": "RED", "topic": "f/i/order"}






    }
}
