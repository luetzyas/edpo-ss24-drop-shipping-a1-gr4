package io.flowing.retail.inventory.messages;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.flowing.retail.inventory.application.InventoryService;
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
  private ObjectMapper objectMapper;

  @Transactional
  @KafkaListener(id = "inventory", topics = MessageSender.TOPIC_NAME)
  public void messageReceived(String messagePayloadJson, @Header("type") String messageType) throws Exception{

    switch (messageType) {
      case ("FetchGoodsCommand"): {
      Message<FetchGoodsCommandPayload> message = objectMapper.readValue(messagePayloadJson, new TypeReference<Message<FetchGoodsCommandPayload>>() {});

      FetchGoodsCommandPayload fetchGoodsCommand = message.getData();
      String pickId = inventoryService.pickItems( //
              fetchGoodsCommand.getItems(), fetchGoodsCommand.getReason(), fetchGoodsCommand.getRefId());

      messageSender.send( //
              new Message<GoodsFetchedEventPayload>( //
                      "GoodsFetchedEvent", //
                      message.getTraceid(), //
                      new GoodsFetchedEventPayload() //
                              .setRefId(fetchGoodsCommand.getRefId())
                              .setPickId(pickId))
                      .setCorrelationid(message.getCorrelationid()));
    }
    case ("CheckAvailableStockEvent"): {
      Message<CheckAvailableStockEventPayload> message = objectMapper.readValue(messagePayloadJson, new TypeReference<Message<CheckAvailableStockEventPayload>>() {});

      CheckAvailableStockEventPayload checkAvailableStockEvent = message.getData();


      boolean available = inventoryService.checkAvailability(checkAvailableStockEvent.getItems());


      GoodsAvailableEventPayload goodsAvailableEventPayload = new GoodsAvailableEventPayload();
        goodsAvailableEventPayload.setRefId(checkAvailableStockEvent.getRefId());
        goodsAvailableEventPayload.setAvailable(available);

      List<GoodsAvailableEventPayload.ItemAvailability> availableItems = inventoryService.getAvailableItems(checkAvailableStockEvent.getItems());
      List<GoodsAvailableEventPayload.ItemAvailability> unavailableItems = inventoryService.getUnavailableItems(checkAvailableStockEvent.getItems());

      goodsAvailableEventPayload.setAvailableItems(availableItems);
      goodsAvailableEventPayload.setUnavailableItems(unavailableItems);

      Message<GoodsAvailableEventPayload> messagePayload = new Message<GoodsAvailableEventPayload>();
        messagePayload.setType("GoodsAvailableEvent");
        messagePayload.setTraceid(message.getTraceid());
        messagePayload.setCorrelationid(message.getCorrelationid());
        messagePayload.setData(goodsAvailableEventPayload);

      messageSender.send(messagePayload);
    }
    default:
      throw new IllegalArgumentException("Unknown message type: " + messageType);
    }





  }


}
