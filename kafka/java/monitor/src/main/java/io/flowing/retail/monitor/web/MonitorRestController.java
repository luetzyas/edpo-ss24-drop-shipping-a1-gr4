package io.flowing.retail.monitor.web;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.flowing.retail.monitor.domain.SensorData;
import io.flowing.retail.monitor.messages.MessageListener;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.flowing.retail.monitor.domain.PastEvent;
import io.flowing.retail.monitor.persistence.LogRepository;

@RestController
public class MonitorRestController {
  
  @RequestMapping(path = "/event", method = GET)
  public Map<String, List<PastEvent>> getAllEvents() {
    return LogRepository.instance.getAllPastEvents();    
  }

  @RequestMapping(path = "/event/{traceId}", method = GET)
  public List<PastEvent> getAllEvents(@PathVariable String traceId) {
    return LogRepository.instance.getAllPastEvents(traceId);
  }

  @RequestMapping(path = "/sensor-data", method = GET)
  public List<SensorData> getAllSensorData() {
     // return MessageListener.instance.getAllSensorData();
    List<SensorData> testData = new ArrayList<>();

    SensorData data1 = new SensorData();
    data1.setAirQuality(1.0);
    data1.setGasResistance(369801.0);
    data1.setHumidity(33.0);
    data1.setIndexedAirQuality(27.0);
    data1.setAirPressure(926.8);
    data1.setRelativeHumidity(27.23);
    data1.setRoomTemperature(25.29);
    data1.setAirTemperature(22.1);
    data1.setTimestamp("2020-02-26T10:59:51.860Z");

    SensorData data2 = new SensorData();
    data2.setAirQuality(2.0);
    data2.setGasResistance(370000.0);
    data2.setHumidity(34.0);
    data2.setIndexedAirQuality(30.0);
    data2.setAirPressure(927.0);
    data2.setRelativeHumidity(28.0);
    data2.setRoomTemperature(25.5);
    data2.setAirTemperature(22.5);
    data2.setTimestamp("2020-02-26T11:59:51.860Z");

    testData.add(data1);
    testData.add(data2);

    return testData;
  }

}