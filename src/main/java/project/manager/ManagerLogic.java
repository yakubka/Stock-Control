package project.manager;

import java.text.DecimalFormat;

import javax.swing.JLabel;

public class ManagerLogic {
    public void updateTotalPrice(String quantityText, String priceText, JLabel totalPriceLabel) {
        try {
            double quantity = Double.parseDouble(quantityText.trim());
            double price = Double.parseDouble(priceText.trim());
            double totalPrice = quantity * price;

            DecimalFormat formatter = new DecimalFormat("#,###");
            totalPriceLabel.setText(formatter.format(totalPrice));
        } catch (NumberFormatException e) {
            totalPriceLabel.setText("0");
        }
    }
}
