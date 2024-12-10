package project.manager; 
import java.util.List;
public class Main {
    public static void main(String[] args) {

        ManagerLogic logic = new ManagerLogic();
        DatabaseManager data = new DatabaseManager();
        ManagerGUI gui = new ManagerGUI(logic,data);
        
       
        List<Product> products = data.getProducts(); //Getting list of products from DB
       
        gui.displayProducts(products);
        gui.show(); 
}
}