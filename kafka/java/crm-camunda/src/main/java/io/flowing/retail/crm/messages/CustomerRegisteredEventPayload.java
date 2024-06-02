package io.flowing.retail.crm.messages;

import io.flowing.retail.crm.domain.Customer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerRegisteredEventPayload {

    private String refId;
    private Customer customer;

}
