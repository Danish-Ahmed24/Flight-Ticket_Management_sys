package ATS;

import java.sql.*;
import java.util.Scanner;

public class Admin {
    private final int id;
    private String name,password,companyname,email;
    private float profit;
    private final Connection connection;
    private final Scanner scanner;

    public Admin(Connection connection, Scanner scanner,int id)
    {
        this.id=id;
        this.scanner=scanner;
        this.connection=connection;
    }
    public void adminMenu(Admin admin)
    {
        System.out.println("Welcome ADMIN:-> " + admin.name);
        while (true) {
            System.out.println("1.  View Clients");//
            System.out.println("2.  View Planes");//
            System.out.println("3.  View All Bookings");//
            System.out.println("4.  View All Flights");//
            System.out.println("5.  Assign Flight");
            System.out.println("6.  Add Plane");//
            System.out.println("7.  Add Flight");//
            System.out.println("8.  Update Flight");
            System.out.println("9.  Another Update Flight");
            System.out.println("10. Log out");
            System.out.print("Enter: ");

            int option = scanner.nextInt();

            switch (option) {
                case 1:
                    admin.viewClients();//view Client
                    break;
                case 2:
                    admin.viewPlanes();//view Planes
                    break;
                case 3:
                    admin.viewAllBookings();// view Bookings
                    break;
                case 4:
                    admin.viewFlights();//view Flights
                    break;
                case 5:
                    System.out.println("Assigning Flight...");
                    break;
                case 6:
                    //Add Plane
                    scanner.nextLine();
                    System.out.print("Enter Plane Model: ");
                    String planeModel = scanner.nextLine();

                    System.out.print("Enter Manufacturer: ");
                    String manufacturer = scanner.nextLine();

                    System.out.print("Enter number of Business Seats: ");
                    int businessSeats = scanner.nextInt();

                    scanner.nextLine();

                    System.out.print("Enter number of Economy Seats: ");
                    int economySeats = scanner.nextInt();

                    scanner.nextLine();

                    admin.addPlane(planeModel, manufacturer, businessSeats, economySeats);
                    break;
                case 7:
                    //addFlight
                    System.out.println("Adding a New Flight...");
                    System.out.print("Enter plane ID: ");
                    try{
                    int plane_id = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Enter source: ");
                    String source = scanner.nextLine();
                    System.out.print("Enter destination: ");
                    String destination = scanner.nextLine();
                    System.out.print("Enter arrival time (HH:MM:SS): ");
                    String arrivalStr = scanner.nextLine();
                    Time arrival_time = Time.valueOf(arrivalStr);
                    System.out.print("Enter reporting time (HH:MM:SS): ");
                    String reportingStr = scanner.nextLine();
                    Time reporting_time = Time.valueOf(reportingStr);
                    System.out.print("Enter expense: ");
                    float expense = scanner.nextFloat();
                    admin.addFlight(plane_id, source, destination, arrival_time, reporting_time, expense);
            }
             catch (IllegalArgumentException e) {
                System.out.println("Invalid time format. Please use HH:MM:SS.");
            }

                    break;
                case 8:
                    //Update Flight
                    System.out.print("Enter Flight ID: ");
                    int flightId = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    System.out.print("Enter field to update (source, destination, arrival_time, reporting_time, expense): ");
                    String field = scanner.nextLine();
                    System.out.print("Enter new value for " + field + ": ");
                    String newValue = scanner.nextLine();
                    String result = admin.updateFlight(flightId, field, newValue);
                    System.out.println(result);
                    break;
                case 9:
                    System.out.println("Updating another Flight...");
                    break;
                case 10:
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    /// ////////////////////////////////////////////////////////////  VIEW WALE
    public void viewClients() {
        try {
            String query = "SELECT * FROM client";  // Your query to fetch clients
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            // Process the ResultSet and display clients
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                System.out.println("ID: " + id + ", Name: " + name + ", Email: " + email);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public void viewPlanes() {
        try {
            String query = "SELECT * FROM plane";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            //Process the ResultSet and display planes
            while(resultSet.next())
            {
                int id = resultSet.getInt("id");
                int admin_id = resultSet.getInt("admin_id");
                String plane_model = resultSet.getString("plane_model");
                String manufacturer = resultSet.getString("manufacturer");
                int business_seats = resultSet.getInt("business_seats");
                int economy_seats = resultSet.getInt("economy_seats");
                System.out.println("ID: " + id + ", Admin ID: " + admin_id + ", Model: " + plane_model +
                        ", Manufacturer: " + manufacturer + ", Business Seats: " + business_seats +
                        ", Economy Seats: " + economy_seats);

            }
        }catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }
    }
    public void viewAllBookings() {
        try{
            String query = "SELECT * FROM booking";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            //Process the ResultSet and display planes
            while (resultSet.next())
            {
                int id = resultSet.getInt("id");
                int flight_id = resultSet.getInt("flight_id");
                int client_id = resultSet.getInt("client_id");
                boolean ispaid = resultSet.getBoolean("ispaid");
                boolean isreserved = resultSet.getBoolean("isreserved");
                float fees = resultSet.getFloat("fees");
                System.out.println("ID: " + id + ", Flight ID: " + flight_id + ", Client ID: " + client_id + ", Is Paid: " + ispaid + ", Is Reserved: " + isreserved + ", Fees: $" + fees);
            }
        }catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }
    }
    public void viewFlights() {
        try{
            String query = "SELECT * FROM flight";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            //Process the ResultSet and display planes
            while (resultSet.next())
            {
                int id = resultSet.getInt("id");
                int plane_id = resultSet.getInt("plane_id");
                String source = resultSet.getString("source");
                String destination = resultSet.getString("destination");
                Time arrival_time = resultSet.getTime("arrival_time");
                Time reporting_time = resultSet.getTime("reporting_time");
                float expense = resultSet.getFloat("expense");
                System.out.println("ID: " + id + ", Plane ID: " + plane_id + ", Source: " + source + ", Destination: " + destination + ", Arrival Time: " + arrival_time + ", Reporting Time: " + reporting_time + ", Expense: $" + expense);

            }
        }catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }
    }
    /// ///////////////////////////////////////////////////////////   ADD WALE
    public void addPlane(String planeModel, String manufacturer, int businessSeats, int economySeats) {
        try {
            String query = "INSERT INTO plane (admin_id, plane_model, manufacturer, business_seats, economy_seats) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, this.id);
            stmt.setString(2, planeModel);
            stmt.setString(3, manufacturer);
            stmt.setInt(4, businessSeats);
            stmt.setInt(5, economySeats);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Plane added successfully!");
            } else {
                System.out.println("Error: Plane not added.");
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    public void addFlight(int plane_id,String source,String destination,Time arrival_time,Time reporting_time,float expense) {
        try {
            String query = "INSERT INTO flight (plane_id,source,destination,arrival_time,reporting_time,expense) VALUES (?, ?, ?, ?, ?,?)";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1,plane_id);
            stmt.setString(2,source);
            stmt.setString(3,destination);
            stmt.setTime(4,arrival_time);
            stmt.setTime(5,reporting_time);
            stmt.setFloat(6,expense);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Flight added successfully!");
            } else {
                System.out.println("Error: Plane not added.");
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    /// //////////////////////////////////////////////////////////    UPDATE WALE
    public String updateFlight(int flightId, String field, String newValue) {
        try {
            // Validate field name to avoid SQL injection
            if (!field.matches("source|destination|arrival_time|reporting_time|expense")) {
                return "Invalid field name.";
            }

            String query = "UPDATE flight SET " + field + " = ? WHERE flight_id = ?";
            PreparedStatement stmt = connection.prepareStatement(query);

            // Set value based on field type
            switch (field) {
                case "expense":
                    stmt.setFloat(1, Float.parseFloat(newValue));
                    break;
                case "arrival_time":
                case "reporting_time":
                    stmt.setTime(1, Time.valueOf(newValue));
                    break;
                default:
                    stmt.setString(1, newValue);
                    break;
            }

            stmt.setInt(2, flightId);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                return "Flight updated successfully.";
            } else {
                return "No flight found with the given ID.";
            }

        } catch (SQLException e) {
            return "SQL Error: " + e.getMessage();
        } catch (IllegalArgumentException e) {
            return "Invalid format. Time must be HH:MM:SS or expense must be a number.";
        }
    }


}