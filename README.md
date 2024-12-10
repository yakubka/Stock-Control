Product Manager

Product Manager is a Java-based desktop application designed to help users efficiently manage a catalog of products and their categories. Leveraging Java Swing for the graphical user interface (GUI) and MariaDB for persistent data storage, this application offers a user-friendly platform for performing CRUD (Create, Read, Update, Delete) operations on products and categories.
Table of Contents

    Features
    Technologies Used
    Project Structure
    Installation
    Usage
    Database Setup
    Key Functionalities
    Data Structures & Algorithms
    Troubleshooting
    Contributing
    License

Features

    CRUD Operations: Easily create, read, update, and delete products and categories.
    Category Management: Add new categories, filter products by category, and delete categories along with all associated products.
    Real-Time Updates: Changes to product quantities or prices are reflected instantly without needing to restart the application.
    User-Friendly Interface: Intuitive GUI built with Java Swing, featuring color-coded buttons and confirmation dialogs to enhance user experience.
    Data Integrity: Ensures unique categories and validates user inputs to maintain consistent and accurate data.

Technologies Used

    Programming Language: Java
    GUI Framework: Java Swing
    Database: MariaDB
    Database Connectivity: JDBC (Java Database Connectivity)
    Version Control: Git & GitHub

Project Structure

product-manager/
│
├── project-manager/
│   └── src/
│        ├──claer/
|        ├──test/
|        └──main/
|             ├──resources/
|             └──java/
|                  └──project/
|                         └──manager/
|                                    ├── Main.java
│                                    ├── ManagerGUI.java
│                                    ├── ManagerLogic.java
│                                    ├── DatabaseManager.java
│                                    └── Product.java
│
├── target/
│   └── Maven Build
│
├──pom.xml
│
├── README.md
└── .gitignore

    Main.java: Entry point of the application. Initializes core components and launches the GUI.
    ManagerGUI.java: Handles the graphical user interface, displaying products and managing user interactions.
    ManagerLogic.java: Contains business logic, such as calculating total prices based on quantity and price inputs.
    DatabaseManager.java: Manages all interactions with the MariaDB database, including executing SQL queries.
    Product.java: Model class representing a product entity with attributes like ID, name, quantity, price, supplier, and category.
    resources/trash.png: Icon image used for the delete buttons in the GUI.
    sql/setup.sql: SQL script to set up the required database and tables.
    README.md: Project documentation.
    .gitignore: Specifies intentionally untracked files to ignore.

Installation

    Clone the Repository:

git clone https://github.com/yourusername/product-manager.git
cd product-manager

Set Up the Database:

    Ensure MariaDB is installed on your machine.

    Open the MariaDB command-line interface and execute the SQL script located at sql/setup.sql:

    mysql -u root -p < sql/setup.sql

    This will create the product_manager_db database along with the necessary tables.

Configure Database Credentials:

    Open DatabaseManager.java located in src/project/manager/.

    Update the USER and PASSWORD constants with your MariaDB credentials:

    private static final String USER = "your_username";
    private static final String PASSWORD = "your_password";

Compile and Run the Application:

    Navigate to the src directory:

cd src

Compile the Java files:

