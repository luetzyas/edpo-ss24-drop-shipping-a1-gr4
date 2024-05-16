package io.flowing.retail.monitor.streams;

import io.flowing.retail.monitor.domain.SensorDataAggregate;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StoreQueryParameters;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyWindowStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class KafkaStreamsService {
    private KafkaStreams streams;

    public void registerStreams(KafkaStreams streams) {
        this.streams = streams;
    }
    public boolean isRunning() {
        return streams != null && streams.state().isRunningOrRebalancing();
    }
    public ReadOnlyWindowStore<String, SensorDataAggregate> getHourlyAverageStore() {
        return streams.store(StoreQueryParameters.fromNameAndType("hourly-average-store", QueryableStoreTypes.windowStore()));
    }
}

