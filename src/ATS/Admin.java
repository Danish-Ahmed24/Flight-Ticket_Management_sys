package ATS;

import java.sql.Connection;
import java.util.Scanner;

public class Admin {
    private int id;
    private String name,password,companyname,email;
    private float profit;
    private Connection connection;
    private Scanner scanner;

    public Admin(Connection connection, Scanner scanner)
    {
        this.scanner=scanner;
        this.connection=connection;
    }
    public void adminMenu(Admin admin)
    {
        System.out.println("Welcome ADMIN:-> " + admin.name);
        while (true) {
            System.out.println("1.  View Clients");
            System.out.println("2.  View Planes");
            System.out.println("3.  View All Bookings");
            System.out.println("4.  View All Flights");
            System.out.println("5.  Assign Flight");
            System.out.println("6.  Add Plane");
            System.out.println("7.  Add Flight");
            System.out.println("8.  Update Flight");
            System.out.println("9.  Another Update Flight");
            System.out.println("10. Log out");
            System.out.print("Enter: ");

            int option = scanner.nextInt();

            switch (option) {
                case 1:
                    System.out.println("Viewing Clients...");
                    break;
                case 2:
                    System.out.println("Viewing Planes...");
                    break;
                case 3:
                    System.out.println("Viewing All Bookings...");
                    break;
                case 4:
                    System.out.println("Viewing All Flights...");
                    break;
                case 5:
                    System.out.println("Assigning Flight...");
                    break;
                case 6:
                    System.out.println("Adding a New Plane...");
                    break;
                case 7:
                    System.out.println("Adding a New Flight...");
                    break;
                case 8:
                    System.out.println("Updating a Flight...");
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
}
