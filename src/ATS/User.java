package ATS;

import Exceptions.UserNotFoundException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public abstract class User<T extends User> {
    protected int id;
    protected String name;
    protected String password;
    protected String email;
    protected String role;

    public User(int id, String name, String password, String email, String role) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    public abstract void menu(T user) throws Exception;

    /**
     * Check if admin credentials are valid
     */
    public static boolean isAdminAvailable(Connection connection, String email, String password) {
        String sql = "SELECT COUNT(*) FROM admin WHERE email = ? AND password = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; // true if count > 0
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Check if client credentials are valid
     */
    public static boolean isClientAvailable(Connection connection, String email, String password) {
        String sql = "SELECT COUNT(*) FROM client WHERE email = ? AND password = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; // true if count > 0
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Fetch admin object from database
     */
    public static Admin getAdmin(Connection connection, String email, String password) {
        String sql = "SELECT * FROM admin WHERE email = ? AND password = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                // Create a Scanner for potential input
                Scanner scanner = new Scanner(System.in);

                int id = rs.getInt("id");
                String name = rs.getString("name");
                String role = "admin"; // Or get from DB if stored
                float profit = rs.getFloat("profit");

                // Extract company name from email
                String companyname = email.split("@")[0];
                if (email.contains("@")) {
                    companyname = email.split("@")[0];
                }

                return new Admin(
                        id,
                        name,
                        password,
                        role,
                        connection,
                        scanner,
                        profit,
                        companyname
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Fetch client object from database
     */
    public static Client getClient(Connection connection, String email, String password) {
        String sql = "SELECT * FROM client WHERE email = ? AND password = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                // Create a Scanner for potential input
                Scanner scanner = new Scanner(System.in);

                return new Client(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("password"),
                        "client", // Or get from DB if stored
                        rs.getInt("admin_id"),
                        rs.getInt("age"),
                        rs.getString("gender"),
                        rs.getFloat("balance"),
                        scanner,
                        connection
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Authenticate user and return the appropriate user object
     * @return User object (either Admin or Client) based on successful authentication
     * @throws UserNotFoundException if no matching user found
     */
    public static User getSignedUser(Connection connection, String email, String password) throws UserNotFoundException {
        // Try to get an admin with these credentials
        Admin admin = getAdmin(connection, email, password);
        if (admin != null) {
            return admin;
        }

        // If not admin, try to get a client
        Client client = getClient(connection, email, password);
        if (client != null) {
            return client;
        }

        // No valid user found
        throw new UserNotFoundException("Invalid Credentials");
    }


    public static void signin(Connection connection,String email,String password) throws Exception
    {
        User user = User.getSignedUser(connection, email, password);

        if (user instanceof Admin) {
            Admin adminUser = (Admin) user;
            adminUser.menu(adminUser); // Call the menu method
            // Handle admin-specific functionality
        } else if (user instanceof Client) {
            Client clientUser = (Client) user;
            clientUser.menu(clientUser); // Call the menu method
            // Handle client-specific functionality
        }
    }
    public int getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }
}