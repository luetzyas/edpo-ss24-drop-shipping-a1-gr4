package io.flowing.retail.order.domain;

import com.google.gson.annotations.SerializedName;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import org.hibernate.annotations.GenericGenerator;



@Entity
public class Customer {
  
  @Id
  @GeneratedValue(generator = "uuid2")
  @GenericGenerator(name = "uuid2", strategy = "uuid2")
  private String id;

  @SerializedName("name")
  private String name;
  @SerializedName("address")
  private String address;

  @SerializedName("email")
  private String email;
  
  public String getName() {
    return name;
  }
  public Customer setName(String name) {
    this.name = name;
    return this;
  }
  public String getAddress() {
    return address;
  }
  public Customer setAddress(String address) {
    this.address = address;
    return this;
  }

    public String getEmail() {
        return email;
    }

    public Customer setEmail(String email) {
        this.email = email;
        return this;
    }

}
