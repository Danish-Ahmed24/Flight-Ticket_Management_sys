package ATS;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public interface GetDataFromDatabase {
    public static ArrayList<Admin> getAdmins(Connection connection, Scanner scanner) {
        ArrayList<Admin> admins = new ArrayList<>();
        String sql = "SELECT * FROM admin";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Admin admin = new Admin(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("password"),
                        rs.getString("email"),
                        "role", // if role isn't in DB, hardcode or derive
                        connection,
                        scanner,
                        rs.getFloat("profit"),
                        rs.getString("companyname")
                );

                admins.add(admin);
            }

        } catch (SQLException e) {
            System.out.println("Error fetching admins: " + e.getMessage());
        }

        return admins;
    }

}
