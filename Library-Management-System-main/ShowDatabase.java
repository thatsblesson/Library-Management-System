import java.sql.*;

public class ShowDatabase {
    public static void main(String[] args) {
        String url = "jdbc:sqlite:library.db"; // path to your database

        // SQL query to fetch all rows from the books table
        String sql = "SELECT * FROM books";

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("Database contents of table 'books':");
            System.out.println("-----------------------------------");

            // Loop through the result set
            while (rs.next()) {
                // Replace column1, column2 with your actual column names
                int id = rs.getInt("id");
                String title = rs.getString("title");
                String author = rs.getString("author");
                int available = rs.getInt("available");

                System.out.println(id + " | " + title + " | " + author + " | Available: " + (available == 1 ? "Yes" : "No"));
            }

        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }
}

    

