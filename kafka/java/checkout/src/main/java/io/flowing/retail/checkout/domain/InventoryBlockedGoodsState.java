package io.flowing.retail.checkout.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventoryBlockedGoodsState {
      private String articleId;
      private int amount;
      private Boolean consumed;
}
