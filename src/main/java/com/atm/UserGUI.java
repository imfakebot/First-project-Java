package com.atm;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.text.SimpleDateFormat;
import java.util.List;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class UserGUI extends JFrame implements handleImageResizing {
    private final String username;
    private final String accountID;
    private final DatabaseHelper databaseHelper;
    private final double CurrentBalance;
    private final String CustomerID;
    private JTextField newUserNameField;
    private JPasswordField oldPasswordField;
    private JPasswordField newPasswordField;
    private JPasswordField confirmPasswordField;

    public UserGUI(String username, String accountID, double CurrentBalance, String CustomerID) {
        super("ATM");
        this.username = username;
        this.accountID = accountID;
        this.databaseHelper = new DatabaseHelper();
        this.CurrentBalance = CurrentBalance;
        this.CustomerID = CustomerID;

        setSize(1910, 1080);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setMinimumSize(new Dimension(990, 650));
        setIconImage(new ImageIcon(getClass().getResource(
                "/com/atm/icon-removebg-preview.png")).getImage());

        UIManager.put("TabbedPane.contentAreaColor", Color.WHITE);
        UIManager.put("TabbedPane.selected", new Color(47, 61, 160));
        UIManager.put("TabbedPane.tabInsets", new java.awt.Insets(10, 60, 10, 60));

        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new BorderLayout());

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setTabPlacement(SwingConstants.LEFT);
        tabbedPane.setOpaque(true);
        tabbedPane.setBackground(new Color(221, 225, 255));
        tabbedPane.setForeground(new Color(15, 20, 56));

        JPanel transactionPanel = TransactionPanel();
        JPanel historyPanel = HistoryPanel();
        JPanel changePasswordPanelOrUserName = ChangePasswordOrUserNamePanel();

        tabbedPane.addTab("Transaction", transactionPanel);
        tabbedPane.addTab("Transaction History", historyPanel);
        tabbedPane.addTab("Change Password or Username", changePasswordPanelOrUserName);

        RoundedButton logoutButton = new RoundedButton("Logout", 47, 61, 160);
        logoutButton.setFont(new Font("Arial", Font.BOLD, 14));
        logoutButton.setBackground(new Color(47, 61, 160));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFocusPainted(false);
        logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutButton.setActionCommand("Logout");
        logoutButton.addActionListener(new ButtonHandler(this));

        sidePanel.add(tabbedPane, BorderLayout.CENTER);
        sidePanel.add(logoutButton, BorderLayout.SOUTH);

        JPanel mainPanel = new JPanel(new BorderLayout());

        JLabel headerLabel = new JLabel("ATM System - Welcome", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 20));
        headerLabel.setOpaque(true);
        headerLabel.setBackground(new Color(47, 61, 160));
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setPreferredSize(new Dimension(0, 50));

        mainPanel.add(headerLabel, BorderLayout.NORTH);
        mainPanel.add(sidePanel, BorderLayout.CENTER);

        add(mainPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    /**
     * Creates and returns a JPanel containing the transaction interface for the ATM
     * system.
     * This panel includes a welcome message with dynamic background, current time
     * display,
     * and buttons for various transaction operations such as deposit, withdraw,
     * transfer, and QR codes.
     *
     * The panel is divided into two main sections:
     * 1. A welcome panel with a greeting that changes based on the time of day and
     * displays the current time.
     * 2. A transaction panel with buttons for different operations.
     *
     * @return JPanel A fully configured panel for the transaction interface.
     *         The returned panel includes:
     *         - A dynamic background that changes based on the time of day
     *         - A welcome message that greets the user based on the time of day
     *         - A current time display that updates every second
     *         - Buttons for Deposit, Withdraw, Transfer, and QR Codes operations
     */
    private JPanel TransactionPanel() {
        JPanel transactionPanel = new JPanel();
        transactionPanel.setLayout(new GridLayout(2, 1, 2, 2));

        DynamicBackgroundPanel welcomePanel = new DynamicBackgroundPanel();
        welcomePanel.setLayout(new BorderLayout());

        JLabel welcomeLabel = new JLabel("", SwingConstants.LEFT);
        welcomeLabel.setVerticalAlignment(SwingConstants.CENTER);
        welcomeLabel.setForeground(new Color(15, 20, 56));
        welcomeLabel.setFont(new Font("Bauhaus 93", Font.PLAIN, 24));

        JLabel timeLabel = new JLabel("", SwingConstants.RIGHT);
        timeLabel.setVerticalAlignment(SwingConstants.CENTER);
        timeLabel.setForeground(new Color(15, 20, 56));
        timeLabel.setFont(new Font("Bauhaus 93", Font.PLAIN, 24));

        Timer timer = new Timer(1000, (ActionEvent _) -> {
            String currentTime = new SimpleDateFormat("HH:mm:ss").format(new java.util.Date());
            timeLabel.setText("Current Time: " + currentTime);

            int hour = Integer.parseInt(new SimpleDateFormat("HH").format(new java.util.Date()));
            String greeting;
            String backgroundPath;

            if (hour >= 5 && hour < 12) {
                greeting = "Good Morning";
                backgroundPath = "src\\main\\java\\com\\atm\\M-commerce concept.jpg";
            } else if (hour >= 12 && hour < 18) {
                greeting = "Good Afternoon";
                backgroundPath = "src\\main\\java\\com\\atm\\M-commerce concept.jpg";
            } else {
                greeting = "Good Evening";
                backgroundPath = "src\\main\\java\\com\\atm\\M-commerce concept.jpg";
            }

            welcomeLabel.setText("<html>" + greeting + "<br>" + username + "</html>");
            welcomePanel.setBackgroundImage(backgroundPath);
        });
        timer.start();

        JPanel horizontalPanel = new JPanel();
        horizontalPanel.setLayout(new GridLayout(1, 2));
        horizontalPanel.setOpaque(false);
        horizontalPanel.add(welcomeLabel);
        horizontalPanel.add(timeLabel);

        welcomePanel.add(horizontalPanel, BorderLayout.NORTH);

        JPanel depositPanel = new JPanel();
        depositPanel.setLayout(new GridLayout(2, 2, 30, 30));
        depositPanel.setOpaque(false);

        RoundedButton depositButton = new RoundedButton("Deposit", 152 + 40 - 20, 156 + 40 - 20, 255);
        depositButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        depositButton.setActionCommand("depositButton");
        depositButton.addActionListener(new ButtonHandler(this));

        RoundedButton withdrawButton = new RoundedButton("Withdraw", 152 + 40 - 20, 156 + 40 - 20, 255);
        withdrawButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        withdrawButton.setActionCommand("withdrawButton");
        withdrawButton.addActionListener(new ButtonHandler(this));

        RoundedButton transferButton = new RoundedButton("Transfer", 152 + 40 - 20, 156 + 40 - 20, 255);
        transferButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        transferButton.setActionCommand("transferButton");
        transferButton.addActionListener(new ButtonHandler(this));

        RoundedButton QRcodesButton = new RoundedButton("QR Codes", 152 + 40 - 20, 156 + 40 - 20, 255);
        QRcodesButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        QRcodesButton.setActionCommand("QRcodesButton");
        QRcodesButton.addActionListener(new ButtonHandler(this));

        depositPanel.add(withdrawButton);
        depositPanel.add(depositButton);
        depositPanel.add(transferButton);
        depositPanel.add(QRcodesButton);

        transactionPanel.add(welcomePanel);
        transactionPanel.add(depositPanel);

        return transactionPanel;
    }

    /**
     * Creates and returns a JPanel containing the user interface for displaying the
     * transaction history.
     *
     * @return JPanel containing the transaction history user interface.
     *         The returned JPanel is used to display the transaction history table,
     *         filter options, and search functionality.
     */
    private JPanel HistoryPanel() {
        JPanel historyPanel = new JPanel();
        historyPanel.setLayout(new BorderLayout());
        historyPanel.setBackground(new Color(240, 248, 255));

        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new GridLayout(1, 2, 10, 10));
        headerPanel.setBackground(new Color(70, 130, 180));

        RoundedPanel accountIDPanel = new RoundedPanel(20);
        accountIDPanel.setLayout(new BorderLayout());
        accountIDPanel.setBackground(new Color(100, 149, 237));

        JLabel accountIDLabel = new JLabel("Account's ID: " + accountID);
        accountIDLabel.setForeground(Color.WHITE);
        accountIDLabel.setFont(accountIDLabel.getFont().deriveFont(22f));
        accountIDLabel.setHorizontalAlignment(SwingConstants.CENTER);
        accountIDPanel.add(accountIDLabel, BorderLayout.CENTER);

        RoundedPanel balancePanel = new RoundedPanel(20);
        balancePanel.setLayout(new BorderLayout());
        balancePanel.setBackground(new Color(72, 209, 204));

        JLabel balanceLabel = new JLabel("Balance: $" + CurrentBalance);
        balanceLabel.setForeground(Color.WHITE);
        balanceLabel.setFont(balanceLabel.getFont().deriveFont(22f));
        balanceLabel.setHorizontalAlignment(SwingConstants.CENTER);
        balancePanel.add(balanceLabel, BorderLayout.CENTER);

        headerPanel.add(accountIDPanel);
        headerPanel.add(balancePanel);

        JPanel filterPanel = new JPanel();
        filterPanel.setLayout(new GridLayout(2, 3, 10, 10));
        filterPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        filterPanel.setBackground(new Color(240, 248, 255));

        JTextField searchField = new JTextField();
        searchField.setFont(new Font("Arial", Font.PLAIN, 16));
        searchField.setBorder(BorderFactory.createTitledBorder("Search by Content"));
        filterPanel.add(searchField);

        JComboBox<String> filterTypeCombo = new JComboBox<>(new String[] { "All", "Deposit", "Withdraw", "Transfer" });
        filterTypeCombo.setFont(new Font("Arial", Font.PLAIN, 16));
        filterTypeCombo.setBorder(BorderFactory.createTitledBorder("Filter by Type"));
        filterPanel.add(filterTypeCombo);

        JTextField senderReceiverField = new JTextField();
        senderReceiverField.setFont(new Font("Arial", Font.PLAIN, 16));
        senderReceiverField.setBorder(BorderFactory.createTitledBorder("Search by Sender/Receiver"));
        filterPanel.add(senderReceiverField);

        JButton applyFilterButton = new JButton("Apply Filter");
        applyFilterButton.setFont(new Font("Arial", Font.BOLD, 14));
        applyFilterButton.setBackground(new Color(30, 144, 255));
        applyFilterButton.setForeground(Color.WHITE);
        applyFilterButton.setFocusPainted(false);
        filterPanel.add(applyFilterButton);

        String[] columnNames = {
                "Date",
                "Transaction Type",
                "Amount",
                "Balance After",
                "Sender's Full Name",
                "Receiver's Full Name",
                "Transaction Content"
        };

        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        List<DatabaseHelper.TransactionHistory> transactions = databaseHelper.getTransactionHistory(CustomerID);
        if (!transactions.isEmpty()) {
            for (DatabaseHelper.TransactionHistory transaction : transactions) {
                Object[] row = {
                        transaction.getDate(),
                        transaction.getTransactionType(),
                        transaction.getAmount(),
                        transaction.getBalanceAfter(),
                        transaction.getSenderFullName(),
                        transaction.getReceiverFullName(),
                        transaction.getTransactionContent()
                };
                model.addRow(row);
            }
        }

        JTable historyTable = new JTable(model);
        historyTable.setRowHeight(30);
        historyTable.setFont(new Font("Arial", Font.PLAIN, 16));
        historyTable.setShowGrid(false);
        historyTable.setIntercellSpacing(new Dimension(0, 0));
        historyTable.setBackground(new Color(245, 245, 245));

        JTableHeader header = historyTable.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 18));
        header.setBackground(new Color(30, 144, 255));
        header.setForeground(Color.WHITE);

        JScrollPane tableScrollPane = new JScrollPane(historyTable);
        tableScrollPane.setBorder(BorderFactory.createEmptyBorder());

        applyFilterButton.addActionListener(_ -> {
            String searchText = searchField.getText().toLowerCase();
            String filterType = filterTypeCombo.getSelectedItem().toString();
            String senderReceiverText = senderReceiverField.getText().toLowerCase();

            filterTable(model, transactions, searchText, filterType, senderReceiverText);
        });

        historyPanel.add(headerPanel, BorderLayout.NORTH);
        historyPanel.add(filterPanel, BorderLayout.CENTER);
        historyPanel.add(tableScrollPane, BorderLayout.SOUTH);

        return historyPanel;
    }

    /**
     * Filters the transaction history table based on the given search criteria.
     *
     * @param model              The DefaultTableModel of the transaction history
     *                           table.
     * @param transactions       The list of transaction history objects.
     * @param searchText         The text to search for in the transaction content.
     * @param filterType         The type of transaction to filter by.
     * @param fromDate           The start date for filtering transactions.
     * @param toDate             The end date for filtering transactions.
     * @param senderReceiverText The text to search for in the sender's or
     *                           receiver's full name.
     */
    private void filterTable(DefaultTableModel model, List<DatabaseHelper.TransactionHistory> transactions,
            String searchText, String filterType, String senderReceiverText) {
        model.setRowCount(0);
        for (DatabaseHelper.TransactionHistory transaction : transactions) {
            boolean matchesSearch = transaction.getTransactionContent().toLowerCase().contains(searchText);
            boolean matchesType = filterType.equals("All")
                    || transaction.getTransactionType().equalsIgnoreCase(filterType);

            boolean matchesSenderReceiver = transaction.getSenderFullName().toLowerCase().contains(senderReceiverText)
                    || transaction.getReceiverFullName().toLowerCase().contains(senderReceiverText);

            if (matchesSearch && matchesType && matchesSenderReceiver) {
                Object[] row = {
                        transaction.getDate(),
                        transaction.getTransactionType(),
                        transaction.getAmount(),
                        transaction.getBalanceAfter(),
                        transaction.getSenderFullName(),
                        transaction.getReceiverFullName(),
                        transaction.getTransactionContent()
                };
                model.addRow(row);
            }
        }
    }

    /**
     * Creates and returns a JPanel containing the user interface for changing the
     * password or username.
     *
     * @return JPanel containing the user interface for changing the password or
     *         username.
     *         The returned JPanel is used to display the input fields for the new
     *         user name and password,
     *         as well as the button to change the password.
     */
    private JPanel ChangePasswordOrUserNamePanel() {
        JPanel changePasswordPanel = new JPanel();
        changePasswordPanel.setLayout(new BoxLayout(changePasswordPanel, BoxLayout.Y_AXIS));
        changePasswordPanel.setBackground(new Color(240, 248, 255));

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(4, 2, 15, 15));
        inputPanel.setBackground(new Color(245, 245, 245));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel newUserName = new JLabel("New UserName:");
        newUserNameField = new JTextField();

        JLabel oldPasswordLabel = new JLabel("Old Password:");
        oldPasswordField = new JPasswordField();

        JLabel newPasswordLabel = new JLabel("New Password:");
        newPasswordField = new JPasswordField();

        JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
        confirmPasswordField = new JPasswordField();

        inputPanel.add(newUserName);
        inputPanel.add(newUserNameField);
        inputPanel.add(oldPasswordLabel);
        inputPanel.add(oldPasswordField);
        inputPanel.add(newPasswordLabel);
        inputPanel.add(newPasswordField);
        inputPanel.add(confirmPasswordLabel);
        inputPanel.add(confirmPasswordField);

        RoundedButton changePasswordButton = new RoundedButton("Change Password", 215, 69, 45);
        changePasswordButton.setFont(new Font("Arial", Font.BOLD, 16));
        changePasswordButton.setForeground(Color.WHITE);
        changePasswordButton.setFocusPainted(false);
        changePasswordButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        changePasswordButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        changePasswordButton.setActionCommand("changePassword");
        changePasswordButton.addActionListener(new ButtonHandler(this));

        changePasswordPanel.add(Box.createVerticalGlue());
        changePasswordPanel.add(inputPanel);
        changePasswordPanel.add(Box.createVerticalStrut(20));
        changePasswordPanel.add(changePasswordButton);
        changePasswordPanel.add(Box.createVerticalGlue());

        return changePasswordPanel;
    }

    /**
     * Resizes the image displayed in the given panel to fit the dimensions of the
     * frame.
     *
     * @param frame The frame containing the panel.
     * @param panel The panel containing the image to be resized.
     *
     *              The method adds a component listener to the frame that
     *              revalidates and
     *              repaints the panel whenever the frame is resized. This ensures
     *              that the
     *              image is always displayed at the correct size.
     */
    @Override
    public void resizeIMG(JFrame frame, JPanel panel) {
        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                panel.revalidate();
                panel.repaint();
            }
        });
    }

    /**
     * Closes the User GUI window and releases any system resources associated with
     * it.
     * This method should be called when the user is done interacting with the GUI.
     * After calling this method, the User GUI window will no longer be visible and
     * any resources
     * it was using will be released.
     */
    public void closeUserGUI() {
        this.dispose();
    }

    /**
     * Returns the JTextField for entering the new user name.
     *
     * @return JTextField containing the new user name.
     *         The returned JTextField is used to get the new user name input
     *         from the user.
     */
    public JTextField getNewUserNameField() {
        return newUserNameField;
    }

    /**
     * Returns the JPasswordField for entering the old password.
     *
     * @return JPasswordField containing the old password.
     *         The returned JPasswordField is used to get the old password input
     *         from the user.
     */
    public JPasswordField getOldPasswordField() {
        return oldPasswordField;
    }

    /**
     * Returns the JPasswordField for entering the new password.
     *
     * @return JPasswordField containing the new password.
     *         The returned JPasswordField is used to get the new password input
     *         from the user.
     */
    public JPasswordField getNewPasswordField() {
        return newPasswordField;
    }

    /**
     * Returns the JPasswordField for confirming the new password.
     *
     * @return JPasswordField containing the confirm password.
     *         The returned JPasswordField is used to get the confirm password input
     *         from the user.
     */
    public JPasswordField getConfirmPasswordField() {
        return confirmPasswordField;
    }
}