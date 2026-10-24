
package assignment1.adm.main;

import assignment1.adm.model.Book;
import assignment1.adm.model.Clothing;
import assignment1.adm.model.Customer;
import assignment1.adm.model.Electronics;
import assignment1.adm.model.Employee;
import assignment1.adm.model.Order;
import assignment1.adm.model.OrderItem;
import assignment1.adm.model.Product;
import assignment1.adm.model.User;
import assignment1.adm.service.Store;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final Store store = new Store();

    public static void main(String[] args) {
        initializeData();

        boolean running = true;

        while (running) {
            System.out.println("\n=== E-Commerce System ===");
            System.out.println("1. Log in");
            System.out.println("0. Exit");

            int choice = readInt("Choose an option: ");

            switch (choice) {
                case 1 -> login();
                case 0 -> running = false;
                default -> System.out.println("Invalid option.");
            }
        }

        System.out.println("Application closed.");
        scanner.close();
    }

    private static void initializeData() {
        store.addUser(new Employee(
                1, "Admin", "admin@store.com", "admin123"
        ));

        store.addUser(new Customer(
                2, "John", "john@example.com", "john123"
        ));

        store.addProduct(new Book(
                1, "Clean Code", 30.00, 5,
                "Robert C. Martin", "9780132350884"
        ));

        store.addProduct(new Electronics(
                2, "Headphones", 50.00, 10, 12
        ));

        store.addProduct(new Clothing(
                3, "T-Shirt", 20.00, 15,
                "M", "Black"
        ));
    }

    private static void login() {
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();

        System.out.print("Password: ");
        String password = scanner.nextLine();

        User user = store.authenticate(email, password);

        if (user == null) {
            System.out.println("Invalid email or password.");
            return;
        }

        System.out.println("Welcome, " + user.getName() + "!");

        if (user instanceof Customer customer) {
            showCustomerMenu(customer);
        } else if (user instanceof Employee employee) {
            showEmployeeMenu(employee);
        } else {
            System.out.println("Unknown user role.");
        }
    }

    private static void showCustomerMenu(Customer customer) {
        boolean loggedIn = true;

        while (loggedIn) {
            System.out.println("\n=== Customer Menu ===");
            System.out.println("1. List products");
            System.out.println("2. Place order");
            System.out.println("3. View my orders");
            System.out.println("0. Log out");

            int choice = readInt("Choose an option: ");

            switch (choice) {
                case 1 -> store.listProducts();
                case 2 -> placeCustomerOrder(customer);
                case 3 -> store.viewCustomerOrders(customer);
                case 0 -> loggedIn = false;
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private static void showEmployeeMenu(Employee employee) {
        boolean loggedIn = true;

        while (loggedIn) {
            System.out.println("\n=== Employee Menu ===");
            System.out.println("1. List products");
            System.out.println("2. Add product");
            System.out.println("3. List users");
            System.out.println("4. Add user");
            System.out.println("5. View all orders");
            System.out.println("0. Log out");

            int choice = readInt("Choose an option: ");

            switch (choice) {
                case 1 -> store.listProducts();
                case 2 -> addProduct();
                case 3 -> store.listUsers();
                case 4 -> addUser();
                case 5 -> store.viewOrders();
                case 0 -> loggedIn = false;
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private static void placeCustomerOrder(Customer customer) {
        store.listProducts();

        ArrayList<OrderItem> items = new ArrayList<>();

        while (true) {
            int productId = readInt(
                    "Enter product ID (0 to finish): "
            );

            if (productId == 0) {
                break;
            }

            Product selectedProduct = null;

            for (Product product : store.getProducts()) {
                if (product.getId() == productId) {
                    selectedProduct = product;
                    break;
                }
            }

            if (selectedProduct == null) {
                System.out.println("Product not found.");
                continue;
            }

            int quantity = readInt("Enter quantity: ");

            if (quantity <= 0) {
                System.out.println("Quantity must be positive.");
                continue;
            }

            items.add(new OrderItem(selectedProduct, quantity));
            System.out.println("Product added to order.");
        }

        if (items.isEmpty()) {
            System.out.println("Order cancelled: no products selected.");
            return;
        }

        Order order = store.placeOrder(customer, items);

        if (order != null) {
            order.printReceipt();
        }
    }

    private static void addUser() {
        System.out.println("\n=== Add User ===");
        System.out.println("1. Customer");
        System.out.println("2. Employee");

        int type = readInt("Choose user type: ");

        if (type != 1 && type != 2) {
            System.out.println("Invalid user type.");
            return;
        }

        int userId = nextUserId();

        System.out.print("Name: ");
        String name = scanner.nextLine().trim();

        System.out.print("Email: ");
        String email = scanner.nextLine().trim();

        System.out.print("Password: ");
        String password = scanner.nextLine();

        if (name.isBlank() || email.isBlank() || password.isBlank()) {
            System.out.println("Fields cannot be empty.");
            return;
        }

        User user;

        if (type == 1) {
            user = new Customer(userId, name, email, password);
        } else {
            user = new Employee(userId, name, email, password);
        }

        if (store.addUser(user)) {
            System.out.println("User added successfully. ID: " + userId);
        } else {
            System.out.println("User could not be added. Check the ID or email.");
        }
    }

    private static void addProduct() {
        System.out.println("\n=== Add Product ===");
        System.out.println("1. Book");
        System.out.println("2. Electronics");
        System.out.println("3. Clothing");

        int type = readInt("Choose product type: ");

        if (type < 1 || type > 3) {
            System.out.println("Invalid product type.");
            return;
        }

        int id = nextProductId();

        System.out.print("Name: ");
        String name = scanner.nextLine().trim();

        if (name.isBlank()) {
            System.out.println("Name cannot be empty.");
            return;
        }

        double price = readDouble("Price: ");
        int stock = readInt("Stock quantity: ");

        if (price < 0 || stock < 0) {
            System.out.println("Price and stock cannot be negative.");
            return;
        }

        Product product;

        switch (type) {
            case 1 -> {
                System.out.print("Author: ");
                String author = scanner.nextLine().trim();

                System.out.print("ISBN: ");
                String isbn = scanner.nextLine().trim();

                if (author.isBlank() || isbn.isBlank()) {
                    System.out.println("Author and ISBN cannot be empty.");
                    return;
                }

                product = new Book(
                        id, name, price, stock, author, isbn
                );
            }
            case 2 -> {
                int warranty = readInt("Warranty in months: ");

                if (warranty < 0) {
                    System.out.println("Warranty cannot be negative.");
                    return;
                }

                product = new Electronics(
                        id, name, price, stock, warranty
                );
            }
            case 3 -> {
                System.out.print("Size: ");
                String size = scanner.nextLine().trim();

                System.out.print("Color: ");
                String color = scanner.nextLine().trim();

                if (size.isBlank() || color.isBlank()) {
                    System.out.println("Size and color cannot be empty.");
                    return;
                }

                product = new Clothing(
                        id, name, price, stock, size, color
                );
            }
            default -> {
                return;
            }
        }

        if (store.addProduct(product)) {
            System.out.println("Product added successfully. ID: " + id);
        } else {
            System.out.println("Product could not be added.");
        }
    }

    private static int nextUserId() {
        int maxId = 0;

        for (User user : store.getUsers()) {
            maxId = Math.max(maxId, user.getUserId());
        }

        return maxId + 1;
    }

    private static int nextProductId() {
        int maxId = 0;

        for (Product product : store.getProducts()) {
            maxId = Math.max(maxId, product.getId());
        }

        return maxId + 1;
    }

    private static int readInt(String message) {
        while (true) {
            System.out.print(message);
            String input = scanner.nextLine().trim();

            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid integer.");
            }
        }
    }

    private static double readDouble(String message) {
        while (true) {
            System.out.print(message);
            String input = scanner.nextLine().trim();

            try {
                double value = Double.parseDouble(input);

                if (Double.isFinite(value)) {
                    return value;
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
                continue;
            }

            System.out.println("Please enter a valid finite number.");
        }
    }
}
