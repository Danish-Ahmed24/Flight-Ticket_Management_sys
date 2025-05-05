package ATS;

import java.sql.*;

public interface ViewData {
    static void viewClients(Connection connection) {
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
    static void viewPlanes(Connection connection) {
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
    static void viewAllBookings(Connection connection) {
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
    static void viewFlights(Connection connection) {
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
}