javac project/manager/*.java

Run the application:

        java project.manager.Main

Usage

Upon launching the application, you will be presented with a user-friendly interface displaying a table of products. Here's how to navigate and use the various features:

    Add New Product:
        Click the "Add New Product" button.
        An empty row will appear where you can enter the product name, quantity, price, supplier, and select a category from the dropdown.
        After entering the details, click "Save Changes" to persist the new product to the database.

    Add New Category:
        Click the "Add New Category" button.
        Enter the name of the new category in the prompted dialog.
        The new category will be saved and available for selection when adding or editing products.

    Delete Category:
        Click the "Delete Category" button.
        Enter the name of the category you wish to delete.
        Confirm the deletion. This action will remove the category and all associated products from the database.

    Search by Category:
        Click the "Search by Category" button.
        Enter the category name to filter the displayed products.
        Only products belonging to the specified category will be visible. Click "Show All Products" to remove the filter.

    Delete Individual Product:
        Each product row has a "Delete" button (trash icon).
        Click the button to remove that specific product from the database after confirmation.

    Save Changes:
        After making any additions or edits, click "Save Changes" to commit all modifications to the database.
        The interface will refresh to display the updated list of products.

Database Setup

Execute the following SQL commands to set up the required database and tables:

CREATE DATABASE product_manager_db;

USE product_manager_db;

CREATE TABLE products (
    id INT AUTO_INCREMENT PRIMARY KEY,
    product_name VARCHAR(255) NOT NULL,
    quantity INT NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    supplier VARCHAR(255),
    category VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS categories (
    id INT AUTO_INCREMENT PRIMARY KEY,
    category_name VARCHAR(255) UNIQUE
);

    products Table:
        Stores product details including name, quantity, price, supplier, and category.
    categories Table:
        Stores unique category names to ensure consistency and prevent duplicates.

Key Functionalities
1. Fetching Data from the Database

    Method: DatabaseManager.getProducts()
    Description: Retrieves all products from the products table and returns them as a List<Product>.
    Usage in Main.java:

    List<Product> products = data.getProducts();
    gui.displayProducts(products);

2. Displaying Products in the GUI

    Method: ManagerGUI.displayProducts(List<Product> products)
    Description: Iterates over the list of products, creates UI components for each, and adds them to the inputPanel.
    Key Points:
        Dynamically loads categories from the categories table.
        Adds a "Delete" button for each product with an action listener to remove it.

3. Adding a New Product

    Method: ManagerGUI.addNewRow()
    Description: Adds a new, editable row in the GUI for inputting product details.
    Key Points:
        Initializes UI components for the new product.
        Disables the "Delete" button since the product isn't saved yet.
        Adds DocumentListener to update the total price in real-time.

4. Saving Changes

    Method: ManagerGUI.saveAllChanges()
    Description: Iterates over all product rows, saving new products and updating existing ones in the database.
    Key Points:
        Differentiates between new and existing products based on the id.
        Handles input validation and error reporting.
        Refreshes the GUI to display updated data.

5. Adding and Deleting Categories

    Adding:
        Method: ManagerGUI.addNewCategory()
        Description: Prompts the user to enter a new category and saves it to the database.
    Deleting:
        Method: ManagerGUI.deleteCategory()
        Description: Prompts the user to enter a category to delete, removes the category and all associated products, and refreshes the GUI.

6. Deleting a Product

    Component: JButton deleteButton in each RowData
    Description: Each product row has a "Delete" button that, when clicked, removes that specific product from the database after user confirmation.
    Key Points:
        Triggers dbManager.deleteProduct(p.getId()).
        Refreshes the GUI to reflect the deletion.

7. Real-Time Total Price Calculation

    Component: DocumentListener attached to quantity and price fields
    Description: Automatically updates the "Total Price" label whenever the quantity or price fields are modified.
    Key Points:
        Calls ManagerLogic.updateTotalPrice(quantity.getText(), price.getText(), totalPrice).

Data Structures & Algorithms
1. Collections

    List<Product>
        Used to store and manage the list of products fetched from the database.
        Facilitates easy iteration and manipulation of product data.
    List<RowData> rowDataList
        Maintains references to all UI components representing each product row.
        Enables efficient saving and updating of product data.

2. Sets

    HashSet<String> (Conceptual)
        Although not explicitly used in the current implementation, HashSet can be beneficial for gathering unique categories from products before the categories table was introduced.

3. Listeners

    DocumentListener
        Listens for changes in the quantity and price text fields and updates the "Total Price" label automatically.

4. MVC Architecture (Model-View-Controller)

    Model
        Represented by Product and database interactions in DatabaseManager.
    View
        Handled by ManagerGUI, which presents data and captures user interactions.
    Controller
        Managed by ManagerLogic, which processes data and updates the view accordingly.

Troubleshooting
1. Access Denied for Database User

Error Message:

ERROR 1044 (42000): Access denied for user 'yokub'@'localhost' to database 'product_manager'

Cause:

    The user yokub does not have the necessary privileges to access the product_manager_db database.

Solution:

    Log in as Root or Admin User:

mysql -u root -p

Grant Privileges:

GRANT ALL PRIVILEGES ON product_manager_db.* TO 'yokub'@'localhost' IDENTIFIED BY 'True7';
FLUSH PRIVILEGES;

    Replace product_manager_db with your actual database name if different.

Verify Access:

    mysql -u yokub -p
    USE product_manager_db;

        Ensure you can switch to the database without errors.

2. Categories Not Persisting

Problem:

    Newly added categories disappear after restarting the application.

Cause:

    Categories were not being saved to the categories table, or the GUI was not fetching updated categories from the database.

Solution:

    Ensure that every time a new category is added via addNewCategory(), it's saved to the categories table using dbManager.saveCategory(newCategory).
    When displaying products or adding new rows, fetch the latest categories from the categories table to populate the JComboBox.

3. Real-Time Updates Not Reflecting

Problem:

    Changes to quantity or price are only visible after saving and restarting the application.

Cause:

    The DocumentListener was not correctly updating the totalPrice label in real-time.
    The GUI was not refreshing immediately after changes.

Solution:

    Ensure that DocumentListener is properly attached to both quantity and price fields and that ManagerLogic.updateTotalPrice() is being called upon any change.
    After saving changes via saveAllChanges(), call displayProducts() to refresh the GUI with updated data.

4. Delete Operations Not Working as Expected

Problem:

    Deleting a product or category doesn't remove it from the database or the GUI.

Cause:

    The action listeners for delete buttons may not be correctly implemented.
    Database operations in DatabaseManager may not be executing successfully.

Solution:

    Verify that dbManager.deleteProduct(id) and dbManager.deleteCategory(category) are correctly implemented and successfully deleting records from the database.
    Ensure that after deletion, displayProducts(dbManager.getProducts()) is called to refresh the GUI.

Contributing

Contributions are welcome! Please follow these steps to contribute to the project:

    Fork the Repository

    Create a Feature Branch

git checkout -b feature/YourFeatureName

Commit Your Changes

git commit -m "Add some feature"

Push to the Branch

    git push origin feature/YourFeatureName

    Open a Pull Request

Please ensure your contributions adhere to the project's coding standards and include relevant documentation.
License

This project is licensed under the MIT License.
