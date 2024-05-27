package io.flowing.retail.checkout.domain;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class Order {
  
  private String orderId = "checkout-generated-" + UUID.randomUUID().toString();
  private String email;
  private List<Item> items = new ArrayList<>();
  
  public void addItem(String articleId, int amount) {
    // keep only one item, but increase amount accordingly
    Item existingItem = removeItem(articleId);
    if (existingItem!=null) {
      amount += existingItem.getAmount();
    }
    
    Item item = new Item();
    item.setAmount(amount);
    item.setArticleId(articleId);
    items.add(item);    
  }

  public Item removeItem(String articleId) {
    for (Item item : items) {
      if (articleId.equals(item.getArticleId())) {
        items.remove(item);
        return item;
      }
    }
    return null;
  }
  

  @Override
  public String toString() {
    return "Order [orderId=" + orderId + ", items=" + items + "]";
  }




}
