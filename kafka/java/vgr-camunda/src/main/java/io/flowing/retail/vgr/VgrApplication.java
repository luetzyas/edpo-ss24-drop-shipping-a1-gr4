package io.flowing.retail.vgr;

import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableProcessApplication
public class VgrApplication {

  public static void main(String[] args) throws Exception {
    SpringApplication.run(VgrApplication.class, args);
  }
}
