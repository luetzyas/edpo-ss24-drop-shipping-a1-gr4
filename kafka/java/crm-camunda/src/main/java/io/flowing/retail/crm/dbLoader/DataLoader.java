package io.flowing.retail.crm.dbLoader;

import io.flowing.retail.crm.domain.Customer;
import io.flowing.retail.crm.messages.MessageSender;
import io.flowing.retail.crm.persistence.CrmRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private CrmRepository crmRepository;

    @Autowired
    private MessageSender messageSender;

    @Override
    public void run(String... args) throws Exception {
        // Load sample data into the database
        // loadSampleData();

        List<Customer> customers = parseXMLFile(); // Load and return customers
        saveCustomers(customers); // Save customers to the database and send to Kafka
    }

    private void loadSampleData() {
        Customer customer1 = new Customer();
        customer1.setName("John Doe");
        customer1.setAddress("123 Main St, Springfield");
        customer1.setEmail("john.doe@example.com");
        crmRepository.save(customer1);

        Customer customer2 = new Customer();
        customer2.setName("Jane Smith");
        customer2.setAddress("456 Elm St, Springfield");
        customer2.setEmail("jane.smith@example.com");
        crmRepository.save(customer2);

        System.out.println("Sample data loaded into the database.");
    }



    public List<Customer> parseXMLFile() {
        List<Customer> customers = new ArrayList<>();
        try {
            // Get File
            File file = new File(Objects.requireNonNull(getClass().getClassLoader().getResource("customers.xml")).getFile());

            // Create a new factory to create parsers
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            // Parse the XML file to get a DOM Document object
            Document document = builder.parse(file);
            document.getDocumentElement().normalize();

            // Get all customers
            NodeList nList = document.getElementsByTagName("customer");

            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node node = nList.item(temp);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) node;
                    Customer customer = new Customer();
                    customer.setEmail(eElement.getElementsByTagName("email").item(0).getTextContent());
                    customer.setName(eElement.getElementsByTagName("name").item(0).getTextContent());
                    customer.setAddress(eElement.getElementsByTagName("address").item(0).getTextContent());
                    customers.add(customer);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return customers;
    }

    private void saveCustomers(List<Customer> customers) {
        crmRepository.saveAll(customers); // Save all customers to the database
        try {
            customers.forEach(customer -> {
                messageSender.send(customer); // Send each customer to the Kafka topic after saving
            });
            System.out.println("Customers loaded and sent to Kafka.");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
