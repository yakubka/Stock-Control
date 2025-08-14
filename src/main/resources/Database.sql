
-- Host: localhost    Database: product_manager_db
-- ------------------------------------------------------
-- Server version	10.11.13-MariaDB-0+deb12u1



DROP TABLE IF EXISTS `categories`;

CREATE TABLE `categories` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `category_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `category_name` (`category_name`)
) ENGINE=InnoDB AUTO_INCREMENT=85 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


LOCK TABLES `categories` WRITE;

INSERT INTO `categories` VALUES
(82,'Beverages'),
(8,'Dairy'),
(2,'Fish'),
(16,'Fruit'),
(3,'Grain'),
(75,'hello'),
(67,'Kimchi'),
(7,'Meat'),
(1,'Milk'),
(83,'Snacks'),
(6,'Sweeteners'),
(4,'Vegetables');
UNLOCK TABLES;

DROP TABLE IF EXISTS `products`;

CREATE TABLE `products` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `product_name` varchar(255) NOT NULL,
  `quantity` int(11) NOT NULL,
  `price` decimal(10,2) NOT NULL,
  `supplier` varchar(255) DEFAULT NULL,
  `category` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


LOCK TABLES `products` WRITE;

INSERT INTO `products` VALUES
(14,'Whole Milk 1L',18,1.49,'Dairy Co','Milk'),
(15,'Skimmed Milk 1L',25,1.39,'Dairy Co','Milk'),
(16,'Chocolate Milk 0.5L',12,1.19,'SweetDairy','Milk'),
(17,'Greek Yogurt 150g',30,0.89,'Yogurt Farm','Milk'),
(18,'Cottage Cheese 200g',9,1.59,'Curd Masters','Milk'),
(19,'Butter 200g',14,2.49,'Creamery Ltd','Milk'),
(20,'Cheddar Cheese 250g',6,3.79,'CheeseWorks','Milk'),
(21,'Mozzarella 200g',0,2.99,'CheeseWorks','Milk'),
(22,'Sour Cream 200g',20,1.09,'Yogurt Farm','Milk'),
(23,'Orange Juice 1L',10,2.20,'Fresh Drinks','Beverages'),
(24,'Apple Juice 1L',8,2.10,'Fresh Drinks','Beverages'),
(25,'Sparkling Water 1L',40,0.79,'BlueSpring','Beverages'),
(26,'Still Water 1.5L',35,0.69,'BlueSpring','Beverages'),
(27,'Cola 0.5L',22,1.00,'Fizzy Corp','Beverages'),
(28,'Lemonade 0.5L',15,0.95,'Fizzy Corp','Beverages'),
(29,'Energy Drink 0.25L',4,1.59,'Boost Ltd','Beverages'),
(30,'Green Tea (bottled) 0.5L',0,1.29,'TeaHouse','Beverages'),
(31,'Iced Coffee 0.33L',7,1.49,'Bean&Bottle','Beverages'),
(32,'Potato Chips 150g',50,1.29,'Snacky','Snacks'),
(33,'Tortilla Chips 200g',12,1.79,'Snacky','Snacks'),
(34,'Salted Peanuts 200g',18,1.59,'Nutty Co','Snacks'),
(35,'Almonds 150g',5,3.99,'Nutty Co','Snacks'),
(36,'Chocolate Bar 50g',60,0.89,'SweetBytes','Snacks'),
(37,'Granola Bar 40g',28,0.79,'HealthyBites','Snacks'),
(38,'Crackers 200g',16,1.19,'Baked&Co','Snacks'),
(39,'Popcorn 90g',0,0.99,'Pop&Joy','Snacks'),
(40,'Protein Cookies 60g',9,1.99,'HealthyBites','Snacks');

UNLOCK TABLES;

