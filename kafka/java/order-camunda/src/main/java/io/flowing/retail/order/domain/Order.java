package io.flowing.retail.order.domain;

import com.google.gson.annotations.SerializedName;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity(name="OrderEntity")
public class Order {

  @Id
//  @GeneratedValue(generator = "uuid2")
//  @GenericGenerator(name = "uuid2", strategy = "uuid2")
  @SerializedName("orderId")
  protected String id; //= UUID.randomUUID().toString();

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER  )
  protected List<OrderItem> items = new ArrayList<OrderItem>();

  private String email;

  public void addItem(OrderItem i) {
    items.add(i);
  }
  
  public int getTotalSum() {
    int sum = 0;
    for (OrderItem orderItem : items) {
      sum += orderItem.getAmount();
    }
    return sum;
  }
  
  public String getId() {
    return id;
  }

  @JsonProperty("orderId")
  public void setId(String id) {
    this.id = id;
  }

  public List<OrderItem> getItems() {
    return items;
  }
 
  @Override
  public String toString() {
    return "Order [id=" + id + ", items=" + items + "]";
  }


  public void setEmail(String email) {
    this.email = email;
  }

  public String getEmail() {
    return email;
  }

}
