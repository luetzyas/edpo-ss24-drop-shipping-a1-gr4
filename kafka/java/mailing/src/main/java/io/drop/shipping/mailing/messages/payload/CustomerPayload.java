package io.drop.shipping.mailing.messages.payload;

import lombok.Data;

@Data
public class CustomerPayload {

  /**
   * {
   *      "name":"Camunda",
   *      "address":"Zossener Strasse 55\n10961 Berlin\nGermany"
   * }
   */

  private String name;
  private String address;
}
