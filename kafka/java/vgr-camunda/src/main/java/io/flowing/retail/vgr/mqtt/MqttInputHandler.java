package io.flowing.retail.vgr.mqtt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.flowing.retail.vgr.domain.Bme680Data;
import io.flowing.retail.vgr.domain.FactoryOrderState;
import io.flowing.retail.vgr.messages.MessageSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
public class MqttInputHandler {
    private volatile String lastOrderStateJson;
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private MessageSender messageSender;
    // @Autowired
    // private OrderStateRepository repository;


    @ServiceActivator(inputChannel = "mqttInputChannel")
    public void inputHandler(Message<String> message) {
        try {
            String topic = message.getHeaders().get("mqtt_receivedTopic").toString();
            String payload = message.getPayload();
           // System.out.println("Received message from topic: " + topic);
           // System.out.println("Message payload: " + payload);

            if ("f/i/order".equals(topic)) {
                lastOrderStateJson = message.getPayload();
                FactoryOrderState orderState = objectMapper.readValue(lastOrderStateJson, FactoryOrderState.class);

                messageSender.sendToKafka("factory-orders", payload, "OrderUpdate");
            } else if ("i/bme680".equals(topic)) {
                // forwarding sensor data to Kafka
                // Bme680Data data = objectMapper.readValue(payload, Bme680Data.class);
                messageSender.sendToKafka("sensor-data", payload, "SensorUpdate");
            }


        } catch (Exception e){
            // Handle JSON parsing or other exceptions
            e.printStackTrace();
        }

    }

    // Getter for the last state for other components if necessary
    public String getLastOrderStateJson() {
        return lastOrderStateJson;
    }
}


