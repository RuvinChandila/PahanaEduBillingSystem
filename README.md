# 📚 Pahana Edu Billing System 

<div align="center">
  <img src="https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=coffeescript&logoColor=white" alt="Java"/>
  <img src="https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white" alt="MySQL" />
  <img src="https://img.shields.io/badge/Apache%20Tomcat-F8DC75?style=for-the-badge&logo=apache-tomcat&logoColor=black" alt="Tomcat" />
  <img src="https://img.shields.io/badge/IntelliJ%20IDEA-000000?style=for-the-badge&logo=intellijidea&logoColor=white" alt="IntelliJ IDEA" /></div>

---

## 🎯 Overview

**Pahana Edu Billing System** is a full-featured enterprise application designed specifically for educational bookshops and retail businesses. The system provides complete billing functionality, customer management, inventory control and business analytics through a modern, responsive web interface.

## ✨ Key Features

### 🔐 Authentication & Security
- **Secure User Registration & Login** with BCrypt password hashing
- **Session-based Authentication** with automatic timeout
- **Role-based Access Control** with authentication filters
- **SQL Injection Protection** through prepared statements
- **Input Validation & Sanitization** on all user inputs

### 👥 Customer Management
- **Complete Customer Profiles** with account numbers and contact details
- **Customer Registration** with unique account number generation
- **Advanced Search & Filtering** capabilities
- **Customer History Tracking** with registration timestamps
- **CRUD Operations** with data integrity constraints

### 📦 Inventory Control
- **Real-time Stock Management** with automatic updates
- **Product Catalog** with detailed item descriptions
- **Price Management** with decimal precision
- **Stock Alerts** for low inventory items
- **Item Code System** for easy identification

### 🧾 Advanced Billing System
- **Multi-step Bill Creation** with customer selection
- **Dynamic Item Addition** with real-time calculations
- **Automatic Stock Deduction** via database triggers
- **Professional Invoice Generation** with detailed line items
- **Grand Total Calculations** with tax considerations
- **Print a Bill** generate a printable copy of the invoice

### 📊 Dashboard & Analytics
- **Interactive Sales Charts** powered by Chart.js
- **Real-time Business Metrics** and KPIs
- **Quick Action Cards** for common operations
- **Live Clock** with Sri Lankan timezone

## 🏗️ Technology Architecture


- **Backend: Java, jBCrypt**
- **Frontend: JSP, HTML, CSS**
- **Build Tool: Maven**
- **Server: Apache Tomcat 11**
- **Database: MySQL**


## 📋 System Requirements

### Development Environment
- **OpenJDK 24 or compatible version**
- **Apache Tomcat**: 11
- **MySQL Server**
- **IDE**: IntelliJ IDEA (Ultimate Edition)
- **Build Tool**: Maven 


## 🚀 Installation Guide


## 🗄️ Database Schema


#### Execute the following SQL script in your MySQL client to set up the database and required tables:
```sql

-- Pahana Edu Bookshop -  Database Schema

DROP DATABASE IF EXISTS `pahana_edu_db`;

CREATE DATABASE `pahana_edu_db` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE `pahana_edu_db`;

    
-- Table Creation

CREATE TABLE IF NOT EXISTS `users` (
  `user_id` INT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(50) NOT NULL UNIQUE,
  `password_hash` VARCHAR(255) NOT NULL,
  `full_name` VARCHAR(100) NULL,
  `created_at` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`user_id`)
) ENGINE = InnoDB;

-- Table `customers`

CREATE TABLE IF NOT EXISTS `customers` (
  `customer_id` INT NOT NULL AUTO_INCREMENT,
  `account_number` VARCHAR(20) NOT NULL UNIQUE,
  `name` VARCHAR(150) NOT NULL,
  `address` VARCHAR(255) NOT NULL,
  `telephone` VARCHAR(20) NOT NULL,
  `registered_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`customer_id`)
) ENGINE = InnoDB;

-- Table `items`

CREATE TABLE IF NOT EXISTS `items` (
  `item_id` INT NOT NULL AUTO_INCREMENT,
  `item_code` VARCHAR(50) NOT NULL UNIQUE,
  `description` VARCHAR(255) NOT NULL,
  `unit_price` DECIMAL(10,2) NOT NULL,
  `stock_quantity` INT NOT NULL DEFAULT 0,
  PRIMARY KEY (`item_id`)
) ENGINE = InnoDB;

-- Table `bills`

