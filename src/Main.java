import ATS.Admin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    private static final String url = "jdbc:mysql://localhost:3306/ats",
    username="root",password="rafay2022";
    public static void main(String[] args) {
        try {
            Connection connection = DriverManager.getConnection(url,username,password);
            Scanner scanner = new Scanner(System.in);
            Admin admin = new Admin(connection,scanner);
            admin.adminMenu(admin);
        }catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }
    }
}