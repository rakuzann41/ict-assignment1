
package assignment1.adm.model;

public class Book extends Product {
    private String author;
    private String isbn;

    public Book(int id, String name, double price, int stockQuantity,
                String author, String isbn) {
        super(id, name, price, stockQuantity);
        this.author = author;
        this.isbn = isbn;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    @Override
    public void displayInfo() {
        super.displayInfo();
        System.out.println("Author: " + author);
        System.out.println("ISBN: " + isbn);
    }
}
