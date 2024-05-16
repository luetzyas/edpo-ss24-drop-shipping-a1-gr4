package io.flowing.retail.crm.domain;

import com.google.gson.annotations.SerializedName;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import spinjar.javax.xml.bind.annotation.XmlAccessType;
import spinjar.javax.xml.bind.annotation.XmlAccessorType;
import spinjar.javax.xml.bind.annotation.XmlElement;
import spinjar.javax.xml.bind.annotation.XmlTransient;


@Entity
@Data
public class Customer {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @XmlTransient
    private String customerId;

    @SerializedName("email")
    @XmlElement(name = "email")
    private String email;

    @SerializedName("name")
    @XmlElement(name = "name")
    private String name;

    @SerializedName("address")
    @XmlElement(name = "address")
    private String address;
}
