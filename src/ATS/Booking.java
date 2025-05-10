package ATS;

import java.sql.Timestamp;

public class Booking {
    private int id;
    private int planeId;
    private String source;
    private String destination;
    private Timestamp arrivalTime;
    private Timestamp reportingTime;
    private float expense;

    public Booking(int id, int planeId, String source, String destination, Timestamp arrivalTime, Timestamp reportingTime, float expense) {
        this.id = id;
        this.planeId = planeId;
        this.source = source;
        this.destination = destination;
        this.arrivalTime = arrivalTime;
        this.reportingTime = reportingTime;
        this.expense = expense;
    }
}
