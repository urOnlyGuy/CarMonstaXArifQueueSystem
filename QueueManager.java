/**
 * by Aqil
 */

import java.util.LinkedList;
import java.util.Queue;

public class QueueManager {
    private Queue<CustomerInfo> queue1; //for cust with <= 3 services
    private Queue<CustomerInfo> queue2; //for cust with <= 3 services
    private Queue<CustomerInfo> queue3; //for cust with > 3 services
    private static final int MAX_SIZE = 5; //maximum size of each queue

    //constructor
    public QueueManager() {
        this.queue1 = new LinkedList<>();
        this.queue2 = new LinkedList<>();
        this.queue3 = new LinkedList<>();
    }

    //  method to add a customer to the appropriate queue
    public boolean enqueue(CustomerInfo customer) {
        int numServices = customer.getServices().size();

        if (numServices > 3) {
            //customers with more than 3 services go to queue3
            if (queue3.size() < MAX_SIZE) {
                return queue3.offer(customer);
            } else {
                System.out.println("Queue 3 is full!");
                return false;
            }
        } else {
            // customers with 3 or fewer services go to queue1 or queue2
            if (queue1.size() < MAX_SIZE) {
                return queue1.offer(customer);
            } else if (queue2.size() < MAX_SIZE) {
                return queue2.offer(customer);
            } else {
                System.out.println("Queue 1 and Queue 2 are currently full!");
                return false;
            }
        }
    }

    // Method to dequeue a customer from a specific queue
    public CustomerInfo dequeue(int queueNumber) {
        switch (queueNumber) {
            case 1:
                return queue1.poll();
            case 2:
                return queue2.poll();
            case 3:
                return queue3.poll();
            default:
                System.out.println("Invalid queue number!");
                return null;
        }
    }

    // method to peek at the first customer in a specific queue
    public CustomerInfo peek(int queueNumber) {
        switch (queueNumber) {
            case 1:
                return queue1.peek();
            case 2:
                return queue2.peek();
            case 3:
                return queue3.peek();
            default:
                System.out.println("Invalid queue number!");
                return null;
        }
    }

    // get the size of a specific queue
    public int getQueueSize(int queueNumber) {
        switch (queueNumber) {
            case 1:
                return queue1.size();
            case 2:
                return queue2.size();
            case 3:
                return queue3.size();
            default:
                System.out.println("Invalid queue number!");
                return -1;
        }
    }

    // check if a specific queue is empty
    public boolean isQueueEmpty(int queueNumber) {
        switch (queueNumber) {
            case 1:
                return queue1.isEmpty();
            case 2:
                return queue2.isEmpty();
            case 3:
                return queue3.isEmpty();
            default:
                System.out.println("Invalid queue number!");
                return true;
        }
    }
}
