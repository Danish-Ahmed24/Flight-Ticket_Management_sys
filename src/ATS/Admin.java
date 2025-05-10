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


    public void setProfit(float profit) {
        this.profit = profit;
    }

    public void setCompanyname(String companyname) {
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
                    IViewData.viewClients(connection);//view Client
                    break;
                case 2:
                    IViewData.viewPlanes(connection);//view Planes
                    break;
                case 3:
                    IViewData.viewAllBookings(connection);// view Bookings
                    break;
                case 4:
                    IViewData.viewFlights(connection);//view Flights
                    break;
                case 5:
                    //Add Plane
                    scanner.nextLine();
                    System.out.print("Enter Plane Model: ");
                    String planeModel = scanner.nextLine();

                    System.out.print("Enter Manufacturer: ");
                    String manufacturer = scanner.nextLine();
                    if(IExistData.planeExists(connection,planeModel,manufacturer))
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
                case 6:
                    //addFlight
                    System.out.println("Adding a New Flight...");
                    System.out.print("Enter plane ID: ");
                    try {
                        int plane_id = scanner.nextInt();
                        scanner.nextLine(); // consume newline
                        if(!IExistData.planeExists(connection,plane_id)){
                            throw new PlaneNotFoundException("Plane not available");
                        }
                        if(plane_id<0)
                        {
                            throw new ValueLessThanZeroException("Id cant be negative");
                        }
                        System.out.print("Enter source: ");
                        String source = scanner.nextLine();

                        System.out.print("Enter destination: ");
                        String destination = scanner.nextLine();

                        // Arrival time
                        System.out.println("Enter arrival time:");
                        System.out.print("  Hour (0-23): ");
                        int arrHour = scanner.nextInt();
                        System.out.print("  Minute (0-59): ");
                        int arrMinute = scanner.nextInt();
                        System.out.print("  Second (0-59): ");
                        int arrSecond = scanner.nextInt();
                        scanner.nextLine(); // consume newline
                        if (arrHour < 0 || arrHour > 23 || arrMinute < 0 || arrMinute > 59 || arrSecond < 0 || arrSecond > 59) {
                            throw new InvalidTimeException("Invalid Time");
                        }
                        LocalDate today = LocalDate.now();
                        LocalTime arrivalLocalTime = LocalTime.of(arrHour, arrMinute, arrSecond);
                        Timestamp arrival_time = Timestamp.valueOf(today.atTime(arrivalLocalTime));

                        // Reporting time
                        System.out.println("Enter reporting time:");
                        System.out.print("  Hour (0-23): ");
                        int repHour = scanner.nextInt();
                        System.out.print("  Minute (0-59): ");
                        int repMinute = scanner.nextInt();
                        System.out.print("  Second (0-59): ");
                        int repSecond = scanner.nextInt();
                        scanner.nextLine(); // consume newline
                        if (repHour < 0 || repHour > 23 || repMinute < 0 || repMinute > 59 || repSecond < 0 || repSecond > 59) {
                            throw new InvalidTimeException("Invalid Time");
                        }
                        LocalTime reportingLocalTime = LocalTime.of(repHour, repMinute, repSecond);
                        Timestamp reporting_time = Timestamp.valueOf(today.atTime(reportingLocalTime));

                        System.out.print("Enter expense: ");
                        float expense = scanner.nextFloat();
                        scanner.nextLine(); // optional, for safety
                        if(expense<0)
                        {
                            throw  new ValueLessThanZeroException("Expense cant be negative");
                        }
                        if(IExistData.flightExists(connection,plane_id,source,destination,arrival_time,reporting_time,expense)){
                            throw new FlightAlreadyExistsException("Flight already exists");
                        }
                        // Pass to your method
                        admin.addFlight(plane_id, source, destination, arrival_time, reporting_time, expense);

                    } catch (Exception e) {
                        System.out.println("Invalid input: " + e.getMessage());
                    }


                    break;
                case 7:
                    //Update Flight
                    System.out.println("Updating flight");
                    System.out.print("Enter Flight ID: ");
                    int flightId = scanner.nextInt();
                    if(!IExistData.flightExists(connection,flightId))
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
    /// ///////////////////////////////////////////////////////////   PATA NHI INKO KAHAN DALON
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
            // ✅ Validate time range
            if (!arrival_time.after(reporting_time)) {
                System.out.println("Error: Arrival time must be after reporting time.");
                return;
            }

            // ✅ Check for overlapping flights
            String checkQuery = "SELECT COUNT(*) FROM flight WHERE plane_id = ? AND (" +
                    "? < arrival_time AND ? > reporting_time OR " +  // New completely covers existing
                    "? BETWEEN reporting_time AND arrival_time OR " + // New reporting_time overlaps
                    "? BETWEEN reporting_time AND arrival_time" +     // New arrival_time overlaps
                    ")";
            PreparedStatement checkStmt = connection.prepareStatement(checkQuery);
            checkStmt.setInt(1, plane_id);
            checkStmt.setTimestamp(2, reporting_time);
            checkStmt.setTimestamp(3, arrival_time);
            checkStmt.setTimestamp(4, reporting_time);
            checkStmt.setTimestamp(5, arrival_time);

            ResultSet rs = checkStmt.executeQuery();
            rs.next();
            int conflictCount = rs.getInt(1);

            if (conflictCount > 0) {
                System.out.println("Error: Plane is already assigned to an overlapping flight.");
                return;
            }

            // ✅ Insert flight
            String insertQuery = "INSERT INTO flight (plane_id, source, destination, arrival_time, reporting_time, expense) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = connection.prepareStatement(insertQuery);
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
                System.out.println("Error: Flight not added.");
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public String updateFlight(int flightId, String field, Scanner scanner) throws Exception{
        /// //////////////////////////////////////////////////////////    UPDATE WALE
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
                    if (hour < 0 || hour > 23 || minute < 0 || minute > 59 || second < 0 || second > 59) {
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
}
