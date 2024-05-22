package io.flowing.retail.crm.domain;

public class CustomerMapper {
    public static io.flowing.retail.crm.domain.db.Customer toEntity(io.flowing.retail.crm.domain.Customer customer) {
        io.flowing.retail.crm.domain.db.Customer entity = new io.flowing.retail.crm.domain.db.Customer();
        entity.setCustomerId(customer.getCustomerId().toString());
        entity.setEmail(customer.getEmail().toString());
        entity.setName(customer.getName().toString());
        entity.setAddress(customer.getAddress().toString());
        return entity;
    }

    public static io.flowing.retail.crm.domain.Customer toAvro(io.flowing.retail.crm.domain.db.Customer entity) {
        return io.flowing.retail.crm.domain.Customer.newBuilder()
                .setCustomerId(entity.getCustomerId())
                .setEmail(entity.getEmail())
                .setName(entity.getName())
                .setAddress(entity.getAddress())
                .build();
    }
}
