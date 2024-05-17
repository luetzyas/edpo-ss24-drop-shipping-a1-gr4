package io.flowing.retail.monitor.web;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.flowing.retail.monitor.domain.SensorData;
import io.flowing.retail.monitor.domain.SensorDataAggregate;
import io.flowing.retail.monitor.domain.SensorDataAggregateWithWindow;
import io.flowing.retail.monitor.messages.MessageListener;
import io.flowing.retail.monitor.streams.KafkaStreamsService;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.Windowed;
import org.apache.kafka.streams.state.KeyValueIterator;
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
  public List<SensorDataAggregateWithWindow> getAllSensorData() {
    List<SensorDataAggregateWithWindow> sensorDataList = new ArrayList<>();
    if (!kafkaStreamsService.isRunning()) {
      throw new IllegalStateException("Monitor Rest Controller: Kafka Streams is not running");
    }
    try {
      ReadOnlyWindowStore<String, SensorDataAggregate> store = kafkaStreamsService.getHourlyAverageStore();
      Instant now = Instant.now();
      Instant from = now.minus(Duration.ofHours(24)); // Adjust the duration as needed
      System.out.println("******* RestController: Fetching data from " + from + " to " + now);

      // Fetch all entries within the time range
      KeyValueIterator<Windowed<String>, SensorDataAggregate> iterator = store.fetchAll(from, now);
      while (iterator.hasNext()) {
        KeyValue<Windowed<String>, SensorDataAggregate> entry = iterator.next();
        // Log each entry
        System.out.println("Key: " + entry.key + ", Value: " + entry.value);

        ZoneId targetTimeZone = ZoneId.of("Europe/Berlin"); // Set timezone

        LocalDateTime windowStart = entry.key.window().startTime().atZone(targetTimeZone).toLocalDateTime();
        LocalDateTime windowEnd = entry.key.window().endTime().atZone(targetTimeZone).toLocalDateTime();
        sensorDataList.add(new SensorDataAggregateWithWindow(
                entry.value,
                windowStart,
                windowEnd
        ));
      }
      iterator.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    System.out.println(sensorDataList);
    return sensorDataList;
  }

}