package io.flowing.retail.crm;

import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableProcessApplication
public class CrmApplication {

  public static void main(String[] args) throws Exception {
    SpringApplication.run(CrmApplication.class, args);
  }
}
