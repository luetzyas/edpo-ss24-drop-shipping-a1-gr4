package io.flowing.retail.crm;

import io.flowing.retail.crm.domain.Customer;
import io.flowing.retail.crm.persistence.CrmRepository;
import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableProcessApplication
public class CrmApplication {

    @Value("${demo.value}")
    private int runDemo;

    public static void main(String[] args) {
        SpringApplication.run(CrmApplication.class, args);
    }

    @Bean
    public CommandLineRunner demo(CrmRepository crmRepository) {
        return (args) -> {
            switch (runDemo) {
                case 1:
                    String demoEmail = "trobson1@google.ca";
                    System.out.println(crmRepository.findByEmail(demoEmail));
                    break;
                case 2:
                    // Fetch all customers
                    Iterable<Customer> customers = crmRepository.findAll();
                    customers.forEach(System.out::println);
                    break;
                default:
                    break;
            }


        };
    }

}