CREATE TABLE IF NOT EXISTS `bills` (
  `bill_id` INT NOT NULL AUTO_INCREMENT,
  `customer_id` INT NOT NULL,
  `bill_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `total_amount` DECIMAL(10,2) NOT NULL DEFAULT 0.00,
  `units_consumed` INT NOT NULL,
  PRIMARY KEY (`bill_id`),
  CONSTRAINT `fk_bills_customers`
    FOREIGN KEY (`customer_id`)
    REFERENCES `customers` (`customer_id`)
    ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE = InnoDB;

-- Table `bill_items`

CREATE TABLE IF NOT EXISTS `bill_items` (
  `bill_item_id` INT NOT NULL AUTO_INCREMENT,
  `bill_id` INT NOT NULL,
  `item_id` INT NOT NULL,
  `quantity` INT NOT NULL,
  `price_at_purchase` DECIMAL(10,2) NOT NULL,
  PRIMARY KEY (`bill_item_id`),
  CONSTRAINT `fk_bill_items_bills`
    FOREIGN KEY (`bill_id`)
    REFERENCES `bills` (`bill_id`)
    ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_bill_items_items`
    FOREIGN KEY (`item_id`)
    REFERENCES `items` (`item_id`)
    ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE = InnoDB;

-- Initial Data Insertion

INSERT INTO `users` (`username`, `password_hash`, `full_name`)
VALUES ('admin', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', 'Administrator');

-- Stored Procedures

DELIMITER $$

CREATE PROCEDURE `AddCustomer`(
    IN p_account_number VARCHAR(20),
    IN p_name VARCHAR(150),
    IN p_address VARCHAR(255),
    IN p_telephone VARCHAR(20)
)
BEGIN
    INSERT INTO `customers` (`account_number`, `name`, `address`, `telephone`)
    VALUES (p_account_number, p_name, p_address, p_telephone);
END$$

CREATE PROCEDURE `GetCustomerDetailsByAccount`(
    IN p_account_number VARCHAR(20)
)
BEGIN
    SELECT `customer_id`, `account_number`, `name`, `address`, `telephone`, `registered_date`
    FROM `customers`
    WHERE `account_number` = p_account_number;
END$$

DELIMITER ;

-- Triggers

DELIMITER $$

CREATE TRIGGER `AfterBillItemInsert`
AFTER INSERT ON `bill_items`
FOR EACH ROW
BEGIN
    UPDATE `items`
    SET `stock_quantity` = `stock_quantity` - NEW.quantity
    WHERE `item_id` = NEW.item_id;

    UPDATE `bills`
    SET `total_amount` = `total_amount` + (NEW.quantity * NEW.price_at_purchase)
    WHERE `bill_id` = NEW.bill_id;
END$$

DELIMITER ;

-- End of Script
```

### Configure Database Connection

Update `src/main/java/com/pahanasolutions/PahanaEdu/db/DatabaseConnection.java`:

```java
public class DatabaseConnection {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/pahana_edu_db?useSSL=false&serverTimezone=UTC";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "123456";
    
    // Rest of the configuration...
}
```


## ⚙️ Configuration Steps

Follow these steps to set up your project environment.

### 1. Configure IntelliJ IDEA Project Structure

First, we need to tell IntelliJ about your JDK and how to package the application.

1.  Open **Project Structure** by navigating to `File > Project Structure` or using the shortcut `Ctrl+Alt+Shift+S`.
2.  **Project Settings > Project**:
   * Set the **Project SDK** to your installed `OpenJDK 24`.
3.  **Platform Settings > Artifacts**:
   * If an artifact doesn't exist, create one by clicking `+` > `Web Application: Exploded` > `From Modules...`.
   * Select the main project module (e.g., `PahanaEduBillingSystem`).
   * Ensure the **Output directory** is set to a valid location, like `.../target/PahanaEduBillingSystem-1.0-SNAPSHOT`.



### 2. Add and Configure the Tomcat Server

Next, link your Tomcat server to the IDE and tell it which application to deploy.

1.  Open the run configurations menu by navigating to `Run > Edit Configurations...`.
2.  Click the `+` icon and select `Tomcat Server > Local`.
3.  In the **Server** tab:
   * For **Application Server**, click `Configure...` and select the root directory of your `Apache Tomcat 11` installation.
   * Set the **HTTP port** to `8080`.
4.  Switch to the **Deployment** tab:
   * Click the `+` icon, select `Artifact...`, and choose the `PahanaEduBillingSystem:war exploded` artifact.
   * Change the **Application context** to `/PahanaEduBillingSystem` or simply `/` if you want it to run at the root URL.
5.  Click **Apply** and **OK** to save the configuration.

## ▶️ Running the Application

You are all set to launch the project!

1.  Select the `Tomcat` configuration you just created from the run configurations dropdown in the top-right toolbar.
2.  Click the green **Run** icon (`▷️`) to start the server.
3.  Open your web browser and navigate to `http://localhost:8080/PahanaEduBillingSystem` (or the application context you set).






## 📁 Project Structure

```
src/main/
├── java/com/pahanasolutions/PahanaEdu/
│   ├── dao/           - Data Access Objects for database operations            
│   ├── model/         - Data models for entities                  
│   ├── servlet/       - Servlets for handling requests        
│   ├── db/            - Database connection        
│   └── util/          - Utility classes for shared functionality         
│
└── webapp/                     
    ├── app/           - JSP view files         
    ├── css/           - custom styles        
    ├── login.jsp              
    └── register.jsp            
```

## 🔮 Future Roadmap

- [ ] **Email Integration**: Automated bill delivery and notifications
- [ ] **Multi-location Support**: Support for multiple store branches
- [ ] **Barcode Scanning**: Mobile barcode scanning for inventory
- [ ] **Payment Integration**: Support for multiple payment methods


## 🤝 Contributing

We welcome contributions from the developer community! Here's how you can help:

1. **Fork** the repository on GitHub
2. **Clone** your fork locally
3. **Create** a feature branch: `git checkout -b feature/amazing-feature`
4. **Make** your changes with proper testing
5. **Commit** with descriptive messages: `git commit -m 'Add amazing feature'`
6. **Push** to your fork: `git push origin feature/amazing-feature`
7. **Submit** a Pull Request with detailed description



## 📄 License

This project is licensed under the **MIT License** - see the [LICENSE](LICENSE) file for complete details.

```
MIT License

Copyright (c) 2025 Pahana Solutions

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

<div align="center">
  <p><strong>Built with ❤️ in Sri Lanka</strong></p>
  <p>© 2025 Pahana Edu Book Shop. All rights reserved.</p>

</div>
