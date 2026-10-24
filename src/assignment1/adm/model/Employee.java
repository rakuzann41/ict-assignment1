package assignment1.adm.model;


public class Employee extends User {

    public Employee(int userId, String name, String email, String password) {
        super(userId, name, email, password, "Employee");
    }
}
