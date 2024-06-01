package io.flowing.retail.order.domain;

import io.flowing.retail.order.domain.avro.EnrichedOrder;

import java.util.List;
import java.util.stream.Collectors;

public class OrderDataToAvroMapper {

    public static EnrichedOrder convertToAvroEnrichedOrder(io.flowing.retail.order.domain.Order order, io.flowing.retail.order.domain.Customer customer) {
        List<io.flowing.retail.order.domain.avro.OrderItem> avroOrderItems = order.getItems().stream()
                .map(OrderDataToAvroMapper::convertToAvroOrderItem)
                .collect(Collectors.toList());

        io.flowing.retail.order.domain.avro.Customer avroCustomer = convertToAvroCustomer(customer, order);

        return new EnrichedOrder(order.getId(), avroOrderItems, avroCustomer, order.getEmail());
    }

    private static io.flowing.retail.order.domain.avro.OrderItem convertToAvroOrderItem(io.flowing.retail.order.domain.OrderItem item) {
        return new io.flowing.retail.order.domain.avro.OrderItem(
                item.getArticleId(),
                item.getAmount()
        );
    }

    private static io.flowing.retail.order.domain.avro.Customer convertToAvroCustomer(io.flowing.retail.order.domain.Customer customer, io.flowing.retail.order.domain.Order order) {
        if (customer == null) {
            return new io.flowing.retail.order.domain.avro.Customer(
                    "noMatch", "Unknown", "No Address", order.getEmail()
            );
        }
        return new io.flowing.retail.order.domain.avro.Customer(
                customer.getId(),
                customer.getName(),
                customer.getAddress(),
                customer.getEmail()
        );
    }

    public static io.flowing.retail.order.domain.Order convertToOrder(io.flowing.retail.order.domain.avro.EnrichedOrder avroOrder) {
        List<io.flowing.retail.order.domain.OrderItem> orderItems = avroOrder.getItems().stream()
                .map(OrderDataToAvroMapper::convertToOrderItem)
                .collect(Collectors.toList());

        return new io.flowing.retail.order.domain.Order(
                avroOrder.getOrderId().toString(),
                orderItems,
                avroOrder.getEmail().toString()
        );
    }

    private static io.flowing.retail.order.domain.OrderItem convertToOrderItem(io.flowing.retail.order.domain.avro.OrderItem avroOrderItem) {
        return new io.flowing.retail.order.domain.OrderItem(
                avroOrderItem.getArticleId().toString(),
                avroOrderItem.getAmount()
        );
    }
}

