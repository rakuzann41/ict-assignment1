
package assignment1.adm.model;

public class Electronics extends Product {
    private int warrantyMonths;

    public Electronics(int id, String name, double price, int stockQuantity,
                       int warrantyMonths) {
        super(id, name, price, stockQuantity);
        this.warrantyMonths = warrantyMonths;
    }

    public int getWarrantyMonths() {
        return warrantyMonths;
    }

    public void setWarrantyMonths(int warrantyMonths) {
        if (warrantyMonths >= 0) {
            this.warrantyMonths = warrantyMonths;
        }
    }

    @Override
    public void displayInfo() {
        super.displayInfo();
        System.out.println("Warranty: " + warrantyMonths + " months");
    }
}
