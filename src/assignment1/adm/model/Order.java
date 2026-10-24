
package assignment1.adm.model;

import java.util.ArrayList;

public class Order {
    private int orderId;
    private Customer customer;
    private ArrayList<OrderItem> items;
    private double totalAmount;

    public Order(int orderId, Customer customer) {
        if (customer == null) {
            throw new IllegalArgumentException("Customer cannot be null.");
        }

        this.orderId = orderId;
        this.customer = customer;
        this.items = new ArrayList<>();
        this.totalAmount = 0.0;
    }

    public int getOrderId() {
        return orderId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public ArrayList<OrderItem> getItems() {
        return new ArrayList<>(items);
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void addItem(Product product, int quantity) {
        items.add(new OrderItem(product, quantity));
        calculateTotal();
    }

    public double calculateTotal() {
        totalAmount = 0.0;

        for (OrderItem item : items) {
            totalAmount += item.getSubtotal();
        }

        return totalAmount;
    }

    public void printReceipt() {
        System.out.println("\n========== RECEIPT ==========");
        System.out.println("Order ID: " + orderId);
        System.out.println("Customer: " + customer.getName());
        System.out.println("-----------------------------");

        for (OrderItem item : items) {
            Product product = item.getProduct();

            System.out.printf("%s x %d = $%.2f%n",
                    product.getName(),
                    item.getQuantity(),
                    item.getSubtotal());
        }

        System.out.println("-----------------------------");
        System.out.printf("Total: $%.2f%n", calculateTotal());
        System.out.println("=============================");
    }
}
