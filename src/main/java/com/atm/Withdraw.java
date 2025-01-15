package com.atm;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class Withdraw extends JFrame {
    private JLabel balanceLabel;
    private JTextField amountField;

    public Withdraw(double initialBalance) {
        super("Withdraw Money");
        initializeFrame();
        initializeComponents(initialBalance);
        setVisible(true);
    }

    /**
     * Initializes the frame for the Withdraw window.
     *
     * This method sets the size, default close operation, location, and layout
     * of the Withdraw window. The frame is configured to be disposed when closed,
     * centered on the screen, and uses a BorderLayout layout.
     *
     * @return void
     *
     * @since 1.0
     */
    private void initializeFrame() {
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
    }

    /**
     * Initializes the components of the Withdraw window.
     *
     * This method creates and adds the header label, main panel, and keypad panel
     * to the Withdraw window. The header label displays the title "Withdraw Money",
     * the main panel contains the current balance label and amount field, and the
     * keypad panel includes number buttons and a withdraw button.
     *
     * @param initialBalance The initial balance to be displayed in the balance
     *                       label. This value is used to set the initial balance
     *                       in the main panel.
     *
     * @return void
     *
     * @since 1.0
     */
    private void initializeComponents(double initialBalance) {
        JLabel headerLabel = createHeaderLabel("Withdraw Money");
        add(headerLabel, BorderLayout.NORTH);

        JPanel mainPanel = createMainPanel(initialBalance);
        add(mainPanel, BorderLayout.CENTER);

        JPanel keypadPanel = createKeypadPanel();
        add(keypadPanel, BorderLayout.SOUTH);
    }

    /**
     * Creates a JLabel with the specified text and customizes its appearance for
     * the header of the ATM withdrawal process.
     *
     * @param text The text to be displayed on the header label.
     *
     * @return A JLabel with the specified text and customized appearance for
     *         the header.
     *
     * @since 1.0
     */
    private JLabel createHeaderLabel(String text) {
        JLabel headerLabel = new JLabel(text, SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 18));
        headerLabel.setOpaque(true);
        headerLabel.setBackground(new Color(70, 130, 180));
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setPreferredSize(new Dimension(0, 50));
        return headerLabel;
    }

    /**
     * Creates a JPanel containing the balance label and amount field for the ATM
     * withdrawal process.
     *
     * @param initialBalance The initial balance to be displayed in the balance
     *                       label.
     *
     * @return A JPanel containing the balance label and amount field.
     *
     * @since 1.0
     */
    private JPanel createMainPanel(double initialBalance) {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(2, 1, 10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        balanceLabel = new JLabel("Current Balance: $" + initialBalance, SwingConstants.CENTER);
        balanceLabel.setFont(new Font("Arial", Font.BOLD, 16));
        balanceLabel.setForeground(new Color(70, 130, 180));

        amountField = new JTextField();
        amountField.setFont(new Font("Arial", Font.PLAIN, 14));
        amountField.setHorizontalAlignment(JTextField.CENTER);
        amountField.setBorder(BorderFactory.createTitledBorder("Enter Amount"));

        mainPanel.add(balanceLabel);
        mainPanel.add(amountField);

        return mainPanel;
    }

    /**
     * Creates a JPanel containing the keypad buttons for the ATM withdrawal
     * process.
     * The keypad includes number buttons from 1 to 9, a zero button, and a withdraw
     * button.
     *
     * @return A JPanel containing the keypad buttons.
     *
     * @since 1.0
     */
    private JPanel createKeypadPanel() {
        JPanel keypadPanel = new JPanel();
        keypadPanel.setLayout(new GridLayout(4, 3, 5, 5));
        keypadPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        for (int i = 1; i <= 9; i++) {
            JButton button = createNumberButton(String.valueOf(i));
            keypadPanel.add(button);
        }

        keypadPanel.add(new JPanel()); // Empty panel to maintain grid layout
        JButton zeroButton = createNumberButton("0");
        keypadPanel.add(zeroButton);

        JButton withdrawButton = createActionButton("Withdraw");
        withdrawButton.setActionCommand("Withdraw");
        withdrawButton.addActionListener(new ButtonHandler(this));

        keypadPanel.add(withdrawButton);

        return keypadPanel;
    }

    /**
     * Creates a JButton with the specified text and customizes its appearance for
     * number buttons.
     *
     * This method creates a JButton with the given text and applies custom styling
     * such as font, focus painted, cursor, and an action listener to append the
     * button's text
     * to the amount field when clicked.
     *
     * @param text The text to be displayed on the button.
     *
     * @return A JButton with the specified text and customized appearance for
     *         number buttons.
     *
     * @since 1.0
     */
    private JButton createNumberButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addActionListener(_ -> amountField.setText(amountField.getText() + text));
        return button;
    }

    /**
     * Creates a JButton with the specified text and customizes its appearance.
     *
     * This method creates a JButton with the given text and applies custom styling
     * such as background color, foreground color, font, focus painted, and cursor.
     *
     * @param text The text to be displayed on the button.
     *
     * @return A JButton with the specified text and customized appearance.
     *
     * @since 1.0
     */
    private JButton createActionButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(102, 255, 102));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    /**
     * Retrieves the text currently entered in the amount field.
     *
     * This method retrieves the text from the amount field and returns it as a
     * string.
     * The returned value can be used for further processing or validation.
     *
     * @return A string representing the text currently entered in the amount field.
     *
     * @since 1.0
     */
    public String getAmountTextField() {
        return amountField.getText();
    }

    /**
     * Clears the text in the amount field.
     *
     * This method sets the text in the amount field to null, effectively clearing
     * any inputted value.
     *
     * @return void
     *
     * @since 1.0
     */
    public void clearAmountTextField() {
        amountField.setText(null);
    }

    /**
     * Updates the current balance label with the new balance value.
     *
     * @param newBalance The new balance value to be displayed.
     *
     * @return void
     *
     * @since 1.0
     */
    public void updateBalance(double newBalance) {
        balanceLabel.setText("Current Balance: $" + newBalance);
    }
}
