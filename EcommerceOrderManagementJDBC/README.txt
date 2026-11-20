E-COMMERCE ORDER MANAGEMENT SYSTEM - JDBC + MYSQL

Requirements: JDK 17+, Apache Maven, MySQL Server.

SETUP:
1. Open MySQL Workbench and connect to your local MySQL server.
2. Open and execute database/ecommerce_db.sql.
3. Edit src/main/java/com/college/ecommerce/DBConnection.java.
4. Replace YOUR_MYSQL_PASSWORD with your MySQL root password.
5. Open the extracted project folder in VS Code (or IntelliJ) and let Maven import it.

RUN IN VS CODE TERMINAL:
    mvn clean install
    mvn exec:java

MENU:
1. Insert three order records (PreparedStatement; duplicate order IDs handled).
2. Display orders by status with quantity > 2, unit price descending.
3. Increase quantity for an existing order.
4. Validate and update order status; display updated details.
5. Display total order value, then delete an order only if status is Cancelled.
0. Exit.

VALID STATUSES:
Pending, Shipped, Delivered, Cancelled

The SQL script creates the ecommerce_db database and Orders table.
The table starts empty so option 1 can be used to insert at least three records.
MySQL URL assumes localhost port 3306.
