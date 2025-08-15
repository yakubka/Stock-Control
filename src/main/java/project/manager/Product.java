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

    
    public Product(String productName, int quantity, double price, String supplier, String category) {
        this(0, productName, quantity, price, supplier, category);
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

    
    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", productName='" + productName + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                ", supplier='" + supplier + '\'' +
                ", category='" + category + '\'' +
                '}';
    }
}