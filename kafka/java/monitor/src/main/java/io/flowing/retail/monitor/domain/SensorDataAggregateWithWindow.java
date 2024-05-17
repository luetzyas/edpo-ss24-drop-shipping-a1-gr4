package io.flowing.retail.monitor.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SensorDataAggregateWithWindow {
    private SensorDataAggregate aggregate;
    private LocalDateTime windowStart;
    private LocalDateTime windowEnd;
}
