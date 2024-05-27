package io.flowing.retail.order.domain;

import com.google.gson.annotations.SerializedName;
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

}
