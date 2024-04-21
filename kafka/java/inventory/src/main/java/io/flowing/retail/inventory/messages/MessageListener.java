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
    case ("ReserveGoodsCommand"): {
      System.out.println("Received ReserveGoodsCommand");
        try {
          Message<ReserveStockItemsCommandPayload> message = objectMapper.readValue(messagePayloadJson, new TypeReference<Message<ReserveStockItemsCommandPayload>>() {
          });

          ReserveStockItemsCommandPayload reserveStockItemsCommand = message.getData();

          // Check if the message has already been processed
          if (idempotentReceiver.isDuplicate(reserveStockItemsCommand.getRefId())) {
            System.out.println("Duplicate ReserveGoodsCommand detected for refId: " + reserveStockItemsCommand.getRefId() + ", skipping processing.");
            return;
          }

          // Reserve goods and order additional items if necessary
          boolean goodsReserved = inventoryService.reserveGoods(reserveStockItemsCommand, message.getTraceid(), message.getCorrelationid());


          // Send AllGoodsAvailableEvent
          if (goodsReserved) {
            AllGoodsAvailableEventPayload allGoodsAvailableEventPayload = new AllGoodsAvailableEventPayload();
            allGoodsAvailableEventPayload.setRefId(reserveStockItemsCommand.getRefId());
            allGoodsAvailableEventPayload.setItems(reserveStockItemsCommand.getItems());

            Message<AllGoodsAvailableEventPayload> allGoodsAvailableEventMessage = new Message<>();
              allGoodsAvailableEventMessage.setType("AllGoodsAvailableEvent");
              allGoodsAvailableEventMessage.setTraceid(message.getTraceid());
              allGoodsAvailableEventMessage.setCorrelationid(message.getCorrelationid());
              allGoodsAvailableEventMessage.setData(allGoodsAvailableEventPayload);

            // Add to outbox instead of sending directly
            InMemoryOutbox.addToOutbox(allGoodsAvailableEventMessage);
            System.out.println("Added to outbox: " + allGoodsAvailableEventMessage);
          }

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
