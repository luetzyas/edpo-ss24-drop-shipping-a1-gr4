package io.flowing.retail.crm.dbLoader;

import io.flowing.retail.crm.domain.db.Customer;
import lombok.Data;
import spinjar.javax.xml.bind.annotation.XmlElement;
import spinjar.javax.xml.bind.annotation.XmlRootElement;

import java.util.List;

@Data
@XmlRootElement(name = "customers")
public class Customers {

    @XmlElement(name = "customer")
    private List<Customer> customers;
}
