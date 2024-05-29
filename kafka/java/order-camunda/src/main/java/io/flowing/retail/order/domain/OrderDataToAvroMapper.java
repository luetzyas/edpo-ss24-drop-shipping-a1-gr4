package io.flowing.retail.order.domain;

import io.flowing.retail.order.domain.avro.EnrichedOrder;

import java.util.List;
import java.util.stream.Collectors;

public class OrderDataToAvroMapper {

    public static EnrichedOrder convertToAvroEnrichedOrder(io.flowing.retail.order.domain.Order order, io.flowing.retail.order.domain.Customer customer) {
        List<io.flowing.retail.order.domain.avro.OrderItem> avroOrderItems = order.getItems().stream()
                .map(OrderDataToAvroMapper::convertToAvroOrderItem)
                .collect(Collectors.toList());

        io.flowing.retail.order.domain.avro.Customer avroCustomer = convertToAvroCustomer(customer);

        return new EnrichedOrder(order.getId(), avroOrderItems, avroCustomer, order.getEmail());
    }

    private static io.flowing.retail.order.domain.avro.OrderItem convertToAvroOrderItem(io.flowing.retail.order.domain.OrderItem item) {
        return new io.flowing.retail.order.domain.avro.OrderItem(
                item.getArticleId(),
                item.getAmount()
        );
    }

    private static io.flowing.retail.order.domain.avro.Customer convertToAvroCustomer(io.flowing.retail.order.domain.Customer customer) {
        if (customer == null) {
            return new io.flowing.retail.order.domain.avro.Customer(
                    "noMatch", "Unknown", "No Address", "no-email"
            );
        }
        return new io.flowing.retail.order.domain.avro.Customer(
                customer.getId(),
                customer.getName(),
                customer.getAddress(),
                customer.getEmail()
        );
    }
}

