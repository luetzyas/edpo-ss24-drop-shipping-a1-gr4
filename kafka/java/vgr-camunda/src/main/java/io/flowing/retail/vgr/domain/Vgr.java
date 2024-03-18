package io.flowing.retail.vgr.domain;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;


@Entity(name="VgrEntity")
public class Vgr {

  @Id
//  @GeneratedValue(generator = "uuid2")
//  @GenericGenerator(name = "uuid2", strategy = "uuid2")
  protected String id; //= UUID.randomUUID().toString();

  public String getId() {
    return id;
  }

  @JsonProperty("vgrId")
  public void setId(String id) {
    this.id = id;
  }

  @Override
  public String toString() {
    return "Vgr [id=" + id + "]";
  }


}
