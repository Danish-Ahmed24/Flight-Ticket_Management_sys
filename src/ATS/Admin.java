package ATS;

import java.sql.*;
import java.time.LocalDate;
import java.util.Scanner;

public class Admin extends User<Admin>{
    private String companyname;
    private float profit;
    private final Connection connection;
    private final Scanner scanner;

    public Admin(int id, String name, String password, String email, String role, Connection connection, Scanner scanner, float profit, String companyname) {
        super(id, name, password, email, role);
        this.connection = connection;
        this.scanner = scanner;
        this.profit = profit;
        this.companyname = companyname;
    }

    @Override
    public void menu(Admin admin)
    {
        System.out.println("Welcome ADMIN:-> " + admin.name);
        while (true) {
            System.out.println("1.  View Clients");//
            System.out.println("2.  View Planes");//
            System.out.println("3.  View All Bookings");//
            System.out.println("4.  View All Flights");//
//            System.out.println("5.  Assign Flight");
            System.out.println("6.  Add Plane");//
            System.out.println("7.  Add Flight");//
            System.out.println("8.  Update Flight");
            System.out.println("9.  Update Plane");
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
//                case 5:
//                    System.out.println("Assigning Flight...");
//                    admin.assignFlight();
//                    break;
                case 6:
                    //Add Plane
                    scanner.nextLine();
                    System.out.print("Enter Plane Model: ");
                    String planeModel = scanner.nextLine();

                    System.out.print("Enter Manufacturer: ");
                    String manufacturer = scanner.nextLine();
                    if(admin.planeExists(planeModel,manufacturer))
                    {
                        System.out.println("Plane already exists...");
                        break;
                    }
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
                    Timestamp arrival_time = Timestamp.valueOf(LocalDate.now() + " " + scanner.nextLine());
                    System.out.print("Enter reporting time (HH:MM:SS): ");
                    String reportingStr = scanner.nextLine();
                    Timestamp reporting_time = Timestamp.valueOf(LocalDate.now() + " " + scanner.nextLine());
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
                    System.out.println(admin.updateFlight(flightId, field, newValue));
                    break;
                case 9:
                    //Update Plane
                    System.out.println("Updating a Plane...");
                    Scanner scanner = new Scanner(System.in);
                    System.out.print("Enter Plane ID to update: ");
                    int planeId = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Enter new Plane Model: ");
                    String updatePlaneModel = scanner.nextLine();
                    System.out.print("Enter new Manufacturer: ");
                    String updateManufacturer = scanner.nextLine();
                    System.out.print("Enter number of Business Seats: ");
                    int updateBusinessSeats = scanner.nextInt();
                    System.out.print("Enter number of Economy Seats: ");
                    int updateEconomySeats = scanner.nextInt();
                    System.out.println(admin.updatePlane(planeId, updatePlaneModel, updateManufacturer, updateBusinessSeats, updateEconomySeats));
                    break;
                case 10://logout
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
            stmt.setInt(1, super.id);
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
    public void addFlight(int plane_id, String source, String destination, Timestamp arrival_time, Timestamp reporting_time, float expense) {
        try {
            String query = "INSERT INTO flight (plane_id, source, destination, arrival_time, reporting_time, expense) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setInt(1, plane_id);
            stmt.setString(2, source);
            stmt.setString(3, destination);
            stmt.setTimestamp(4, arrival_time);
            stmt.setTimestamp(5, reporting_time);
            stmt.setFloat(6, expense);

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

            String query = "UPDATE flight SET " + field + " = ? WHERE id = ?";
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
    public String updatePlane(int planeId, String updatePlaneModel, String updateManufacturer, int updateBusinessSeats, int updateEconomySeats) {
        try {
            String query = "UPDATE plane SET plane_model = ?, manufacturer = ?, business_seats = ?, economy_seats = ? WHERE id = ? AND admin_id = ?";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, updatePlaneModel);
            stmt.setString(2, updateManufacturer);
            stmt.setInt(3, updateBusinessSeats);
            stmt.setInt(4, updateEconomySeats);
            stmt.setInt(5, planeId);
            stmt.setInt(6, this.id); // Ensures only the admin who added it can update

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                return "Plane updated successfully.";
            } else {
                return "No plane found with the given ID for this admin.";
            }
        } catch (SQLException e) {
            return "SQL Error: " + e.getMessage();
        }
    }
    /// //////////////////////////////////////////////////////////     ASSIGN WALE
    //assign plane code below
    //    public void assignFlight() {
//        try {
//            // Step 1: Display all flights that are not assigned a plane (plane_id is NULL)
//            String flightQuery = "SELECT id, source, destination FROM flight WHERE plane_id IS NULL";
//            PreparedStatement flightStmt = connection.prepareStatement(flightQuery);
//            ResultSet flightRs = flightStmt.executeQuery();
//
//            List<Integer> unassignedFlightIds = new ArrayList<>();
//            System.out.println("Unassigned Flights:");
//            while (flightRs.next()) {
//                int flightId = flightRs.getInt("id");
//                String source = flightRs.getString("source");
//                String destination = flightRs.getString("destination");
//                System.out.println("Flight ID: " + flightId + ", Source: " + source + ", Destination: " + destination);
//                unassignedFlightIds.add(flightId);
//            }
//
//            if (unassignedFlightIds.isEmpty()) {
//                System.out.println("No unassigned flights available.");
//                return;
//            }
//
//            // Step 2: Let the user select a flight to assign a plane
//            Scanner scanner = new Scanner(System.in);
//            System.out.print("Select a flight ID to assign a plane: ");
//            int selectedFlightId = scanner.nextInt();
//            if (!unassignedFlightIds.contains(selectedFlightId)) {
//                System.out.println("Invalid Flight ID.");
//                return;
//            }
//
//            // Step 3: Display all planes that are not assigned to any flight
//            String planeQuery = "SELECT id, plane_model FROM plane WHERE id NOT IN (SELECT plane_id FROM flight WHERE plane_id IS NOT NULL)";
//            PreparedStatement planeStmt = connection.prepareStatement(planeQuery);
//            ResultSet planeRs = planeStmt.executeQuery();
//
//            List<Integer> availablePlaneIds = new ArrayList<>();
//            System.out.println("Available Planes:");
//            while (planeRs.next()) {
//                int planeId = planeRs.getInt("id");
//                String planeModel = planeRs.getString("plane_model");
//                System.out.println("Plane ID: " + planeId + ", Model: " + planeModel);
//                availablePlaneIds.add(planeId);
//            }
//
//            if (availablePlaneIds.isEmpty()) {
//                System.out.println("No available planes to assign.");
//                return;
//            }
//
//            // Step 4: Let the user select a plane to assign to the flight
//            System.out.print("Select a plane ID to assign to the flight: ");
//            int selectedPlaneId = scanner.nextInt();
//            if (!availablePlaneIds.contains(selectedPlaneId)) {
//                System.out.println("Invalid Plane ID.");
//                return;
//            }
//
//            // Step 5: Update the flight with the selected plane_id
//            String updateFlightQuery = "UPDATE flight SET plane_id = ? WHERE id = ?";
//            PreparedStatement updateStmt = connection.prepareStatement(updateFlightQuery);
//            updateStmt.setInt(1, selectedPlaneId);
//            updateStmt.setInt(2, selectedFlightId);
//
//            int rowsAffected = updateStmt.executeUpdate();
//            if (rowsAffected > 0) {
//                System.out.println("Flight " + selectedFlightId + " has been successfully assigned to Plane " + selectedPlaneId);
//            } else {
//                System.out.println("Error assigning the plane to the flight.");
//            }
//        } catch (SQLException e) {
//            System.out.println("Error: " + e.getMessage());
//        }
//    } a
    /// //////////////////////////////////////////////////////////     CHECKERS
    public boolean planeExists(String model, String manufacturer) {
        String sql = "SELECT COUNT(*) FROM plane WHERE plane_model = ? AND manufacturer = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, model);
            stmt.setString(2, manufacturer);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; // true if count > 0
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean flightExists(int planeId, String source, String destination,
                                Timestamp arrivalTime, Timestamp reportingTime, double expense) {
        String sql = "SELECT COUNT(*) FROM flight WHERE plane_id = ? AND source = ? AND destination = ? " +
                "AND arrival_time = ? AND reporting_time = ? AND expense = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, planeId);
            stmt.setString(2, source);
            stmt.setString(3, destination);
            stmt.setTimestamp(4, arrivalTime);
            stmt.setTimestamp(5, reportingTime);
            stmt.setDouble(6, expense);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


}



//Handle Flight ADD ......