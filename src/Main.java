import ATS.Admin;
import ATS.Client;
import ATS.IViewData;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
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
//                Admin admin = new Admin(1,"d","d","d",connection,scanner,1,"d");
//                admin.menu(admin);
                ArrayList<Client> clients= IViewData.getClients(connection);
                Client client= clients.get(1);
                client.menu(client);
            }catch (Exception e)
            {
                System.out.println(e.getMessage());
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