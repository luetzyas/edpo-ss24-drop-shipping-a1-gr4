package io.flowing.retail.inventory.mqtt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.flowing.retail.inventory.application.InventoryService;
import io.flowing.retail.inventory.domain.FactoryStockState;
import io.flowing.retail.inventory.domain.InventoryUpdateMessage;
import io.flowing.retail.inventory.domain.PickOrder;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StockStateUpdater {

    @Getter
    private volatile String lastStateJson;
    private final ObjectMapper objectMapper = new ObjectMapper();

    // @Autowired
    // private OrderStateRepository repository;

    @Autowired
    private InventoryService inventoryService;


    @ServiceActivator(inputChannel = "mqttInputChannel")
    public void updateOrderState(Message<String> message) {
        try {
            lastStateJson = message.getPayload();
            // Process the message as needed, e.g., save to a database or log it
            System.out.println("Updated order state: " + lastStateJson);

            InventoryUpdateMessage updateMessage = objectMapper.readValue(lastStateJson, InventoryUpdateMessage.class);

            // Convert the workpieces from the MQTT message to FactoryStockState objects
            inventoryService.updateStock(updateMessage);


            //PickOrder inventoryState = objectMapper.readValue(lastStateJson, PickOrder.class);
            // Save the order state using repository or any other service
            // repository.save(orderState);


        } catch (Exception e){
            // Handle JSON parsing or other exceptions
            e.printStackTrace();
        }

    }
}
