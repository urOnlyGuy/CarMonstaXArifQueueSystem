//after integration

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.awt.event.ActionListener;
import java.util.Stack;

import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;
import javax.swing.table.DefaultTableModel;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class MainDashboard extends JFrame{
    // variables for all components
    private JPanel topPanel, mainPanel, bottomPanel;
    private JLabel lblCustomersLeft, lblServicesPerformed, lblTotalSales, lblDateTime;
    private JTable table1, table2, table3 ;
    private JScrollPane tableScrollPane1, tableScrollPane2, tableScrollPane3;
    private JTabbedPane tabbedPane;
    private JPanel tab1, tab2, tab3;
    private JPanel tab1ActionPanel, tab2ActionPanel, tab3ActionPanel;
    private JLabel lblTab1CustomerInfo, lblTab2CustomerInfo, lblTab3CustomerInfo;
    private JButton btnTab1CreateReceipt, btnTab1CancelOrder;
    private JButton btnTab2CreateReceipt, btnTab2CancelOrder;
    private JButton btnTab3CreateReceipt, btnTab3CancelOrder;
    private ImageIcon icon;
    private Timer timer;

    private Processor processor; // processor instance
    private Queue<CustomerInfo> queue1, queue2, queue3; //three queues
    private LinkedList<CustomerInfo> customerList; 

    private JButton btnUploadCustomer;

    private int cancelledCount = 0;  // Track the number of cancelled customers

    public MainDashboard() {
        // frame setup
        setTitle("CxAQS - CarMonsta x Arif Queue System (by Arif x CarMonsta)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLayout(new BorderLayout());
        setVisible(true);
        icon = new ImageIcon("kisspng-car-vehicle-icon.png");
        setIconImage(icon.getImage());

        // top panel
        topPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        topPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // creation of colored dynamic stats (text, background color, border color)
        lblCustomersLeft = createStatPanel("Customers Served: 0", new Color(176, 224, 230), Color.BLUE); //obtain from completeStack
        lblServicesPerformed = createStatPanel("Cancelled Customer: 0", new Color(255, 225, 159), Color.ORANGE); //will be obtain from completeStack
        lblTotalSales = createStatPanel("Total Sales: $0.00", Color.GREEN); //will be obtain from total cost in completedStack

        topPanel.add(lblCustomersLeft);
        topPanel.add(lblServicesPerformed);
        topPanel.add(lblTotalSales);

        // main panel with tabbed pane
        mainPanel = new JPanel(new BorderLayout());
        tabbedPane = new JTabbedPane();

        // tab 1 setup
        tab1 = new JPanel(new BorderLayout());
        table1 = new JTable(new DefaultTableModel(new Object[]{"Customer ID", "Customer Name", "Vehicle Plate No." , "No of Services", "Total Service Cost",}, 0));
        tableScrollPane1 = new JScrollPane(table1);
        tab1.add(tableScrollPane1, BorderLayout.CENTER);

        tab1ActionPanel = createTabActionPanel(table1, queue1);
        tab1.add(tab1ActionPanel, BorderLayout.SOUTH);

        // tab 2 setup
        tab2 = new JPanel(new BorderLayout());
        table2 = new JTable(new DefaultTableModel(new Object[]{"Customer ID", "Customer Name", "Vehicle Plate No." , "No of Services", "Total Service Cost",}, 0));
        tableScrollPane2 = new JScrollPane(table2);
        tab2.add(tableScrollPane2, BorderLayout.CENTER);

        tab2ActionPanel = createTabActionPanel(table2, queue2);
        tab2.add(tab2ActionPanel, BorderLayout.SOUTH);

        // tab 3 setup
        tab3 = new JPanel(new BorderLayout());        
        table3 = new JTable(new DefaultTableModel(new Object[]{"Customer ID", "Customer Name", "Vehicle Plate No." , "No of Services", "Total Service Cost",}, 0));
        tableScrollPane3 = new JScrollPane(table3);
        tab3.add(tableScrollPane3, BorderLayout.CENTER);

        tab3ActionPanel = createTabActionPanel(table3, queue3);
        tab3.add(tab3ActionPanel, BorderLayout.SOUTH);

        // add tabs to the tabbed pane
        tabbedPane.addTab("Queue 1", tab1);
        tabbedPane.addTab("Queue 2", tab2);
        tabbedPane.addTab("Queue 3", tab3);

        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        // bottom panel (date and time)
        bottomPanel = new JPanel();
        lblDateTime = new JLabel();

        JButton btnViewCompletedTransaction = new JButton("View Completed Transactions");
        btnViewCompletedTransaction.addActionListener(e -> openCompletedTransWin());

        btnUploadCustomer = new JButton("Upload Customer");

        bottomPanel.add(btnViewCompletedTransaction);
        bottomPanel.add(btnUploadCustomer);
        bottomPanel.add(lblDateTime);

        //add components to frame
        add(topPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        //update date and time dynamically
        startDateTimeUpdater();

        /////test after, put in the constructor
        processor = new Processor(); // initialize processor
        queue1 = new LinkedList<>(); // initialize queues
        queue2 = new LinkedList<>();
        queue3 = new LinkedList<>();
        customerList = new LinkedList<>(); // linked list for all customers

        //actionlistener
        btnUploadCustomer.addActionListener(e -> {
                    // load data
                    try {
                        processor.loadCustomerDataFromFile("customerlist.txt");
                        customerList = processor.getCustomerList();
                        updateQueues();
                        updateTables();
                        JOptionPane.showMessageDialog(this, "Customer data loaded successfully!");
                        System.out.println("Queue size after upload: " + queue1.size());//debug
                        System.out.println("Queue size after upload: " + queue2.size());//debug
                        System.out.println("Queue is: " + queue1);
                        System.out.println("Queue is: " + queue2);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, "Error loading customer data: " + ex.getMessage());
                    }
            });  
    }

    //Queue process
    private void updateQueues() {
        queue1.clear();
        queue2.clear();
        queue3.clear();

        for (CustomerInfo customer : customerList) {
            int serviceCount = customer.getCountOfServices();
            if (serviceCount > 3) {
                queue3.add(customer);
            } else if (queue1.size() <= queue2.size()) {
                queue1.add(customer);
            } else {
                queue2.add(customer);
            }
        }
    }

    private static Stack<CustomerInfo> completeStack = new Stack<>();

    //update jtable part1/2
    private void updateTables() {
        updateTable(table1, queue1);
        updateTable(table2, queue2);
        updateTable(table3, queue3);
    }

    // update jtable part2/2
    //method to update the JTable with customer data
    private void updateTable(JTable table, Queue<CustomerInfo> queue) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0); // Clear the existing data

        // Only display the first 5 customers from the selected queue
        int count = 0;
        for (CustomerInfo customerInfo : queue) {
            if (count >= 5) {
                break; // Stop after 5 customers
            }
            model.addRow(new Object[]{
                    customerInfo.getCustomerId(),
                    customerInfo.getCustomerName(),
                    customerInfo.getVehiclePlateNumber(),
                    customerInfo.getCountOfServices(),
                    customerInfo.calculateTotalCost()
                });
            count++;
        }
    }

    // helper method to create a stat panel
    private JLabel createStatPanel(String text, Color bgColor, Color borderColor) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setOpaque(true);
        label.setBackground(bgColor); // set background color
        label.setForeground(Color.BLACK); // text color
        label.setFont(new Font("Arial", Font.BOLD, 16));
        label.setBorder(BorderFactory.createMatteBorder(1, 5, 1, 1, borderColor)); // matte border with customizable weights and color
        return label;
    }

    private JLabel createStatPanel(String text, Color bgColor) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setOpaque(true);
        label.setBackground(bgColor); // set background color
        label.setForeground(Color.BLACK); // text color
        label.setFont(new Font("Arial", Font.BOLD, 16));
        return label;
    }

    ///stats update method
    private void updateDynamicStats() {
        lblCustomersLeft.setText("Customers Served: " + processor.getCompletedStack().size());
        lblServicesPerformed.setText("Cancelled Customers: " + cancelledCount);
        lblTotalSales.setText("Total Sales: $" + processor.calculateTotalSales());
    }

    // helper method to create a bottom panel for each tabs
    // Method to create action buttons for each tab
    private JPanel createTabActionPanel(JTable table, Queue<CustomerInfo> queue) {
        JPanel actionPanel = new JPanel();
        actionPanel.setLayout(new BorderLayout());

        // title label
        JLabel lblTitle = new JLabel("Action Button", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 16));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        actionPanel.add(lblTitle, BorderLayout.NORTH);

        // action buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton btnCreateReceipt = new JButton("Create Receipt");
        JButton btnCancelOrder = new JButton("Cancel Order");
        buttonPanel.add(btnCreateReceipt);
        buttonPanel.add(btnCancelOrder);

        actionPanel.add(buttonPanel, BorderLayout.CENTER);

        // Action listeners for buttons
        btnCreateReceipt.addActionListener(e -> processReceipt(table1, queue1)); // dynamic, Update table after processing
        btnCancelOrder.addActionListener(e -> cancelOrder(table1, queue1)); // dynamic, Update table after cancellation

        return actionPanel;
    }

    // processReceipt implementation
    private void processReceipt(JTable table, Queue<CustomerInfo> queue) {
        System.out.println("Create Receipt button clicked");
        if (queue.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No customers in this queue.");
            return;
        }

        CustomerInfo customer = queue.poll(); // remove the first customer from the queue
        completeStack.push(customer);
        try{
            processor.markCustomerAsComplete(customer); // mark as complete (this should be handled in your processor)
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error processing the receipt: " + ex.getMessage());
        }
        updateTables(); // update tables after processing the customer
        updateDynamicStats(); // update the dynamic stats

        JOptionPane.showMessageDialog(this, "Receipt created for " + customer.getCustomerName());
    }

    // cancelOrder implementation
    private void cancelOrder(JTable table, Queue<CustomerInfo> queue) {
        System.out.println("Create Receipt button clicked");
        if (queue.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No customers in this queue.");
            return;
        }
        // Prompt the user for confirmation before canceling
        int response = JOptionPane.showConfirmDialog(MainDashboard.this,
                "Are you sure you want to cancel this order?", "Confirm Cancellation",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (response == JOptionPane.YES_OPTION) {
            // Increment the cancelled customer count
            cancelledCount++;

            // Perform the cancellation logic (e.g., remove customer, etc.)
            updateTables();  // Update the tables if needed
            updateDynamicStats();  // Update dynamic stats to reflect the cancelled count

            CustomerInfo customer = queue.poll(); // remove the first customer from the queue
            // Optionally, you could push this customer to a "canceled" list or handle as needed

            updateTables(); // update tables after canceling the order
            updateDynamicStats(); // update the dynamic stats

            JOptionPane.showMessageDialog(this, "Order canceled for " + customer.getCustomerName());
        }
    }
        //end of implement Action buttons for actionpanel

        // method to dynamically update the date and time
        private void startDateTimeUpdater() {
            timer = new Timer(1000, e -> {
                        String currentDateTime = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date());
                        lblDateTime.setText(currentDateTime);
                });
            timer.start();
        }

        private void openCompletedTransWin() {
            CompletedTransactionWin completedTransWin = new CompletedTransactionWin();
            completedTransWin.setVisible(true);
        }

        public static Stack<CustomerInfo> getCompletedTransactions() {
            return completeStack; // Assuming completeStack is the Stack that stores completed transactions

        }

        ///////wiwu
        public static void main(String[] args) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        // invokeLater is for safe practice of ui building
        SwingUtilities.invokeLater(() -> {
                    new MainDashboard();
            });

        /////////all this three can give a try and error. can remove comment to give a try
        ////tokenizer
        //Processor processor = new Processor();

        //// load customer data from file
        //processor.loadCustomerDataFromFile("customerlist.txt");

        //// simulate marking a customer as complete
        //CustomerInfo completedCustomer = processor.getCustomerList().get(0);
        //processor.markCustomerAsComplete(completedCustomer);

        ////display completed transactions in backend for test
        //System.out.println("Completed Transactions: " + processor.getCompletedTransactionCount());
        //System.out.println("Total Sales: $" + processor.calculateTotalSales());
        /////////////////
    }
}