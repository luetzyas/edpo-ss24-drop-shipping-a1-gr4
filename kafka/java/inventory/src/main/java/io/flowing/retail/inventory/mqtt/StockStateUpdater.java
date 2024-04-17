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
import java.util.concurrent.atomic.AtomicReference;
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
    private final AtomicReference<String> lastStateJsonRef = new AtomicReference<>();
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(StockStateUpdater.class);

    @ServiceActivator(inputChannel = "mqttInputChannel")
    public void updateOrderState(Message<String> message) {
        System.out.println("Received mqtt message to update stock state");
        String lastStateJson = message.getPayload().replace("None", "null");

       // lastStateJson = message.getPayload();
        System.out.println("Stock update message raw message: " + message);

        try {
            InventoryUpdateMessage updateMessage = objectMapper.readValue(lastStateJson, InventoryUpdateMessage.class);
            inventoryService.updateStock(updateMessage);

        } catch (Exception e) {
            e.printStackTrace();
        }


        /*
        try {
            logger.info("Received mqtt message to update stock state");
            lastStateJson = message.getPayload();
            logger.info("Stock update message raw message: " + message);
            logger.info("Stock update message: " + lastStateJson);

            // Replace None with null to avoid JsonParseException. As set to volatile, using AtomicReference for atomicity.
            lastStateJsonRef.updateAndGet(currentValue ->
                    (currentValue != null) ? currentValue.replace("None", "null") : null);


            // Process the message as needed, e.g., save to a database or log it
            logger.info("Stock update message: " + lastStateJson);

            InventoryUpdateMessage updateMessage = objectMapper.readValue(lastStateJson, InventoryUpdateMessage.class);

            // Convert the workpieces from the MQTT message to FactoryStockState objects
            inventoryService.updateStock(updateMessage);


            //PickOrder inventoryState = objectMapper.readValue(lastStateJson, PickOrder.class);
            // Save the order state using repository or any other service
            // repository.save(orderState);


        } catch (Exception e){
            e.printStackTrace();
        }
        */


    }
}
