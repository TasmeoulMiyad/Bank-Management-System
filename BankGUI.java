import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class BankGUI extends JFrame {
    private Bank bank;
    private JTextArea displayArea;

    public BankGUI() {
        bank = new Bank();
        bank.loadFromFile();

        setTitle("Bank Management System");
        setSize(780, 560);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Auto-save when window closes
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                saveAndExit();
            }
        });

        // Top Header
        JLabel titleLabel = new JLabel("Bank Management System", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(titleLabel, BorderLayout.NORTH);

        // Center Output Display Area
        displayArea = new JTextArea();
        displayArea.setEditable(false);
        displayArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        JScrollPane scrollPane = new JScrollPane(displayArea);
        add(scrollPane, BorderLayout.CENTER);

        // Left Action Buttons Grid
        JPanel buttonPanel = new JPanel(new GridLayout(8, 1, 8, 8));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton btnCreate = new JButton("1. Create Account");
        JButton btnDeposit = new JButton("2. Deposit Funds");
        JButton btnWithdraw = new JButton("3. Withdraw Funds");
        JButton btnTransfer = new JButton("4. Inter-Account Transfer");
        JButton btnSearch = new JButton("5. Search Records");
        JButton btnHistory = new JButton("6. Transaction Ledger");
        JButton btnAll = new JButton("7. View All Accounts");
        JButton btnSaveExit = new JButton("8. Save & Exit");

        buttonPanel.add(btnCreate);
        buttonPanel.add(btnDeposit);
        buttonPanel.add(btnWithdraw);
        buttonPanel.add(btnTransfer);
        buttonPanel.add(btnSearch);
        buttonPanel.add(btnHistory);
        buttonPanel.add(btnAll);
        buttonPanel.add(btnSaveExit);
        add(buttonPanel, BorderLayout.WEST);

        // Action Listeners
        btnCreate.addActionListener(e -> createAccountDialog());
        btnDeposit.addActionListener(e -> depositDialog());
        btnWithdraw.addActionListener(e -> withdrawDialog());
        btnTransfer.addActionListener(e -> transferDialog());
        btnSearch.addActionListener(e -> searchDialog());
        btnHistory.addActionListener(e -> transactionDialog());
        btnAll.addActionListener(e -> displayAllAccounts());
        btnSaveExit.addActionListener(e -> saveAndExit());

        displayArea.setText("Welcome! System initialized. Loaded active accounts from storage.\nSelect an action on the left.\n");
    }

    private void createAccountDialog() {
        try {
            String[] types = {"Savings Account", "Current Account"};
            int typeChoice = JOptionPane.showOptionDialog(this, "Select Account Type", "Account Type",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, types, types[0]);
            if (typeChoice == JOptionPane.CLOSED_OPTION) return;

            String accNum = JOptionPane.showInputDialog(this, "Enter Account Number:");
            if (accNum == null || accNum.trim().isEmpty()) return;

            String custId = JOptionPane.showInputDialog(this, "Enter Customer ID:");
            String name = JOptionPane.showInputDialog(this, "Enter Customer Name:");
            String phone = JOptionPane.showInputDialog(this, "Enter Customer Phone:");
            String email = JOptionPane.showInputDialog(this, "Enter Customer Email:");
            double initialDeposit = Double.parseDouble(JOptionPane.showInputDialog(this, "Enter Initial Deposit ($):"));

            Customer customer = new Customer(custId, name, phone, email);

            if (typeChoice == 0) {
                double minBal = Double.parseDouble(JOptionPane.showInputDialog(this, "Enter Minimum Balance Floor ($):"));
                bank.addAccount(new SavingsAccount(accNum, customer, initialDeposit, minBal));
            } else {
                double overdraft = Double.parseDouble(JOptionPane.showInputDialog(this, "Enter Overdraft Limit Ceiling ($):"));
                bank.addAccount(new CurrentAccount(accNum, customer, initialDeposit, overdraft));
            }

            displayArea.append("\n[SUCCESS] Account " + accNum + " created for " + name + ".\n");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid number entered. Operation cancelled.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void depositDialog() {
        try {
            String accNum = JOptionPane.showInputDialog(this, "Enter Account Number:");
            Account acc = bank.findAccount(accNum);
            if (acc == null) {
                JOptionPane.showMessageDialog(this, "Account not found.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            double amount = Double.parseDouble(JOptionPane.showInputDialog(this, "Enter Deposit Amount ($):"));
            acc.deposit(amount);
            displayArea.append("\n[DEPOSIT] $" + amount + " deposited into Account " + acc.getAccountNumber() + ". Current Balance: $" + acc.getBalance() + "\n");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid input: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void withdrawDialog() {
        try {
            String accNum = JOptionPane.showInputDialog(this, "Enter Account Number:");
            Account acc = bank.findAccount(accNum);
            if (acc == null) {
                JOptionPane.showMessageDialog(this, "Account not found.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            double amount = Double.parseDouble(JOptionPane.showInputDialog(this, "Enter Withdrawal Amount ($):"));
            acc.withdraw(amount);
            displayArea.append("\n[WITHDRAWAL] $" + amount + " withdrawn from Account " + acc.getAccountNumber() + ". Current Balance: $" + acc.getBalance() + "\n");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Transaction Denied", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void transferDialog() {
        try {
            String from = JOptionPane.showInputDialog(this, "Enter Sender Account Number:");
            String to = JOptionPane.showInputDialog(this, "Enter Recipient Account Number:");
            double amount = Double.parseDouble(JOptionPane.showInputDialog(this, "Enter Transfer Amount ($):"));

            bank.transfer(from, to, amount);
            displayArea.append("\n[TRANSFER] Successfully transferred $" + amount + " from " + from + " to " + to + ".\n");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Transfer Failed", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void searchDialog() {
        String query = JOptionPane.showInputDialog(this, "Enter Account Number or Customer Name to Search:");
        if (query == null || query.trim().isEmpty()) return;

        Account acc = bank.findAccount(query);
        displayArea.setText("--- SEARCH RESULTS ---\n");
        if (acc != null) {
            displayAccountRecord(acc);
            return;
        }

        ArrayList<Account> matches = bank.searchByName(query);
        if (!matches.isEmpty()) {
            for (Account a : matches) {
                displayAccountRecord(a);
            }
        } else {
            displayArea.append("No accounts found matching '" + query + "'.\n");
        }
    }

    private void transactionDialog() {
        String accNum = JOptionPane.showInputDialog(this, "Enter Account Number:");
        Account acc = bank.findAccount(accNum);
        if (acc == null) {
            JOptionPane.showMessageDialog(this, "Account not found.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        displayArea.setText("--- TRANSACTION LEDGER: ACCOUNT " + acc.getAccountNumber() + " ---\n");
        for (Transaction t : acc.getTransactions()) {
            displayArea.append(t.getDetails() + "\n");
        }
    }

    private void displayAllAccounts() {
        ArrayList<Account> accounts = bank.getAllAccounts();
        displayArea.setText("--- ALL ACTIVE ACCOUNTS (" + accounts.size() + ") ---\n");
        if (accounts.isEmpty()) {
            displayArea.append("No active accounts registered in system.\n");
            return;
        }
        for (Account acc : accounts) {
            displayAccountRecord(acc);
        }
    }

    private void displayAccountRecord(Account acc) {
        String type = (acc instanceof SavingsAccount) ? "Savings" : "Current";
        displayArea.append("Type: " + type + " | Acc No: " + acc.getAccountNumber() + " | Balance: $" + acc.getBalance() + "\n");
        displayArea.append("Holder: " + acc.getCustomer().getDisplayInfo() + "\n");
        displayArea.append("----------------------------------------------------------------------------------\n");
    }

    private void saveAndExit() {
        try {
            bank.saveToFile();
            JOptionPane.showMessageDialog(this, "All records saved to accounts.txt. Exiting!", "Saved", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving records: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        System.exit(0);
    }

}