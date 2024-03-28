package io.drop.shipping.mailing.mqtt;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class StockStateUpdater {

    @Value("${mqtt.host}")
    private String mqttHost;

    @Value("${mqtt.port}")
    private String mqttPort;

    @Value("${mqtt.username}")
    private String mqttUsername;

    @Value("${mqtt.password}")
    private String mqttPassword;

    @Value("${mqtt.topic}")
    private String mqttTopic;

    public void factoryState() { // method called from the endpoint

        // TODO: Implement a MQTT client that saves always the last state of the factory of the topic
        // TODO: this is a sample message: { "stockItems" :  [  {   "location" : "A1",   "workpiece" :    {    "id" : "04f2b74a616080",    "state" : "RAW",    "type" : "WHITE"   }  },  {   "location" : "A2",   "workpiece" :    {    "id" : "04b4ae4a616080",    "state" : "RAW",    "type" : "BLUE"   }  },  {   "location" : "A3",   "workpiece" : null  },  {   "location" : "B1",   "workpiece" : null  },  {   "location" : "B2",   "workpiece" :    {    "id" : "04f8ad4a616080",    "state" : "RAW",    "type" : "WHITE"   }  },  {   "location" : "B3",   "workpiece" :    {    "id" : "042fac4a616080",    "state" : "RAW",    "type" : "RED"   }  },  {   "location" : "C1",   "workpiece" : null  },  {   "location" : "C2",   "workpiece" : null  },  {   "location" : "C3",   "workpiece" : null  } ], "ts" : "2020-12-11T12:14:24.717Z" }






    }
}
