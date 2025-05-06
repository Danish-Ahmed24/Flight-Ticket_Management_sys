package ATS;

import Exceptions.*;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Scanner;

public class Admin extends User<Admin>{
    private String companyname;
    private float profit;
    private final Connection connection;
    private final Scanner scanner;
    //ye sab toh signup pe verify hongein
     public Admin(int id, String name, String password, String role, Connection connection, Scanner scanner, float profit, String companyname) { 
         super(id, name, password,companyname+"@ats.com", role); 
         this.connection = connection; 
         this.scanner = scanner;  
         this.profit = profit; 
         this.companyname = companyname;
     }
    @Override
    public void menu(Admin admin) throws Exception
    {
        System.out.println("Welcome ADMIN:-> " + admin.name);
        while (true) {
            System.out.println("1.  View Clients");//
            System.out.println("2.  View Planes");//
            System.out.println("3.  View All Bookings");//
            System.out.println("4.  View All Flights");//
            System.out.println("5.  Add Plane");//
            System.out.println("6.  Add Flight");//
            System.out.println("7.  Update Flight");
            System.out.println("8. Log out");
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
                    if(businessSeats<0){
                        throw new ValueLessThanZeroException("Business seats cant be negative");
                    }
                    System.out.print("Enter number of Economy Seats: ");
                    int economySeats = scanner.nextInt();

                    scanner.nextLine();
                    if(economySeats<0){
                        throw new ValueLessThanZeroException("Economy seats cant be negative");
                    }
                    admin.addPlane(planeModel, manufacturer, businessSeats, economySeats);
                    break;
                // Corrected Case 6 in the Main Menu
                case 6:
                    // Display all planes for reference
                    admin.viewPlanes();
                    System.out.println("Adding a New Flight...");
                    try {
                        System.out.print("Enter plane ID: ");
                        int plane_id = scanner.nextInt();
                        scanner.nextLine(); // Consume newline

                        // Check if plane ID is valid and non-negative
                        if (plane_id < 0) {
                            throw new ValueLessThanZeroException("Plane ID cannot be negative.");
                        }

                        System.out.print("Enter source: ");
                        String source = scanner.nextLine().trim();
                        if (source.isEmpty()) {
                            throw new IllegalArgumentException("Source cannot be empty.");
                        }

                        System.out.print("Enter destination: ");
                        String destination = scanner.nextLine().trim();
                        if (destination.isEmpty()) {
                            throw new IllegalArgumentException("Destination cannot be empty.");
                        }

                        // Arrival time input
                        System.out.println("Enter arrival time:");
                        System.out.print("  Hour (0-23): ");
                        int arrHour = scanner.nextInt();
                        System.out.print("  Minute (0-59): ");
                        int arrMinute = scanner.nextInt();
                        System.out.print("  Second (0-59): ");
                        int arrSecond = scanner.nextInt();
                        scanner.nextLine(); // Consume newline

                        LocalDate today = LocalDate.now();
                        LocalTime arrivalLocalTime = LocalTime.of(arrHour, arrMinute, arrSecond);
                        Timestamp arrival_time = Timestamp.valueOf(LocalDateTime.of(today, arrivalLocalTime));

                        // Reporting time input
                        System.out.println("Enter reporting time:");
                        System.out.print("  Hour (0-23): ");
                        int repHour = scanner.nextInt();
                        System.out.print("  Minute (0-59): ");
                        int repMinute = scanner.nextInt();
                        System.out.print("  Second (0-59): ");
                        int repSecond = scanner.nextInt();
                        scanner.nextLine(); // Consume newline

                        LocalTime reportingLocalTime = LocalTime.of(repHour, repMinute, repSecond);
                        Timestamp reporting_time = Timestamp.valueOf(LocalDateTime.of(today, reportingLocalTime));

                        // Expense input
                        System.out.print("Enter expense: ");
                        float expense = scanner.nextFloat();
                        scanner.nextLine(); // Consume newline
                        if (expense < 0) {
                            throw new ValueLessThanZeroException("Expense cannot be negative.");
                        }

                        // Call the addFlight method
                        admin.addFlight(plane_id, source, destination, arrival_time, reporting_time, expense);

                    } catch (Exception e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                    break;
                case 7:
                    //Update Flight
                    System.out.println("Updating flight");
                    System.out.print("Enter Flight ID: ");
                    int flightId = scanner.nextInt();
                    if(flightExists(flightId))
                    {
                        throw new FlightDoesntExistsExeption("Flight does not exists");
                    }
                    scanner.nextLine(); // Consume newline
                    if(flightId<0)
                    {
                        throw new ValueLessThanZeroException("Flight id cant be negative");
                    }
                    System.out.print("Enter field to update (source, destination, arrival_time, reporting_time, expense): ");
                    String field = scanner.nextLine();
                    if (!field.equals("source") &&
                            !field.equals("destination") &&
                            !field.equals("arrival_time") &&
                            !field.equals("reporting_time") &&
                            !field.equals("expense")) {
                        throw new IllegalArgumentException("Invalid field entered.");
                    }

                    try{
                    System.out.println(admin.updateFlight(flightId, field, scanner));
                    }catch (Exception e)
                    {
                        System.out.println(e.getMessage());
                    }
                        break;
                case 8://logout
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

    // Corrected addFlight Method
    public void addFlight(int plane_id, String source, String destination, Timestamp arrival_time, Timestamp reporting_time, float expense) {
        try {
            // Validate plane ownership
            String planeOwnershipQuery = "SELECT COUNT(*) FROM plane WHERE id = ? AND admin_id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(planeOwnershipQuery)) {
                stmt.setInt(1, plane_id);
                stmt.setInt(2, super.id);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next() && rs.getInt(1) == 0) {
                        throw new PlaneNotFoundException("The plane with ID " + plane_id + " does not belong to you.");
                    }
                }
            }

            // Validate source and destination
            if (source == null || source.trim().isEmpty()) {
                throw new IllegalArgumentException("Source cannot be null or empty.");
            }
            if (destination == null || destination.trim().isEmpty()) {
                throw new IllegalArgumentException("Destination cannot be null or empty.");
            }
            if (source.equalsIgnoreCase(destination)) {
                throw new IllegalArgumentException("Source and destination cannot be the same.");
            }

            // Validate timestamps
            if (arrival_time.before(new Timestamp(System.currentTimeMillis())) || reporting_time.before(new Timestamp(System.currentTimeMillis()))) {
                throw new IllegalArgumentException("Arrival and reporting times must be in the future.");
            }
            if (!arrival_time.after(reporting_time)) {
                throw new IllegalArgumentException("Arrival time must be after reporting time.");
            }

            // Check for overlapping flights
            String overlapQuery = "SELECT COUNT(*) FROM flight WHERE plane_id = ? AND (" +
                    "(? < arrival_time AND ? > reporting_time) OR " +
                    "(? BETWEEN reporting_time AND arrival_time) OR " +
                    "(? BETWEEN reporting_time AND arrival_time))";
            try (PreparedStatement stmt = connection.prepareStatement(overlapQuery)) {
                stmt.setInt(1, plane_id);
                stmt.setTimestamp(2, reporting_time);
                stmt.setTimestamp(3, arrival_time);
                stmt.setTimestamp(4, reporting_time);
                stmt.setTimestamp(5, arrival_time);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        throw new IllegalArgumentException("Plane is already assigned to an overlapping flight.");
                    }
                }
            }

