package project.manager;

public class Product {
    private int id;
    private String productName;
    private int quantity;
    private double price;
    private String supplier;
    private String category;

    public Product(int id, String productName, int quantity, double price, String supplier, String category) {
        this.id = id;
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
        this.supplier = supplier;
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public String getProductName() {
        return productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    public String getSupplier() {
        return supplier;
    }

    public String getCategory() {
        return category;
    }
}
