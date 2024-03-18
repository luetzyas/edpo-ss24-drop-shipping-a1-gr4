package io.flowing.retail.zeebe.order;

import io.camunda.zeebe.spring.client.annotation.Deployment;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Deployment(resources = "order-zeebe.bpmn")
public class OrderFulfillmentApplication {
  
  public static void main(String[] args) throws Exception {
    SpringApplication.run(OrderFulfillmentApplication.class, args);
  }

}
