package ATS;

import java.sql.Connection;

public class Admin {
    private int id;
    private String name,password,companyname,email;
    private float profit;
    private Connection connection;

    public Admin(Connection connection)
    {
        this.connection=connection;
    }
}
