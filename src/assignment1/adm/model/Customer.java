package assignment1.adm.model;


public class Customer extends User {

    public Customer(int userId, String name, String email, String password) {
        super(userId, name, email, password, "Customer");
    }
}
