import java.sql.*;
import java.util.Scanner;

public class LibraryManagementSystem {
    static final String DB_URL = "jdbc:sqlite:library.db";

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            if (conn != null) {
                initializeDatabase(conn);
                runLibrarySystem(conn);
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }

    private static void initializeDatabase(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        String sqlBooks = "CREATE TABLE IF NOT EXISTS books (" +
                          "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                          "title TEXT NOT NULL," +
                          "author TEXT NOT NULL," +
                          "available INTEGER DEFAULT 1)";
        stmt.execute(sqlBooks);

        String sqlMembers = "CREATE TABLE IF NOT EXISTS members (" +
                            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                            "name TEXT NOT NULL)";
        stmt.execute(sqlMembers);
    }

    private static void runLibrarySystem(Connection conn) throws SQLException {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("\nLibrary Management System");
            System.out.println("1. Add Book");
            System.out.println("2. View Books");
            System.out.println("3. Issue Book");
            System.out.println("4. Return Book");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");
            int choice = sc.nextInt();
            sc.nextLine(); // consume newline

            switch (choice) {
                case 1 -> addBook(conn, sc);
                case 2 -> viewBooks(conn);
                case 3 -> issueBook(conn, sc);
                case 4 -> returnBook(conn, sc);
                case 5 -> {
                    System.out.println("Exiting...");
                    return;
                }
                default -> System.out.println("Invalid choice!");
            }
        }
    }

    private static void addBook(Connection conn, Scanner sc) throws SQLException {
        System.out.print("Enter book title: ");
        String title = sc.nextLine();
        System.out.print("Enter author name: ");
        String author = sc.nextLine();
        String sql = "INSERT INTO books(title, author) VALUES (?, ?)";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, title);
        ps.setString(2, author);
        ps.executeUpdate();
        System.out.println("Book added successfully!");
    }

    private static void viewBooks(Connection conn) throws SQLException {
        String sql = "SELECT id, title, author, available FROM books";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        System.out.println("\nID | Title | Author | Available");
        while (rs.next()) {
            System.out.printf("%d | %s | %s | %s\n",
                    rs.getInt("id"),
                    rs.getString("title"),
                    rs.getString("author"),
                    rs.getInt("available") == 1 ? "Yes" : "No");
        }
    }

    private static void issueBook(Connection conn, Scanner sc) throws SQLException {
        System.out.print("Enter book ID to issue: ");
        int id = sc.nextInt();
        String sql = "UPDATE books SET available = 0 WHERE id = ? AND available = 1";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, id);
        int updated = ps.executeUpdate();
        System.out.println(updated == 1 ? "Book issued!" : "Book not available!");
    }

    private static void returnBook(Connection conn, Scanner sc) throws SQLException {
        System.out.print("Enter book ID to return: ");
        int id = sc.nextInt();
        String sql = "UPDATE books SET available = 1 WHERE id = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, id);
        ps.executeUpdate();
        System.out.println("Book returned!");
    }
}
