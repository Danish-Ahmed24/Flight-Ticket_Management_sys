import ATS.Admin;
import ATS.Client;
import ATS.User;
import Exceptions.UserNotFoundException;

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
            //START
            try {
                User.signin(connection,"admin@airjet.com","admin123");
            } catch (UserNotFoundException e) {
                System.out.println(e.getMessage());
                // Handle invalid login
            } catch (Exception e) {
                e.printStackTrace();
                // Handle other exceptions
            }
            //END
            scanner.close();
            connection.close();
        }catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }
    }
}