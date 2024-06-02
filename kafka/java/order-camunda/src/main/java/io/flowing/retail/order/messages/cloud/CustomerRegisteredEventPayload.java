package io.flowing.retail.order.messages.cloud;

import io.flowing.retail.order.domain.Customer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerRegisteredEventPayload {

    private String refId;
    private Customer customer;

}
