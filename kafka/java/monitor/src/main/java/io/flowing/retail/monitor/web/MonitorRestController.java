package io.flowing.retail.monitor.web;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.flowing.retail.monitor.domain.SensorData;
import io.flowing.retail.monitor.domain.SensorDataAggregate;
import io.flowing.retail.monitor.messages.MessageListener;
import io.flowing.retail.monitor.streams.KafkaStreamsService;
import org.apache.kafka.streams.state.ReadOnlyWindowStore;
import org.apache.kafka.streams.state.WindowStoreIterator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.flowing.retail.monitor.domain.PastEvent;
import io.flowing.retail.monitor.persistence.LogRepository;

@RestController
public class MonitorRestController {
  @Autowired
  private KafkaStreamsService kafkaStreamsService; // Service to interact with Kafka Streams

  @RequestMapping(path = "/event", method = GET)
  public Map<String, List<PastEvent>> getAllEvents() {
    return LogRepository.instance.getAllPastEvents();    
  }

  @RequestMapping(path = "/event/{traceId}", method = GET)
  public List<PastEvent> getAllEvents(@PathVariable String traceId) {
    return LogRepository.instance.getAllPastEvents(traceId);
  }

  @RequestMapping(path = "/sensor-data", method = GET)
  public List<SensorDataAggregate> getAllSensorData() {
    List<SensorDataAggregate> sensorDataList = new ArrayList<>();
    if (!kafkaStreamsService.isRunning()) {
      throw new IllegalStateException("Kafka Streams is not running");
    }
    try {
      ReadOnlyWindowStore<String, SensorDataAggregate> store = kafkaStreamsService.getHourlyAverageStore();
      Instant now = Instant.now();
      Instant from = now.minus(Duration.ofHours(24)); // Adjust the duration as needed
      System.out.println("******* RestController: Fetching data from " + from + " to " + now);
      store.fetchAll(from, now).forEachRemaining(entry -> {
        WindowStoreIterator<SensorDataAggregate> iterator = store.fetch(String.valueOf(entry.key), from, now);
        System.out.println("First value = Key: " + entry.key + ", Value: " + entry.value);
        while (iterator.hasNext()) {
          sensorDataList.add(iterator.next().value);
          System.out.println("Next Value: " + iterator.next().value);
        }
      });
    } catch (Exception e) {
      e.printStackTrace();
    }
    return sensorDataList;
  }

}