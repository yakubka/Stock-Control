package project.manager;


import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;


public class OutOfStock extends JPanel {
    private final DatabaseManager db;
    private final ManagerLogic logic; 
    private final int threshold;      
    private final DefaultTableModel model;
    private final JTable table;

    public OutOfStock(DatabaseManager db, ManagerLogic logic, int threshold) {
        this.db = db;
        this.logic = logic;
        this.threshold = threshold;

        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel title = new JLabel("Low stock (quantity < " + threshold + ")", SwingConstants.LEFT);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 16f));
        add(title, BorderLayout.NORTH);

        model = new DefaultTableModel(
                new Object[]{"Product", "Quantity", "Price", "Supplier", "Category"}, 0
        ) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };

        table = new JTable(model);
        table.setRowHeight(26);
        table.setFillsViewportHeight(true);

        
        DefaultTableCellRenderer qtyRenderer = new DefaultTableCellRenderer() {
            @Override
            protected void setValue(Object value) {
                super.setValue(value);
                try {
                    int q = Integer.parseInt(String.valueOf(value));
                    if (q < threshold) {
                        setForeground(new Color(200, 30, 30));
                        setFont(getFont().deriveFont(Font.BOLD));
                    } else {
                        setForeground(Color.BLACK);
                        setFont(getFont().deriveFont(Font.PLAIN));
                    }
                } catch (NumberFormatException ignored) {
                    setForeground(Color.BLACK);
                    setFont(getFont().deriveFont(Font.PLAIN));
                }
                setHorizontalAlignment(SwingConstants.CENTER);
            }
        };
        table.getColumnModel().getColumn(1).setCellRenderer(qtyRenderer); 

        add(new JScrollPane(table), BorderLayout.CENTER);
    }

   
    public void refresh() {
        
        List<Product> low = db.getLowStock(threshold);

        model.setRowCount(0);
        for (Product p : low) {
            model.addRow(new Object[]{
                    p.getProductName(),
                    p.getQuantity(),
                    p.getPrice(),
                    p.getSupplier(),
                    p.getCategory()
            });
        }
    }
}