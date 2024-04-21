package io.drop.shipping.mailing;

import io.camunda.zeebe.client.ZeebeClient;
import io.camunda.zeebe.client.api.response.ProcessInstanceEvent;
import io.camunda.zeebe.spring.client.EnableZeebeClient;
import io.camunda.zeebe.spring.client.annotation.ZeebeDeployment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@EnableZeebeClient
@ZeebeDeployment(resources = "classpath:send-email-kafka.bpmn")
//@ZeebeDeployment(resources = "classpath:test_send-email-zeebe.bpmn")
public class MailingApplication implements CommandLineRunner {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(MailingApplication.class, args);
    }

    @Autowired
    private ZeebeClient client;

    @Override
    public void run(final String... args) throws Exception {
        Map<String, Object> variables = new HashMap<>();
        variables.put("message_content", "Hello from the Spring Boot Zeebe get started");

        final ProcessInstanceEvent event =
                client
                        .newCreateInstanceCommand()
                        .bpmnProcessId("Process_0vtb1i9")
                        .latestVersion()
                        .variables(variables)
                        .send()
                        .join();

        System.out.println("Started instance for processDefinitionKey='" + event.getProcessDefinitionKey() +
                "', bpmnProcessId='" + event.getBpmnProcessId() + "', version='" + event.getVersion() +
                "' with processInstanceKey='" + event.getProcessInstanceKey() + "'");

    }

}
