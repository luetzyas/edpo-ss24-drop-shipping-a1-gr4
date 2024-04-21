package io.flowing.retail.inventory.messages;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.flowing.retail.inventory.application.InventoryService;
import io.flowing.retail.inventory.connector.MessageSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class MessageListener {

  @Autowired
  private MessageSender messageSender;

  @Autowired
  private InventoryService inventoryService;

  @Autowired
  private IdempotentReceiver idempotentReceiver;

  @Autowired
  private ObjectMapper objectMapper;

  private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(MessageListener.class);


  @Transactional
  @KafkaListener(id = "inventory", topics = MessageSender.TOPIC_NAME)
  public void messageReceived(String messagePayloadJson, @Header("type") String messageType) throws Exception{

    switch (messageType) {
      case ("FetchGoodsCommand"): {
        System.out.println("Received FetchGoodsCommand");
        try {
          Message<FetchGoodsCommandPayload> message = objectMapper.readValue(messagePayloadJson, new TypeReference<Message<FetchGoodsCommandPayload>>() {});
          FetchGoodsCommandPayload fetchGoodsCommand = message.getData();
          String pickId = inventoryService.pickItems(fetchGoodsCommand.getItems(), fetchGoodsCommand.getReason(), fetchGoodsCommand.getRefId());

          GoodsFetchedEventPayload goodsFetchedPayload = new GoodsFetchedEventPayload()
                  .setRefId(fetchGoodsCommand.getRefId())
                  .setPickId(pickId);

          Message<GoodsFetchedEventPayload> goodsFetchedEventMessage = new Message<>();
            goodsFetchedEventMessage.setType("GoodsFetchedEvent");
            goodsFetchedEventMessage.setTraceid(message.getTraceid());
            goodsFetchedEventMessage.setCorrelationid(message.getCorrelationid());
            goodsFetchedEventMessage.setData(goodsFetchedPayload);

          // Add to outbox instead of sending directly
          InMemoryOutbox.addToOutbox(goodsFetchedEventMessage);
          System.out.println("Added to outbox: " + goodsFetchedEventMessage);

/*
          messageSender.send( //
                  new Message<GoodsFetchedEventPayload>( //
                          "GoodsFetchedEvent", //
                          message.getTraceid(), //
                          new GoodsFetchedEventPayload() //
                                  .setRefId(fetchGoodsCommand.getRefId())
                                  .setPickId(pickId))
                          .setCorrelationid(message.getCorrelationid()));

 */

      } catch (Exception e) {
          e.printStackTrace();
      }
      break;
    }
    case ("CheckAvailableStockEvent"): {
      System.out.println("Received CheckAvailableStockEvent");
        try {
          Message<CheckAvailableStockEventPayload> message = objectMapper.readValue(messagePayloadJson, new TypeReference<Message<CheckAvailableStockEventPayload>>() {
          });

          CheckAvailableStockEventPayload checkAvailableStockEvent = message.getData();

          // Check if the message has already been processed
          if (idempotentReceiver.isDuplicate(checkAvailableStockEvent.getRefId())) {
            System.out.println("Duplicate CheckAvailableStockEvent detected for refId: " + checkAvailableStockEvent.getRefId() + ", skipping processing.");
            return;
          }

          System.out.println("MessageListener: Check if stock available for: " + checkAvailableStockEvent.getItems());
          boolean available = inventoryService.checkAvailability(checkAvailableStockEvent.getItems());
          System.out.println("MessageListener: Stock available: " + available);

          GoodsAvailableEventPayload goodsAvailableEventPayload = new GoodsAvailableEventPayload();
            goodsAvailableEventPayload.setRefId(checkAvailableStockEvent.getRefId());
            goodsAvailableEventPayload.setAvailable(available);

          List<GoodsAvailableEventPayload.ItemAvailability> availableItems = inventoryService.getAvailableItems(checkAvailableStockEvent.getItems());
          System.out.println("MessageListener: Available items (requestedQuantity / availableQuantity): " + availableItems);
          List<GoodsAvailableEventPayload.ItemAvailability> unavailableItems = inventoryService.getUnavailableItems(checkAvailableStockEvent.getItems());
          System.out.println("MessageListener: Unavailable items (requestedQuantity / unavailableQuantity): " + unavailableItems);

          goodsAvailableEventPayload.setAvailableItems(availableItems);
          goodsAvailableEventPayload.setUnavailableItems(unavailableItems);

          Message<GoodsAvailableEventPayload> messagePayload = new Message<>();
            messagePayload.setType("GoodsAvailableEvent");
            messagePayload.setTraceid(message.getTraceid());
            messagePayload.setCorrelationid(message.getCorrelationid());
            messagePayload.setData(goodsAvailableEventPayload);
          //  System.out.println("MessageListener: Sending GoodsAvailableEvent: " + messagePayload); implementation before outbox

          // Add to outbox instead of sending directly
          InMemoryOutbox.addToOutbox(messagePayload);
          System.out.println("Added to outbox: " + messagePayload);
          // messageSender.send(messagePayload); implementation before outbox


        } catch (Exception e) {
          e.printStackTrace();
        }
    break;
    }
    default:
      throw new IllegalArgumentException("Unknown message type: " + messageType);
    }





  }


}
