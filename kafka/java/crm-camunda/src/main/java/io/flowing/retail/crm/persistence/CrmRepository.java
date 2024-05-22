package io.flowing.retail.crm.persistence;

import io.flowing.retail.crm.domain.db.Customer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

@Component
public interface CrmRepository extends CrudRepository<Customer, String> {
    boolean existsByEmail(String email);

    Customer findByEmail(String email);
}
