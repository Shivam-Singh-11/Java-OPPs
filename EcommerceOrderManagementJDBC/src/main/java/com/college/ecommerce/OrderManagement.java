package com.college.ecommerce;

import java.sql.*;
import java.util.Scanner;

public class OrderManagement {
    private static final Scanner sc = new Scanner(System.in);
    private static final String[] VALID_STATUSES = {"Pending", "Shipped", "Delivered", "Cancelled"};

    public static void main(String[] args) {
        try (Connection con = DBConnection.getConnection()) {
            System.out.println("Connected to ecommerce_db successfully.");
            int choice;
            do {
                System.out.println("\n===== ORDER MANAGEMENT SYSTEM =====");
                System.out.println("1. Insert at least three orders");
                System.out.println("2. Display orders by status (quantity > 2), price descending");
                System.out.println("3. Increase order quantity");
                System.out.println("4. Update order status");
                System.out.println("5. Display total order value and delete cancelled order");
                System.out.println("0. Exit");
                System.out.print("Enter choice: ");
                choice = readInt();

                switch (choice) {
                    case 1 -> insertOrders(con);
                    case 2 -> displayOrdersByStatus(con);
                    case 3 -> increaseQuantity(con);
                    case 4 -> updateStatus(con);
                    case 5 -> showTotalAndDelete(con);
                    case 0 -> System.out.println("Exiting application.");
                    default -> System.out.println("Invalid choice. Try again.");
                }
            } while (choice != 0);
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
            System.out.println("Check MySQL, ecommerce_db, and the password in DBConnection.java.");
        }
    }

