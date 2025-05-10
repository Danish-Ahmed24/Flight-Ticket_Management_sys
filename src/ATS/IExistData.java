package ATS;

import java.sql.*;

public interface IExistData {
    static public boolean planeExists(Connection connection, String model, String manufacturer) {
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
    static public boolean planeExists(Connection connection, int planeId) {
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
    static public boolean flightExists(Connection connection, int planeId, String source, String destination, Timestamp arrivalTime, Timestamp reportingTime, double expense) {
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
    static public boolean flightExists(Connection connection, int flightId) {
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
