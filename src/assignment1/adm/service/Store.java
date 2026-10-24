
package assignment1.adm.service;

import assignment1.adm.model.Customer;
import assignment1.adm.model.Employee;
import assignment1.adm.model.Order;
import assignment1.adm.model.OrderItem;
import assignment1.adm.model.Product;
import assignment1.adm.model.User;

import java.util.ArrayList;

public class Store {
    private ArrayList<User> users;
    private ArrayList<Product> products;
    private ArrayList<Order> orders;

    private int nextOrderId = 1;

    public Store() {
        users = new ArrayList<>();
        products = new ArrayList<>();
        orders = new ArrayList<>();
    }

    public User authenticate(String email, String password) {
        for (User user : users) {
            if (user.getEmail().equalsIgnoreCase(email)
                    && user.getPassword().equals(password)) {
                return user;
            }
        }

        return null;
    }

    public boolean addUser(User user) {
        if (user == null || user.getEmail() == null
                || user.getEmail().isBlank()) {
            return false;
        }

        for (User existingUser : users) {
            if (existingUser.getUserId() == user.getUserId()
                    || existingUser.getEmail().equalsIgnoreCase(user.getEmail())) {
                return false;
            }
        }

        users.add(user);
        return true;
    }

    public boolean addProduct(Product product) {
        if (product == null || product.getName() == null
                || product.getName().isBlank()
                || product.getPrice() < 0
                || product.getStockQuantity() < 0) {
            return false;
        }

        for (Product existingProduct : products) {
            if (existingProduct.getId() == product.getId()) {
                return false;
            }
        }

        products.add(product);
        return true;
    }

    public void listUsers() {
        if (users.isEmpty()) {
            System.out.println("No users found.");
            return;
        }

        for (User user : users) {
            user.displayInfo();
            System.out.println("--------------------");
        }
    }

    public void listProducts() {
        if (products.isEmpty()) {
            System.out.println("No products available.");
            return;
        }

        for (Product product : products) {
            product.displayInfo();
            System.out.println("--------------------");
        }
    }

    public Product findProductById(int productId) {
        for (Product product : products) {
            if (product.getId() == productId) {
                return product;
            }
        }

        return null;
    }

    public User findUserById(int userId) {
        for (User user : users) {
            if (user.getUserId() == userId) {
                return user;
            }
        }

        return null;
    }

    public Order placeOrder(Customer customer, ArrayList<OrderItem> requestedItems) {
        if (customer == null || requestedItems == null
                || requestedItems.isEmpty()) {
            System.out.println("Invalid order.");
            return null;
        }

        User registeredUser = findUserById(customer.getUserId());

        if (!(registeredUser instanceof Customer)) {
            System.out.println("Customer is not registered.");
            return null;
        }

        ArrayList<OrderItem> validatedItems = new ArrayList<>();

        for (OrderItem requestedItem : requestedItems) {
            if (requestedItem == null) {
                System.out.println("Invalid order item.");
                return null;
            }

            Product requestedProduct = requestedItem.getProduct();
            Product storeProduct = findProductById(requestedProduct.getId());

            if (storeProduct == null
                    || requestedItem.getQuantity() <= 0) {
                System.out.println("Invalid product or quantity.");
                return null;
            }

            OrderItem existingItem = null;

            for (OrderItem item : validatedItems) {
                if (item.getProduct().getId() == storeProduct.getId()) {
                    existingItem = item;
                    break;
                }
            }

            if (existingItem == null) {
                validatedItems.add(
                        new OrderItem(storeProduct, requestedItem.getQuantity())
                );
            } else {
                existingItem.setQuantity(
                        existingItem.getQuantity() + requestedItem.getQuantity()
                );
            }
        }

        for (OrderItem item : validatedItems) {
            Product product = item.getProduct();

            if (product.getStockQuantity() < item.getQuantity()) {
                System.out.println(
                        "Not enough stock for: " + product.getName()
                );
                return null;
            }
        }

        Order order = new Order(nextOrderId, (Customer) registeredUser);

        for (OrderItem item : validatedItems) {
            Product product = item.getProduct();

            order.addItem(product, item.getQuantity());
        }

        for (OrderItem item : validatedItems) {
            Product product = item.getProduct();

            product.setStockQuantity(
                    product.getStockQuantity() - item.getQuantity()
            );
        }

        orders.add(order);
        nextOrderId++;

        System.out.println("Order placed successfully.");
        return order;
    }

    public void viewOrders() {
        if (orders.isEmpty()) {
            System.out.println("No orders found.");
            return;
        }

        for (Order order : orders) {
            order.printReceipt();
        }
    }

    public void viewCustomerOrders(Customer customer) {
        boolean found = false;

        for (Order order : orders) {
            if (order.getCustomer().getUserId() == customer.getUserId()) {
                order.printReceipt();
                found = true;
            }
        }

        if (!found) {
            System.out.println("You have no orders.");
        }
    }

    public ArrayList<Product> getProducts() {
        return new ArrayList<>(products);
    }

    public ArrayList<User> getUsers() {
        return new ArrayList<>(users);
    }

    public ArrayList<Order> getOrders() {
        return new ArrayList<>(orders);
    }
}