    // (a) Insert three orders using PreparedStatement and handle duplicate primary keys.
    private static void insertOrders(Connection con) {
        String sql = "INSERT INTO Orders(order_id, customer_name, product_name, quantity, unit_price, status) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            for (int i = 1; i <= 3; i++) {
                System.out.println("\nOrder " + i);
                System.out.print("Order ID: ");
                int id = readInt();
                System.out.print("Customer name: ");
                String customer = sc.nextLine().trim();
                System.out.print("Product name: ");
                String product = sc.nextLine().trim();
                System.out.print("Quantity: ");
                int quantity = readInt();
                System.out.print("Unit price: ");
                double price = readDouble();
                System.out.print("Status (Pending/Shipped/Delivered/Cancelled): ");
                String status = normalizeStatus(sc.nextLine());

                if (quantity <= 0 || price < 0 || customer.isEmpty() || product.isEmpty()) {
                    System.out.println("Invalid data. Quantity must be positive; price cannot be negative; names are required.");
                    i--;
                    continue;
                }
                if (status == null) {
                    System.out.println("Invalid status. Use Pending, Shipped, Delivered, or Cancelled.");
                    i--;
                    continue;
                }

                ps.setInt(1, id);
                ps.setString(2, customer);
                ps.setString(3, product);
                ps.setInt(4, quantity);
                ps.setDouble(5, price);
                ps.setString(6, status);

                try {
                    ps.executeUpdate();
                    System.out.println("Order inserted successfully.");
                } catch (SQLIntegrityConstraintViolationException e) {
                    System.out.println("Duplicate order ID " + id + ". This order was not inserted.");
                } catch (SQLException e) {
                    if ("23000".equals(e.getSQLState())) {
                        System.out.println("Duplicate order ID or constraint error for ID " + id + ".");
                    } else {
                        System.out.println("Insert failed: " + e.getMessage());
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Insert operation failed: " + e.getMessage());
        }
    }

    // (b) Filter by status and quantity > 2; sort by unit_price descending.
    private static void displayOrdersByStatus(Connection con) {
        System.out.print("Enter order status: ");
        String status = normalizeStatus(sc.nextLine());
        if (status == null) {
            System.out.println("Invalid status. Use Pending, Shipped, Delivered, or Cancelled.");
            return;
        }

        String sql = "SELECT * FROM Orders WHERE status = ? AND quantity > 2 ORDER BY unit_price DESC";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, status);
            try (ResultSet rs = ps.executeQuery()) {
                boolean found = false;
                printHeader();
                while (rs.next()) {
                    found = true;
                    printOrder(rs);
                }
                if (!found) System.out.println("No matching orders found.");
            }
        } catch (SQLException e) {
            System.out.println("Display failed: " + e.getMessage());
        }
    }

    // (c) Increase quantity for the selected order.
    private static void increaseQuantity(Connection con) {
        System.out.print("Enter order ID: ");
        int id = readInt();
        System.out.print("Enter additional quantity: ");
        int additional = readInt();
        if (additional <= 0) {
            System.out.println("Additional quantity must be greater than zero.");
            return;
        }

        String sql = "UPDATE Orders SET quantity = quantity + ? WHERE order_id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, additional);
            ps.setInt(2, id);
            if (ps.executeUpdate() > 0) {
                System.out.println("Order quantity updated successfully.");
            } else {
                System.out.println("Order does not exist.");
            }
        } catch (SQLException e) {
            System.out.println("Quantity update failed: " + e.getMessage());
        }
    }

    // (d) Check existence, validate status, update, then display the updated order.
    private static void updateStatus(Connection con) {
        System.out.print("Enter order ID: ");
        int id = readInt();

        String selectSql = "SELECT * FROM Orders WHERE order_id = ?";
        try (PreparedStatement select = con.prepareStatement(selectSql)) {
            select.setInt(1, id);
            try (ResultSet rs = select.executeQuery()) {
                if (!rs.next()) {
                    System.out.println("Order does not exist.");
                    return;
                }
                System.out.println("Existing order details:");
                printHeader();
                printOrder(rs);
            }

            System.out.print("Enter new status (Pending/Shipped/Delivered): ");
            String newStatus = normalizeStatus(sc.nextLine());
            if (newStatus == null || newStatus.equals("Cancelled")) {
                System.out.println("Invalid status. New status must be Pending, Shipped, or Delivered.");
                return;
            }

            String updateSql = "UPDATE Orders SET status = ? WHERE order_id = ?";
            try (PreparedStatement update = con.prepareStatement(updateSql)) {
                update.setString(1, newStatus);
                update.setInt(2, id);
                update.executeUpdate();
            }

            System.out.println("Order status updated. Updated order details:");
            try (PreparedStatement again = con.prepareStatement(selectSql)) {
                again.setInt(1, id);
                try (ResultSet rs = again.executeQuery()) {
                    if (rs.next()) {
                        printHeader();
                        printOrder(rs);
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Search/update failed: " + e.getMessage());
        }
    }

    // (e) Show SUM(quantity * unit_price), then delete only a Cancelled order.
    private static void showTotalAndDelete(Connection con) {
        String sumSql = "SELECT COALESCE(SUM(quantity * unit_price), 0) AS total_value FROM Orders";
        try (Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sumSql)) {
            if (rs.next()) {
                System.out.printf("Total order value: %.2f%n", rs.getDouble("total_value"));
            }
        } catch (SQLException e) {
            System.out.println("Total value query failed: " + e.getMessage());
            return;
        }

        System.out.print("Enter order ID to delete: ");
        int id = readInt();
        String deleteSql = "DELETE FROM Orders WHERE order_id = ? AND status = 'Cancelled'";
        try (PreparedStatement ps = con.prepareStatement(deleteSql)) {
            ps.setInt(1, id);
            if (ps.executeUpdate() > 0) {
                System.out.println("Order deleted successfully because its status was Cancelled.");
            } else {
                System.out.println("Order cannot be deleted: it does not exist or its status is not Cancelled.");
            }
        } catch (SQLException e) {
            System.out.println("Delete failed: " + e.getMessage());
        }
    }

    private static String normalizeStatus(String value) {
        for (String status : VALID_STATUSES) {
            if (status.equalsIgnoreCase(value.trim())) return status;
        }
        return null;
    }

    private static void printHeader() {
        System.out.printf("%-10s %-20s %-20s %-10s %-12s %-12s%n",
                "Order ID", "Customer", "Product", "Quantity", "Unit Price", "Status");
        System.out.println("------------------------------------------------------------------------------------------");
    }

    private static void printOrder(ResultSet rs) throws SQLException {
        System.out.printf("%-10d %-20s %-20s %-10d %-12.2f %-12s%n",
                rs.getInt("order_id"),
                rs.getString("customer_name"),
                rs.getString("product_name"),
                rs.getInt("quantity"),
                rs.getDouble("unit_price"),
                rs.getString("status"));
    }

    private static int readInt() {
        while (true) {
            try {
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("Enter a valid integer: ");
            }
        }
    }

    private static double readDouble() {
        while (true) {
            try {
                return Double.parseDouble(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print("Enter a valid number: ");
            }
        }
    }
}
