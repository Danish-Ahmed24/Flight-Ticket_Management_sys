import ATS.Admin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    private static final String url = "",
    username="",password="";
    public static void main(String[] args) {
        try {
            Connection connection = DriverManager.getConnection(url,username,password);
            Scanner scanner = new Scanner(System.in);
            //START

            Admin admin = new Admin(1,"d","d","d","d",connection,scanner,1,"d");
            admin.menu(admin);

            //END
            scanner.close();
            connection.close();
        }catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }
    }
}