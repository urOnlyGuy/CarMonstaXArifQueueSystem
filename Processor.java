import java.io.*;
import java.util.*;
import java.util.LinkedList;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Processor {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private LinkedList<CustomerInfo> customerList; // linked list to store all customers
    private Stack<CustomerInfo> completedStack; // stack to store completed transactions
    
    public Processor() {
        customerList = new LinkedList<>();
        completedStack = new Stack<>();
    }

    // getter for customer list
    public LinkedList<CustomerInfo> getCustomerList() {
        return customerList;
    }

    // getter for completed stack
    public Stack<CustomerInfo> getCompletedStack() {
        return completedStack;
    }

    // method to read data from the file
    public void loadCustomerDataFromFile(String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                processCustomerData(line); // process each line
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage()); // no space at the start of the comment
        }
    }

    // method to process customer data
    private void processCustomerData(String data) {
        StringTokenizer tokenizer = new StringTokenizer(data, ";");

        // extract customer details
        String customerId = tokenizer.nextToken();
        String customerName = tokenizer.nextToken();
        String vehiclePlateNumber = tokenizer.nextToken();

        // create customer object
        CustomerInfo customer = new CustomerInfo(customerId, customerName, vehiclePlateNumber);

        // process services
        while (tokenizer.hasMoreTokens()) {
            String servicesData = tokenizer.nextToken();
            processServicesData(servicesData, customer);
        }

        // add the customer to the customer list
        customerList.add(customer);
    }

    // method to process services for a customer
    private void processServicesData(String servicesData, CustomerInfo customer) {
        StringTokenizer serviceTokenizer = new StringTokenizer(servicesData, "|"); // multiple services separated by "|"
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        while (serviceTokenizer.hasMoreTokens()) {
            String singleService = serviceTokenizer.nextToken(); // each service data
            String[] serviceDetails = singleService.split(",");

            // create ServiceInfo object
            int serviceId = Integer.parseInt(serviceDetails[0]);
            String serviceType = serviceDetails[1];
            double serviceCost = Double.parseDouble(serviceDetails[2]);
            LocalDate serviceDate = LocalDate.parse(serviceDetails[3]);
            int estCompletionTime = Integer.parseInt(serviceDetails[4]);

            ServiceInfo service = new ServiceInfo(serviceId, serviceType, serviceCost, serviceDate, estCompletionTime);

            // add the service to the customer's service list
            customer.addService(service);
        }
    }

    // method to mark a customer as completed
    public void markCustomerAsComplete(CustomerInfo customer) {
        completedStack.push(customer); // add the customer to the stack
        customerList.remove(customer); // remove from the customer list
    }

    // method to get the number of completed transactions
    public int getCompletedTransactionCount() {
        return completedStack.size();
    }

    // method to calculate total sales
    public double calculateTotalSales() {
        double totalSales = 0.0;
        for (CustomerInfo customer : completedStack) {
            totalSales += customer.calculateTotalCost();
        }
        return totalSales;
    }
}