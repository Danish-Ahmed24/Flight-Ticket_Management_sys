package ATS;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Client extends User<Client>{
    private int admin_id;
    private int age;
    private String gender;
    private float balance;
    private Scanner scanner;
    private Connection connection;

    public Client(int id, String name, String password, String role, int admin_id, int age, String gender, float balance,Scanner scanner,Connection connection) {
        super(id, name, password, name+"@client.com", role);
        this.admin_id = admin_id;
        this.connection=connection;
        this.scanner=scanner;
        this.age = age;
        this.gender = gender;
        this.balance = balance;
    }

    @Override
    public void menu(Client client) throws Exception {
        System.out.println("Welcome Client:-> " + client.name);

        while (true) {
            System.out.println("1.  View Bookings");//
            System.out.println("2.  View Flights");//
            System.out.println("3.  Book Flight");//
            System.out.println("4.  My profile");//
            System.out.println("5.  Change password");//
            System.out.println("6. Log out");
            System.out.print("Enter: ");


            int option = scanner.nextInt();
            switch (option)
            {
                case 1:
                    IViewData.viewBookings(connection,client);
                    break;
                case 2:
                    IViewData.viewFlights(connection);
                    break;
                case 3:
                    //book flight
                    break;
                case 4:
                    showProfile();
                    break;
                case 5:
                    scanner.nextLine();
                    System.out.print("Enter current password: ");
                    String currentPassword = scanner.nextLine();

                    System.out.print("Enter new password: ");
                    String newPassword = scanner.nextLine();

                    System.out.print("Confirm new password: ");
                    String confirmPassword = scanner.nextLine();

                    changePassword(currentPassword, newPassword, confirmPassword);
                    break;
                case 6:
                    System.out.println("Logging out");
                    return;
            }
        }
    }
//    name,gender,age,email)

    public void showProfile()
    {
        try {
            String query = "SELECT * FROM client where id="+this.id;  // Your query to fetch clients
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            // Process the ResultSet and display clients
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                String gender = resultSet.getString("gender");
                int age = resultSet.getInt("age");
                System.out.println("Name: " + name + ", Email: " + email + ", Gender: " + gender+", Age: "+age);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public void changePassword(String currentPassword, String newPassword, String confirmPassword) {
        ArrayList<Client> clients = IViewData.getClients(connection);

        for (Client client : clients) {
            // Assuming 'this' refers to the currently logged-in client
            if (client.getId() == this.id) {
                if (!client.getPassword().equals(currentPassword)) {
                    System.out.println("Current password is incorrect.");
                    return;
                }

                if (!newPassword.equals(confirmPassword)) {
                    System.out.println("New password and confirmation do not match.");
                    return;
                }

                try {
                    String updateQuery = "UPDATE client SET password = ? WHERE id = ?";
                    PreparedStatement pstmt = connection.prepareStatement(updateQuery);
                    pstmt.setString(1, newPassword);
                    pstmt.setInt(2, this.id);
                    int rowsUpdated = pstmt.executeUpdate();

                    if (rowsUpdated > 0) {
                        System.out.println("Password updated successfully.");
                        this.password = newPassword; // update in object as well
                    } else {
                        System.out.println("Failed to update password.");
                    }
                } catch (SQLException e) {
                    System.out.println("Database error: " + e.getMessage());
                }

                return;
            }
        }

        System.out.println("Client not found.");
    }

}