            // Insert new flight
            String insertQuery = "INSERT INTO flight (plane_id, source, destination, arrival_time, reporting_time, expense) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(insertQuery)) {
                stmt.setInt(1, plane_id);
                stmt.setString(2, source.trim());
                stmt.setString(3, destination.trim());
                stmt.setTimestamp(4, arrival_time);
                stmt.setTimestamp(5, reporting_time);
                stmt.setFloat(6, expense);

                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Flight added successfully!");
                } else {
                    System.out.println("Failed to add flight.");
                }
            }

        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /// //////////////////////////////////////////////////////////    UPDATE WALE
    public String updateFlight(int flightId, String field, Scanner scanner) throws Exception{
        try {
            // Validate field name to avoid SQL injection
            if (!field.matches("source|destination|arrival_time|reporting_time|expense")) {
                return "Invalid field name.";
            }

            // Get current plane_id, arrival_time, and reporting_time for comparison
            String selectQuery = "SELECT plane_id, arrival_time, reporting_time FROM flight WHERE id = ?";
            PreparedStatement selectStmt = connection.prepareStatement(selectQuery);
            selectStmt.setInt(1, flightId);
            ResultSet rs = selectStmt.executeQuery();

            if (!rs.next()) return "Flight not found.";

            int planeId = rs.getInt("plane_id");
            Timestamp originalArrival = rs.getTimestamp("arrival_time");
            Timestamp originalReporting = rs.getTimestamp("reporting_time");

            // New values to update
            Timestamp newArrival = originalArrival;
            Timestamp newReporting = originalReporting;

            String query = "UPDATE flight SET " + field + " = ? WHERE id = ?";
            PreparedStatement stmt = connection.prepareStatement(query);

            switch (field) {
                case "expense":
                    System.out.print("Enter new expense: ");
                    float expense = Float.parseFloat(scanner.nextLine());
                    if(expense<0)
                    {
                        throw new ValueLessThanZeroException("Expense cannot be less than zero");
                    }
                    stmt.setFloat(1, expense);
                    break;

                case "arrival_time":
                case "reporting_time":
                    System.out.println("Enter new " + field.replace("_", " ") + ":");
                    System.out.print("  Hour (0–23): ");
                    int hour = Integer.parseInt(scanner.nextLine());
                    System.out.print("  Minute (0–59): ");
                    int minute = Integer.parseInt(scanner.nextLine());
                    System.out.print("  Second (0–59): ");
                    int second = Integer.parseInt(scanner.nextLine());
                    if (hour >= 0 && hour <= 23 && minute >= 0 && minute <= 59 && second >= 0 && second <= 59) {
                        throw new InvalidTimeException("Invalid Time");
                    }

                    LocalDate today = LocalDate.now(); // Assuming same day for simplicity
                    LocalTime newTime = LocalTime.of(hour, minute, second);
                    Timestamp newTimestamp = Timestamp.valueOf(LocalDateTime.of(today, newTime));

                    if (field.equals("arrival_time")) {
                        newArrival = newTimestamp;
                    } else {
                        newReporting = newTimestamp;
                    }

                    // Validate time order
                    if (!newArrival.after(newReporting)) {
                        return "Error: Arrival time must be after reporting time.";
                    }

                    // Check for time overlaps
                    String overlapQuery = "SELECT id FROM flight WHERE plane_id = ? AND id != ? AND NOT (arrival_time <= ? OR reporting_time >= ?)";
                    PreparedStatement overlapStmt = connection.prepareStatement(overlapQuery);
                    overlapStmt.setInt(1, planeId);
                    overlapStmt.setInt(2, flightId);
                    overlapStmt.setTimestamp(3, newReporting);
                    overlapStmt.setTimestamp(4, newArrival);

                    ResultSet overlapRs = overlapStmt.executeQuery();
                    if (overlapRs.next()) {
                        return "Error: Another flight with the same plane overlaps in time.";
                    }

                    stmt.setTimestamp(1, newTimestamp);
                    break;

                default:
                    System.out.print("Enter new value for " + field + ": ");
                    String newValue = scanner.nextLine();
                    stmt.setString(1, newValue);
                    break;
            }

            stmt.setInt(2, flightId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0 ? "Flight updated successfully." : "No flight found with the given ID.";

        } catch (SQLException e) {
            return "SQL Error: " + e.getMessage();
        } catch (Exception e) {
            return "Input Error: " + e.getMessage();
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
    public boolean planeExists(int planeId) {
        String sql = "SELECT COUNT(*) FROM plane WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, planeId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; // true if count > 0
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

        public boolean flightExists(int planeId, String source, String destination, Timestamp arrivalTime, Timestamp reportingTime, double expense) {
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
            System.out.println(e.getMessage());
        }
        return false;
    }
    public boolean flightExists(int flightId) {
        String sql = "SELECT COUNT(*) FROM flight WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, flightId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; // true if count > 0
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

}


//Overriding karle for updating
//Handle Flight UPDATE ......
