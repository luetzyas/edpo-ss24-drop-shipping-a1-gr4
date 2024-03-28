package io.flowing.retail.vgr.mqtt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.flowing.retail.vgr.domain.FactoryOrderState;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
public class OrderStateUpdater {
    private volatile String lastStateJson;
    private final ObjectMapper objectMapper = new ObjectMapper();

    // @Autowired
    // private OrderStateRepository repository;


    @ServiceActivator(inputChannel = "mqttInputChannel")
    public void updateOrderState(Message<String> message) {
        try {
            lastStateJson = message.getPayload();
            // Process the message as needed, e.g., save to a database or log it
            System.out.println("Updated order state: " + lastStateJson);

            FactoryOrderState orderState = objectMapper.readValue(lastStateJson, FactoryOrderState.class);
            // Save the order state using repository or any other service
            // repository.save(orderState);



        } catch (Exception e){
            // Handle JSON parsing or other exceptions
            e.printStackTrace();
        }

    }

    // Getter for the last state for other components if necessary
    public String getLastStateJson() {
        return lastStateJson;
    }
}


