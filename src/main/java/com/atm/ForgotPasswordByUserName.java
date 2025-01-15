package com.atm;

import java.awt.Color;
import java.awt.Cursor;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class ForgotPasswordByUserName extends JFrame {
    private final JTextField usernameField;

    public ForgotPasswordByUserName() {
        super("Recover Account");
        setSize(450, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null);
        setResizable(false);
        setVisible(true);

        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setBounds(50, 50, 100, 30);

        usernameField = new JTextField();
        usernameField.setBounds(150, 50, 200, 30);

        RoundedButton recoverButton = new RoundedButton("Recover Account", 55, 72, 189);
        recoverButton.setBounds(150, 100, 200, 30);
        recoverButton.setActionCommand("Recover By Username");
        recoverButton.addActionListener(new ForgotPassword(this));
        getRootPane().setDefaultButton(recoverButton);

        JLabel recoverByEmailLabel = new JLabel("Don't remember username? Click here.");
        recoverByEmailLabel.setBounds(130, 150, 300, 30);
        recoverByEmailLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        recoverByEmailLabel.setForeground(new Color(232, 19, 118));
        recoverByEmailLabel.addMouseListener(new ForgotPassword(this));

        add(usernameLabel);
        add(usernameField);
        add(recoverButton);
        add(recoverByEmailLabel);
    }

    /**
     * Retrieves the username entered by the user in the Forgot Password by Username
     * window.
     *
     * @return A string representing the username entered by the user.
     *
     * @see JTextField#getText()
     */
    public String getUsername() {
        return usernameField.getText();
    }

    /**
     * Closes the Forgot Password by Username window.
     *
     * This method disposes of the JFrame, effectively closing the window and
     * releasing
     * any system resources associated with it.
     *
     * @see javax.swing.JFrame#dispose()
     */
    public void closeForgotPasswordByUserNameFrame() {
        this.dispose();
    }

}
