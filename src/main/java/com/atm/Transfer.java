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
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class Transfer extends JFrame {
    private JLabel balanceLabel;
    private JTextField amountField;
    private JTextField accountField;
    private JTextField contentField;

    public Transfer(double initialBalance) {
        super("Transfer Money");
        setIconImage(new ImageIcon(getClass().getResource(
                "/com/atm/icon-removebg-preview.png")).getImage());
        initializeFrame();
        initializeComponents(initialBalance);

    }

    private void initializeFrame() {
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
    }

    private void initializeComponents(double initialBalance) {
        JLabel headerLabel = createHeaderLabel("Transfer Money");
        add(headerLabel, BorderLayout.NORTH);

        JPanel mainPanel = createMainPanel(initialBalance);
        add(mainPanel, BorderLayout.CENTER);

        JPanel buttonPanel = createButtonPanel();
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private JLabel createHeaderLabel(String text) {
        JLabel headerLabel = new JLabel(text, SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 18));
        headerLabel.setOpaque(true);
        headerLabel.setBackground(new Color(70, 130, 180));
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setPreferredSize(new Dimension(0, 50));
        return headerLabel;
    }

    private JPanel createMainPanel(double initialBalance) {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(4, 1, 10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        balanceLabel = new JLabel("Current Balance: " + String.valueOf(initialBalance), SwingConstants.CENTER);
        balanceLabel.setFont(new Font("Arial", Font.BOLD, 14));
        balanceLabel.setForeground(new Color(70, 130, 180));

        amountField = new JTextField();
        amountField.setFont(new Font("Arial", Font.PLAIN, 14));
        amountField.setBorder(BorderFactory.createTitledBorder("Enter Amount"));

        accountField = new JTextField();
        accountField.setFont(new Font("Arial", Font.PLAIN, 14));
        accountField.setBorder(BorderFactory.createTitledBorder("Enter Account Number"));

        contentField = new JTextField();
        contentField.setFont(new Font("Arial", Font.PLAIN, 14));
        contentField.setBorder(BorderFactory.createTitledBorder("Enter Transfer Content"));

        mainPanel.add(balanceLabel);
        mainPanel.add(amountField);
        mainPanel.add(accountField);
        mainPanel.add(contentField);

        return mainPanel;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel();

        JButton transferButton = new JButton("Transfer");
        transferButton.setFont(new Font("Arial", Font.BOLD, 14));
        transferButton.setBackground(new Color(102, 255, 102));
        transferButton.setForeground(Color.WHITE);
        transferButton.setFocusPainted(false);
        transferButton.setPreferredSize(new Dimension(200, 40));
        transferButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        transferButton.setActionCommand("Transfer");
        transferButton.addActionListener(new ButtonHandler(this));

        buttonPanel.add(transferButton);

        return buttonPanel;
    }

    public String getAmountField() {
        return amountField.getText();
    }

    public String getAccountNumberField() {
        return accountField.getText();
    }

    public String getContentTransactionField() {
        return contentField.getText();
    }
}
