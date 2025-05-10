import ATS.User;
import Exceptions.UserNotFoundException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    private static final String url = "jdbc:mysql://localhost:3306/ats";
    private static final String username = "root";
    private static final String password = "rafay2022";

    public static void main(String[] args) {
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            Scanner sc = new Scanner(System.in);

            while (true) {
                System.out.print("\nDo you want to signup, signin, or exit? (signup/signin/exit): ");
                String choice = sc.nextLine().trim().toLowerCase();

                if (choice.equals("exit")) {
                    System.out.println("Exiting program. Goodbye!");
                    break;
                }

                try {
                    switch (choice) {
                        case "signup":
                            System.out.print("Enter usertype (admin/client): ");
                            String usertype = sc.nextLine();

                            System.out.print("Enter name: ");
                            String name = sc.nextLine();

                            System.out.print("Enter password: ");
                            String signupPassword = sc.nextLine();
                            String signupEmail;
                            if(usertype.equals("client"))
                            {
                             signupEmail = name+"@client.com";
                            }
                                else {
                                    signupEmail = name+"@ats.com";
                            }
                            User.signup(connection, usertype, name, signupPassword, signupEmail);
                            break;

                        case "signin":
                            System.out.print("Enter email: ");
                            String signinEmail = sc.nextLine();

                            System.out.print("Enter password: ");
                            String signinPassword = sc.nextLine();

                            User.signin(connection, signinEmail, signinPassword);
                            break;

                        default:
                            System.out.println("Invalid choice. Please enter 'signup', 'signin', or 'exit'.");
                    }
                } catch (UserNotFoundException e) {
                    System.out.println(e.getMessage());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            sc.close();
            connection.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
