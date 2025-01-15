package com.atm;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class Deposit extends JFrame {
    private double currentBalance;
    private double depositAmount;
    private final JButton depositButton;
    private final JLabel amountLabel;
    private final JLabel balanceLabel;

    public Deposit(double balance) {
        super("Deposit Money");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setIconImage(new ImageIcon("src\\main\\java\\com\\atm\\icon-removebg-preview.png").getImage());

        this.currentBalance = balance;
        this.depositAmount = 0;

        JLabel headerLabel = new JLabel("Deposit Money", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 18));
        headerLabel.setOpaque(true);
        headerLabel.setBackground(new Color(70, 130, 180));
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setPreferredSize(new Dimension(0, 50));

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(2, 1, 10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        amountLabel = new JLabel("Amount to Deposit: $");
        amountLabel.setFont(new Font("Arial", Font.BOLD, 16));

        balanceLabel = new JLabel("Current Balance: " + currentBalance);
        balanceLabel.setFont(new Font("Arial", Font.BOLD, 16));
        balanceLabel.setForeground(new Color(70, 130, 180));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 2, 10, 0));

        JButton checkButton = new JButton("Check");
        checkButton.setBackground(new Color(255, 165, 0));
        checkButton.setForeground(Color.WHITE);
        checkButton.setFont(new Font("Arial", Font.BOLD, 14));
        checkButton.setFocusPainted(false);
        checkButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        checkButton.setActionCommand("Check");
        checkButton.addActionListener(new ButtonHandler(this));

        depositButton = new JButton("Deposit");
        depositButton.setBackground(new Color(102, 255, 102));
        depositButton.setForeground(Color.WHITE);
        depositButton.setFont(new Font("Arial", Font.BOLD, 14));
        depositButton.setFocusPainted(false);
        depositButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        depositButton.setEnabled(false);
        depositButton.setActionCommand("Deposit");
        depositButton.addActionListener(new ButtonHandler(this));

        buttonPanel.add(checkButton);
        buttonPanel.add(depositButton);

        mainPanel.add(amountLabel);
        mainPanel.add(balanceLabel);

        add(headerLabel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    /**
     * Sets the deposit amount entered by the user and updates the UI accordingly.
     *
     * @param amount The deposit amount entered by the user. This value should be a
     *               positive number.
     *
     * @return void
     *
     * @throws IllegalArgumentException If the provided amount is negative.
     *
     * @since 1.0
     */
    public void setDepositAmount(double amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Deposit amount cannot be negative.");
        }
        this.depositAmount = amount;
        this.amountLabel.setText("Amount to Deposit: $" + String.format("%.2f", amount));
        this.depositButton.setEnabled(true);
    }

    /**
     * Retrieves the deposit amount entered by the user.
     *
     * @return The deposit amount entered by the user.
     *
     * @since 1.0
     */
    public double getDepositAmount() {
        return this.depositAmount;
    }

    /**
     * Updates the current balance of the account and updates the balance label.
     *
     * @param newBalance The new balance to be set for the account.
     * @return void
     *
     * @throws NoException This method does not throw any exceptions.
     *
     * @since 1.0
     */
    public void updateBalance(double newBalance) {
        this.currentBalance = newBalance;
        balanceLabel.setText("Current Balance: " + String.valueOf(newBalance));
    }

}
