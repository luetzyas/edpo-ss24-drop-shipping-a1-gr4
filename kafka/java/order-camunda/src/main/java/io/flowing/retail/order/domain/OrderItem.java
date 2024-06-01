package io.flowing.retail.order.domain;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem {

  @Id
  @GeneratedValue(generator = "uuid2")
  @GenericGenerator(name = "uuid2", strategy = "uuid2")
  private String id;
  
  private String articleId;
  private int amount;

  public OrderItem(String toString, Integer amount) {
    this.articleId = toString;
    this.amount = amount;
  }

  @Override
  public String toString() {
    return "OrderItem [articleId=" + articleId + ", amount=" + amount + "]";
  }
}
