package ATS;

abstract class User<T extends User> {
    protected int id;
    protected String name;
    protected String password;
    protected String email;
    protected String role;

    public User(int id, String name, String password, String email, String role) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    public abstract void menu(T user) throws Exception;
}

