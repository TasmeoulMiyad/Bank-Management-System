import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // Launches the Swing GUI
        SwingUtilities.invokeLater(() -> {
            new BankGUI().setVisible(true);
        });
    }
}