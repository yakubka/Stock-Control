package project.manager;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class ManagerGUI {

    private JFrame frame;
    private JPanel inputPanel;
    private ManagerLogic logic;
    public JScrollPane scroll;
    private DatabaseManager dbManager;
    private List<RowData> rowDataList = new ArrayList<>();

    private static class RowData {
        int id;
        JTextField productName;
        JTextField quantity;
        JTextField price;
        JLabel totalPrice;
        JTextField supplier;
        JComboBox<String> category;
        JButton deleteButton;

        RowData(int id, JTextField productName, JTextField quantity, JTextField price, JLabel totalPrice,
                JTextField supplier, JComboBox<String> category, JButton deleteButton) {
            this.id = id;
            this.productName = productName;
            this.quantity = quantity;
            this.price = price;
            this.totalPrice = totalPrice;
            this.supplier = supplier;
            this.category = category;
            this.deleteButton = deleteButton;
        }
    }

    public ManagerGUI(ManagerLogic logic, DatabaseManager data) {
        this.logic = logic;
        this.dbManager = data;

        frame = new JFrame("Product Manager");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 600);
        frame.setLayout(new BorderLayout());

        inputPanel = new JPanel();

        inputPanel.setLayout(new GridLayout(0, 7));
        scroll = new JScrollPane(inputPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        frame.add(scroll, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        frame.add(buttonPanel, BorderLayout.NORTH);

        JButton addProductButton = new JButton("Add New Product");
        addProductButton.setBackground(new Color(51, 102, 255)); // Brighter Blue
        addProductButton.setForeground(Color.WHITE);
        addProductButton.addActionListener(e -> addNewRow());
        buttonPanel.add(addProductButton);

        JButton addCategoryButton = new JButton("Add New Category");
        addCategoryButton.setBackground(new Color(51, 102, 255)); // Brighter Blue
        addCategoryButton.setForeground(Color.WHITE);
        addCategoryButton.addActionListener(e -> addNewCategory());
        buttonPanel.add(addCategoryButton);

        JButton deleteCategoryButton = new JButton("Delete Category");
        deleteCategoryButton.setBackground(new Color(204, 51, 51)); // Softer Red
        deleteCategoryButton.setForeground(Color.WHITE);
        deleteCategoryButton.addActionListener(e -> deleteCategory());
        buttonPanel.add(deleteCategoryButton);

        JButton searchCategoryButton = new JButton("Search by Category");
        searchCategoryButton.setBackground(new Color(51, 102, 255)); // Brighter Blue
        searchCategoryButton.setForeground(Color.WHITE);
        searchCategoryButton.addActionListener(e -> filterByCategory());
        buttonPanel.add(searchCategoryButton);

        JButton showAllButton = new JButton("Show All Products");
        showAllButton.setBackground(new Color(51, 102, 255)); // Brighter Blue
        showAllButton.setForeground(Color.WHITE);
        showAllButton.addActionListener(e -> showAllProducts());
        buttonPanel.add(showAllButton);

        JButton saveChangesButton = new JButton("Save Changes");
        saveChangesButton.setBackground(new Color(102, 204, 102)); // Softer Green
        saveChangesButton.setForeground(Color.WHITE);
        saveChangesButton.addActionListener(e -> saveAllChanges());
        buttonPanel.add(saveChangesButton);

        frame.setVisible(true);
    }

    public void show() {
        frame.setVisible(true);
    }

    private void addColumnHeaders() {
        String[] headers = {"Product Name", "Quantity", "Price", "Total Price", "Supplier", "Category", "Delete"};
        for (String header : headers) {
            JLabel label = new JLabel(header, SwingConstants.CENTER);
            label.setOpaque(true);
            label.setBackground(Color.LIGHT_GRAY);
            inputPanel.add(label);
        }
    }

    private void addNewRow() {

        // Getting categories from DB

        List<String> categoriesFromDB = dbManager.getAllCategories();
        if (categoriesFromDB.isEmpty()) {
            categoriesFromDB.add("Milk");
            categoriesFromDB.add("Beverages");
        }
        String[] categoryArray = categoriesFromDB.toArray(new String[0]);

        JTextField productName = new JTextField(20);
        JTextField quantity = new JTextField(20);
        JTextField price = new JTextField(20);
        JLabel totalPrice = new JLabel("0", SwingConstants.CENTER);
        totalPrice.setOpaque(true);
        totalPrice.setBackground(Color.WHITE);
        JTextField supplier = new JTextField(20);
        JComboBox<String> category = new JComboBox<>(categoryArray);

        // totalPrice changes if quantity or price changed
        quantity.getDocument().addDocumentListener(new SimpleDocumentListener() {
            @Override
            public void update(DocumentEvent e) {
                logic.updateTotalPrice(quantity.getText(), price.getText(), totalPrice);
            }
        });
        price.getDocument().addDocumentListener(new SimpleDocumentListener() {
            @Override
            public void update(DocumentEvent e) {
                logic.updateTotalPrice(quantity.getText(), price.getText(), totalPrice);
            }
        });

        JButton deleteButton = new JButton("Delete");
        deleteButton.setBackground(new Color(204, 51, 51)); 
        deleteButton.setForeground(Color.WHITE);
        //ImageIcon trashIcon = new ImageIcon("project-manager/src/main/java/project/manager/trash.png");
        //deleteButton.setIcon(trashIcon);
        deleteButton.setEnabled(false);

        inputPanel.add(productName);
        inputPanel.add(quantity);
        inputPanel.add(price);
        inputPanel.add(totalPrice);
        inputPanel.add(supplier);
        inputPanel.add(category);
        inputPanel.add(deleteButton);

        RowData rd = new RowData(-1, productName, quantity, price, totalPrice, supplier, category, deleteButton);
        rowDataList.add(rd);

        inputPanel.revalidate();
        inputPanel.repaint();
    }

    private void addNewCategory() {
        String newCategory = JOptionPane.showInputDialog(frame, "Enter new category:", "Add Category", JOptionPane.PLAIN_MESSAGE);

        if (newCategory != null && !newCategory.trim().isEmpty()) {
            dbManager.saveCategory(newCategory.trim());
            JOptionPane.showMessageDialog(frame, "Category added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void showAllProducts() {
        for (Component c : inputPanel.getComponents()) {
            c.setVisible(true);
        }
        inputPanel.revalidate();
        inputPanel.repaint();
    }

    private void filterByCategory() {
        String categoryToFilter = JOptionPane.showInputDialog(frame, "Enter category to filter:", "Filter by Category", JOptionPane.PLAIN_MESSAGE);

        if (categoryToFilter != null && !categoryToFilter.trim().isEmpty()) {
            
            for (int i = 7; i < inputPanel.getComponentCount(); i += 7) {
                JComboBox<String> categoryBox = (JComboBox<String>) inputPanel.getComponent(i + 5);
                String currentCategory = (String) categoryBox.getSelectedItem();
                boolean visible = currentCategory.equalsIgnoreCase(categoryToFilter.trim());

                for (int j = 0; j < 7; j++) {
                    inputPanel.getComponent(i + j).setVisible(visible);
                }
            }
            inputPanel.revalidate();
            inputPanel.repaint();
        }
    }

    public void displayProducts(List<Product> products) {
        inputPanel.removeAll();
        addColumnHeaders();

        List<String> categoriesFromDB = dbManager.getAllCategories();
        if (categoriesFromDB.isEmpty()) {
            categoriesFromDB.add("Milk");
            categoriesFromDB.add("Beverages");
        }
        String[] categoryArray = categoriesFromDB.toArray(new String[0]);

        rowDataList.clear();

        for (Product p : products) {
            JTextField productName = new JTextField(p.getProductName(), 20);
            JTextField quantity = new JTextField(String.valueOf(p.getQuantity()), 20);
            JTextField price = new JTextField(String.valueOf(p.getPrice()), 20);

            double total = p.getQuantity() * p.getPrice();
            JLabel totalPrice = new JLabel(String.valueOf(total), SwingConstants.CENTER);
            totalPrice.setOpaque(true);
            totalPrice.setBackground(Color.WHITE);

            JTextField supplier = new JTextField(p.getSupplier(), 20);

            JComboBox<String> category = new JComboBox<>(categoryArray);
            category.setSelectedItem(p.getCategory());

            JButton deleteButton = new JButton("Delete");
            deleteButton.setBackground(new Color(204, 51, 51)); 
            deleteButton.setForeground(Color.WHITE);
            deleteButton.addActionListener(e -> {
                
                dbManager.deleteProduct(p.getId());

                displayProducts(dbManager.getProducts());
            });

            inputPanel.add(productName);
            inputPanel.add(quantity);
            inputPanel.add(price);
            inputPanel.add(totalPrice);
            inputPanel.add(supplier);
            inputPanel.add(category);
            inputPanel.add(deleteButton);

            RowData rd = new RowData(p.getId(), productName, quantity, price, totalPrice, supplier, category, deleteButton);
            rowDataList.add(rd);
        }

        inputPanel.revalidate();
        inputPanel.repaint();
    }

    private void deleteCategory() {
        String categoryToDelete = JOptionPane.showInputDialog(frame,
                "Enter category to delete (all products under this category will be deleted):",
                "Delete Category", JOptionPane.WARNING_MESSAGE);

        if (categoryToDelete != null && !categoryToDelete.trim().isEmpty()) {
            int confirm = JOptionPane.showConfirmDialog(
                    frame,
                    "All products under the category \"" + categoryToDelete + "\" will be deleted. Are you sure?",
                    "Confirm Deletion",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

            if (confirm == JOptionPane.YES_OPTION) {
                dbManager.deleteCategory(categoryToDelete.trim());
                displayProducts(dbManager.getProducts());
                JOptionPane.showMessageDialog(frame,
                        "Category \"" + categoryToDelete + "\" and all related products have been deleted.",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private void saveAllChanges() {
        for (RowData rd : rowDataList) {
            try {
                String name = rd.productName.getText().trim();
                int qty = Integer.parseInt(rd.quantity.getText().trim());
                double prc = Double.parseDouble(rd.price.getText().trim());
                String supp = rd.supplier.getText().trim();
                String cat = rd.category.getSelectedItem().toString();

                if (rd.id < 0) {

                    dbManager.saveProduct(name, qty, prc, supp, cat);
                } else {
                    dbManager.updateProduct(rd.id, name, qty, prc, supp, cat);
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        List<Product> updatedProducts = dbManager.getProducts();
        displayProducts(updatedProducts);

        JOptionPane.showMessageDialog(frame, "All changes saved successfully!");
    }

    // Simple Document Listener for totalPrice

    abstract class SimpleDocumentListener implements DocumentListener {
        public abstract void update(DocumentEvent e);

        @Override
        public void insertUpdate(DocumentEvent e) {
            update(e);
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            update(e);
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            update(e);
        }
    }
}
