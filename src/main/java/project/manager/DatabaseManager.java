package project.manager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private static final String URL = "jdbc:mariadb://localhost:3306/product_manager_db";
    private static final String USER = "yokub";
    private static final String PASSWORD = "True7";

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public void saveProduct(String productName, int quantity, double price, String supplier, String category) {
        String query = "INSERT INTO products (product_name, quantity, price, supplier, category) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, productName);
            preparedStatement.setInt(2, quantity);
            preparedStatement.setDouble(3, price);
            preparedStatement.setString(4, supplier);
            preparedStatement.setString(5, category);
            preparedStatement.executeUpdate();

            
            saveCategory(category);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateProduct(int id, String productName, int quantity, double price, String supplier, String category) {
        String query = "UPDATE products SET product_name=?, quantity=?, price=?, supplier=?, category=? WHERE id=?";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, productName);
            preparedStatement.setInt(2, quantity);
            preparedStatement.setDouble(3, price);
            preparedStatement.setString(4, supplier);
            preparedStatement.setString(5, category);
            preparedStatement.setInt(6, id);

            preparedStatement.executeUpdate();
            saveCategory(category);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteProduct(int id) {
        String query = "DELETE FROM products WHERE id=?";
        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteCategory(String category) {
       
        String deleteProductsQuery = "DELETE FROM products WHERE category=?";
        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(deleteProductsQuery)) {
            ps.setString(1, category);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Deleting categories from category table 
        
        String deleteCategoryQuery = "DELETE FROM categories WHERE category_name=?";
        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(deleteCategoryQuery)) {
            ps.setString(1, category);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Product> getProducts() {
        List<Product> productList = new ArrayList<>();
        String query = "SELECT * FROM products";

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(query)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("product_name");
                int qty = rs.getInt("quantity");
                double price = rs.getDouble("price");
                String supplier = rs.getString("supplier");
                String category = rs.getString("category");

                Product p = new Product(id, name, qty, price, supplier, category);
                productList.add(p);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return productList;
    }

    public List<String> getAllCategories() {
        List<String> categories = new ArrayList<>();
        String query = "SELECT category_name FROM categories ORDER BY category_name";
        try (Connection connection = getConnection();
             Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(query)) {
            while (rs.next()) {
                categories.add(rs.getString("category_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories;
    }

    public void saveCategory(String category) {
        if (category == null || category.trim().isEmpty()) return;
        String query = "INSERT IGNORE INTO categories (category_name) VALUES (?)";
        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, category.trim());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
