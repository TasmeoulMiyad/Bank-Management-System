import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Transaction {
    private static int counter = 1000;
    private String transId;
    private String type;
    private double amount;
    private String date;

    public Transaction(String type, double amount) {
        this.transId = "TXN" + (++counter);
        this.type = type;
        this.amount = amount;
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.date = dtf.format(LocalDateTime.now());
    }

    public String getDetails() {
        return String.format("[%s] ID: %s | %-16s | $%.2f", date, transId, type, amount);
    }
}