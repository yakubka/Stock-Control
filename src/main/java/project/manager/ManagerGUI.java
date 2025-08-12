package project.manager;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class ManagerGUI {

    private JFrame frame;
    private JPanel inputPanel;
    private JPanel buttonPanel; // поле панели кнопок, чтобы было видно во всём классе

    // === CardLayout «роутер» и страницы ===
    private CardLayout cardLayout;
    private JPanel root;         // корневой контейнер со страницами
    private JPanel pageMain;     // главная страница с таблицей/формой
    private JPanel pageOut;      // страница Out of Stock
    private JPanel pagePopular;  // страница Popular

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

        frame = new JFrame("Stock Control");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
        frame.setSize(900, 600);
        inputPanel = new JPanel();

        // ====== CardLayout и страницы ======
        cardLayout = new CardLayout();
        root = new JPanel(cardLayout);

        // --- страница MAIN (твоя текущая разметка) ---
        pageMain = new JPanel(new BorderLayout());

        // верхняя панель кнопок (с анимацией)
        buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        AnimatedButton Out_of_StockButton = new AnimatedButton("Out of Stock");
        Out_of_StockButton.addActionListener(e -> {
            // переход на страницу «Out of Stock»
            // TODO: подгрузить/обновить данные для этой страницы при необходимости
            cardLayout.show(root, "out");
        });
        buttonPanel.add(Out_of_StockButton);

        AnimatedButton PopularButton = new AnimatedButton("Popular");
        PopularButton.addActionListener(e -> {
            // переход на страницу «Popular»
            // TODO: добавить фильтры/сортировку под популярные позиции
            cardLayout.show(root, "popular");
        });
        buttonPanel.add(PopularButton);

        // Кнопка поиска с иконкой (иконку подставь по своему пути или из ресурсов)
        JButton searchButton = new JButton();
        searchButton.setIcon(new ImageIcon("search.png")); // при желании заменить на ресурс getResource("/icons/search.png")
        searchButton.setBorderPainted(true);
        searchButton.setContentAreaFilled(true);
        searchButton.setFocusPainted(true);
        searchButton.setBackground(new Color(51, 102, 255));
        searchButton.setForeground(Color.WHITE);
        // При клике можно переходить на страницу результатов или открывать поиск
        searchButton.addActionListener(e -> {
            // TODO: здесь логика поиска/перехода
            cardLayout.show(root, "popular"); // пример
        });
        buttonPanel.add(searchButton);

        // центральная часть — скролл с inputPanel (таблица/строки)
        inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(0, 7));
        scroll = new JScrollPane(
            inputPanel,
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
            JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
        );

        pageMain.add(buttonPanel, BorderLayout.NORTH);
        pageMain.add(scroll, BorderLayout.CENTER);

        // --- страница OUT OF STOCK ---
        pageOut = new JPanel(new BorderLayout());
        pageOut.add(new JLabel("Out of Stock page", SwingConstants.CENTER), BorderLayout.CENTER);
        AnimatedButton backFromOut = new AnimatedButton("Back");
        backFromOut.addActionListener(e -> cardLayout.show(root, "main"));
        JPanel outBottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        outBottom.add(backFromOut);
        pageOut.add(outBottom, BorderLayout.SOUTH);

        // --- страница POPULAR ---
        pagePopular = new JPanel(new BorderLayout());
        pagePopular.add(new JLabel("Popular page", SwingConstants.CENTER), BorderLayout.CENTER);
        AnimatedButton backFromPopular = new AnimatedButton("Back");
        backFromPopular.addActionListener(e -> cardLayout.show(root, "main"));
        JPanel popularBottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        popularBottom.add(backFromPopular);
        pagePopular.add(popularBottom, BorderLayout.SOUTH);

        // Регистрируем страницы в CardLayout
        root.add(pageMain, "main");
        root.add(pageOut, "out");
        root.add(pagePopular, "popular");

        // ставим «роутер» в окно и показываем MAIN
        frame.setContentPane(root);
        cardLayout.show(root, "main");

        frame.setLocationRelativeTo(null);
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
        // deleteButton.setIcon(new ImageIcon("project-manager/src/main/java/project/manager/trash.png"));
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
                @SuppressWarnings("unchecked")
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
        public void insertUpdate(DocumentEvent e) { update(e); }
        @Override
        public void removeUpdate(DocumentEvent e) { update(e); }
        @Override
        public void changedUpdate(DocumentEvent e) { update(e); }
    }

    // ===== Анимированная кнопка (hover: плавный цвет и скругление) =====
    static class AnimatedButton extends JButton {
        private final Timer timer;
        private float t = 0f;      // текущее значение анимации 0..1
        private float target = 0f; // целевое значение (0 — обычная, 1 — hover)

        private final Color base = new Color(51, 102, 255);
        private final Color hover = new Color(70, 140, 255);

        AnimatedButton(String text) {
            super(text);
            setOpaque(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setForeground(Color.WHITE);
            setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR));
            setMargin(new Insets(10, 16, 10, 16));

            timer = new Timer(16, e -> step());
            timer.setCoalesce(true);

            // наведение мыши — старт/остановка анимации
            addMouseListener(new MouseAdapter() {
                @Override public void mouseEntered(MouseEvent e) { target = 1f; timer.start(); }
                @Override public void mouseExited (MouseEvent e) { target = 0f; timer.start(); }
                @Override public void mousePressed(MouseEvent e)  { target = 1f; }
                @Override public void mouseReleased(MouseEvent e) { target = getBounds().contains(e.getPoint()) ? 1f : 0f; }
            });
        }

        private void step() {
            // плавный переход t -> target
            float speed = 0.15f;
            t += (target - t) * speed;
            if (Math.abs(target - t) < 0.01f) {
                t = target;
                timer.stop();
            }
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // интерполяция цвета и радиуса углов
            Color bg = mix(base, hover, t);
            int arc = (int) (12 + 8 * t);

           
            g2.setColor(bg);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), arc, arc);
          
            super.paintComponent(g2);
            g2.dispose();
        }

        @Override
        public void updateUI() {
            super.updateUI();
            
            setContentAreaFilled(false);
        }

        private static Color mix(Color a, Color b, float t) {
            t = Math.max(0f, Math.min(1f, t));
            int r = (int) (a.getRed()   + (b.getRed()   - a.getRed())   * t);
            int g = (int) (a.getGreen() + (b.getGreen() - a.getGreen()) * t);
            int bl= (int) (a.getBlue()  + (b.getBlue()  - a.getBlue())  * t);
            return new Color(r, g, bl);
        }
    }
}
