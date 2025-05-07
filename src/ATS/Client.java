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
                    scanner.nextLine(); // Consume newline
                    System.out.print("Enter seat type (business/economy): ");
                    String seatType = scanner.nextLine();
                    System.out.print("Enter source: ");
                    String source = scanner.nextLine();
                    System.out.print("Enter destination: ");
                    String destination = scanner.nextLine();
                    bookFlight(seatType, source, destination);
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
    //phele destination or source or type pochega
//    public void bookFlight(String type, String reqSource, String reqDestination) {
//        // Using PreparedStatement for security and performance
//        String query = "SELECT * FROM flight WHERE source = ? AND destination = ?";
//
//        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
//            // Set parameters safely to prevent SQL injection
//            preparedStatement.setString(1, reqSource);
//            preparedStatement.setString(2, reqDestination);
//
//            // Execute the query
//            try (ResultSet resultSet = preparedStatement.executeQuery()) {
//                boolean flightsFound = false;
//                System.out.println("Available flights from " + reqSource + " to " + reqDestination + ":");
//                System.out.println("----------------------------------------------------------");
//
//                // Process the ResultSet and display flights
//                while (resultSet.next()) {
//                    flightsFound = true;
//                    int id = resultSet.getInt("id");
//                    int planeId = resultSet.getInt("plane_id");
//                    String source = resultSet.getString("source");
//                    String destination = resultSet.getString("destination");
//                    Time arrivalTime = resultSet.getTime("arrival_time");
//                    Time reportingTime = resultSet.getTime("reporting_time");
//                    float expense = resultSet.getFloat("expense");
//
//                    // Display flight information in a more readable format
//                    System.out.printf("Flight ID: %-5d | Plane: %-5d | Route: %-10s to %-10s | Arrival: %-10s | Check-in: %-10s | Price: $%.2f%n",
//                            id, planeId, source, destination, arrivalTime, reportingTime, expense);
//                }
//
//                if (!flightsFound) {
//                    System.out.println("No flights available for the selected route.");
//                } else {
//                    System.out.println("----------------------------------------------------------");
//                    System.out.println("Please select a flight ID to book:");
//                }
//            }
//        } catch (SQLException e) {
//            System.out.println("Error searching for flights: " + e.getMessage());
//            e.printStackTrace();
//        }
//    }  book
    //book flight
    /**
     * Books a flight for the client after checking availability and balance
     * @param type Type of seat (business/economy)
     * @param reqSource Source location
     * @param reqDestination Destination location
     */
    public void bookFlight(String type, String reqSource, String reqDestination) {
        // Using PreparedStatement for security and performance
        String query = "SELECT f.*, p.business_seats, p.economy_seats, p.admin_id, " +
                "(SELECT COUNT(*) FROM booking b WHERE b.flight_id = f.id AND b.isreserved = 1 AND b.seat_type = ?) as booked_seats " +
                "FROM flight f " +
                "JOIN plane p ON f.plane_id = p.id " +
                "WHERE f.source = ? AND f.destination = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            // Set parameters safely to prevent SQL injection
            preparedStatement.setString(1, type.toLowerCase());
            preparedStatement.setString(2, reqSource);
            preparedStatement.setString(3, reqDestination);

            // Execute the query
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                boolean flightsFound = false;
                System.out.println("Available flights from " + reqSource + " to " + reqDestination + ":");
                System.out.println("----------------------------------------------------------");

                // Process the ResultSet and display flights
                while (resultSet.next()) {
                    flightsFound = true;
                    int id = resultSet.getInt("id");
                    int planeId = resultSet.getInt("plane_id");
                    int adminId = resultSet.getInt("admin_id");
                    String source = resultSet.getString("source");
                    String destination = resultSet.getString("destination");
                    Timestamp arrivalTime = resultSet.getTimestamp("arrival_time");
                    Timestamp reportingTime = resultSet.getTimestamp("reporting_time");
                    float expense = resultSet.getFloat("expense");
                    int totalSeats = type.equalsIgnoreCase("business") ?
                            resultSet.getInt("business_seats") :
                            resultSet.getInt("economy_seats");
                    int bookedSeats = resultSet.getInt("booked_seats");
                    int availableSeats = totalSeats - bookedSeats;

                    // Display flight information with available seats info
                    System.out.printf("Flight ID: %-5d | Plane: %-5d | Route: %-10s to %-10s | Arrival: %-19s | Check-in: %-19s | Price: $%.2f | %s seats available: %d%n",
                            id, planeId, source, destination, arrivalTime, reportingTime, expense, type, availableSeats);
                }

                if (!flightsFound) {
                    System.out.println("No flights available for the selected route.");
                    return;
                } else {
                    System.out.println("----------------------------------------------------------");
                    System.out.print("Please enter the flight ID to book (or 0 to cancel): ");
                    int selectedFlightId = scanner.nextInt();
                    scanner.nextLine(); // Consume newline

                    if (selectedFlightId == 0) {
                        System.out.println("Booking cancelled.");
                        return;
                    }

                    // Book the selected flight
                    bookSelectedFlight(selectedFlightId, type);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error searching for flights: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Helper method to book a selected flight
     * @param flightId ID of the flight to book
     * @param seatType Type of seat (business/economy)
     */
    private void bookSelectedFlight(int flightId, String seatType) {
        // First verify the flight exists and has available seats
        String checkQuery = "SELECT f.*, f.expense, p.business_seats, p.economy_seats, p.admin_id, " +
                "(SELECT COUNT(*) FROM booking b WHERE b.flight_id = f.id AND b.isreserved = 1 AND b.seat_type = ?) as booked_seats " +
                "FROM flight f " +
                "JOIN plane p ON f.plane_id = p.id " +
                "WHERE f.id = ?";

        try (PreparedStatement checkStmt = connection.prepareStatement(checkQuery)) {
            checkStmt.setString(1, seatType.toLowerCase());
            checkStmt.setInt(2, flightId);

            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next()) {
                    float expense = rs.getFloat("expense");
                    int adminId = rs.getInt("admin_id");
                    int totalSeats = seatType.equalsIgnoreCase("business") ?
                            rs.getInt("business_seats") : rs.getInt("economy_seats");
                    int bookedSeats = rs.getInt("booked_seats");

                    // Check if seats are available
                    if (bookedSeats >= totalSeats) {
                        System.out.println("Sorry, no " + seatType + " seats available on this flight.");
                        return;
                    }

                    // Calculate ticket price based on seat type
                    float ticketPrice = expense;
                    if (seatType.equalsIgnoreCase("business")) {
                        ticketPrice *= 1.5; // Business class costs more
                    }

                    // Check if client has sufficient balance
                    if (this.balance < ticketPrice) {
                        System.out.println("Insufficient balance. Your balance: $" + this.balance +
                                ", Ticket price: $" + ticketPrice);
                        return;
                    }

                    // All checks passed, proceed with booking
                    connection.setAutoCommit(false); // Start transaction

                    try {
                        // 1. Insert booking record
                        String bookingQuery = "INSERT INTO booking (flight_id, client_id, ispaid, isreserved, fees, seat_type) VALUES (?, ?, ?, ?, ?, ?)";
                        try (PreparedStatement bookStmt = connection.prepareStatement(bookingQuery)) {
                            bookStmt.setInt(1, flightId);
                            bookStmt.setInt(2, this.id);
                            bookStmt.setBoolean(3, true); // ispaid
                            bookStmt.setBoolean(4, true); // isreserved
                            bookStmt.setFloat(5, ticketPrice);
                            bookStmt.setString(6, seatType.toLowerCase());
                            bookStmt.executeUpdate();
                        }

                        // 2. Deduct balance from client
                        String updateClientQuery = "UPDATE client SET balance = balance - ? WHERE id = ?";
                        try (PreparedStatement updateClientStmt = connection.prepareStatement(updateClientQuery)) {
                            updateClientStmt.setFloat(1, ticketPrice);
                            updateClientStmt.setInt(2, this.id);
                            updateClientStmt.executeUpdate();
                        }

                        // 3. Add to admin's profit
                        String updateAdminQuery = "UPDATE admin SET profit = profit + ? WHERE id = ?";
                        try (PreparedStatement updateAdminStmt = connection.prepareStatement(updateAdminQuery)) {
                            updateAdminStmt.setFloat(1, ticketPrice);
                            updateAdminStmt.setInt(2, adminId);
                            updateAdminStmt.executeUpdate();
                        }

                        // 4. Update local balance
                        this.balance -= ticketPrice;

                        connection.commit(); // Commit transaction
                        System.out.println("Flight booked successfully!");
                        System.out.println("Ticket Details:");
                        System.out.println("Flight ID: " + flightId);
                        System.out.println("Seat Type: " + seatType);
                        System.out.println("Amount Paid: $" + ticketPrice);
                        System.out.println("Remaining Balance: $" + this.balance);

                    } catch (SQLException e) {
                        connection.rollback(); // Rollback in case of error
                        System.out.println("Error during booking: " + e.getMessage());
                        e.printStackTrace();
                    } finally {
                        connection.setAutoCommit(true); // Reset auto-commit
                    }
                } else {
                    System.out.println("Flight not found.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error checking flight availability: " + e.getMessage());
            e.printStackTrace();
        }
    }
}