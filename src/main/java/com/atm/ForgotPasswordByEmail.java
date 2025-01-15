package com.atm;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

@SuppressWarnings("all")
public class ForgotPasswordByEmail {
    private final JTextField emailField;
    private final JFrame emailFrame;

    public ForgotPasswordByEmail(ForgotPasswordByUserName parentFrame) {
        if (parentFrame != null) {
            parentFrame.dispose();
        }

        emailFrame = new JFrame("Recover by Email");
        emailFrame.setSize(400, 300);
        emailFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        emailFrame.setLocationRelativeTo(null);
        emailFrame.setResizable(false);
        emailFrame.setVisible(true);

        JLabel emailLabel = new JLabel("Enter your email:");
        emailLabel.setBounds(50, 50, 300, 30);

        emailField = new JTextField();
        emailField.setBounds(50, 100, 300, 30);

        RoundedButton recoverByEmailButton = new RoundedButton("Recover Account", 55, 72, 189);
        recoverByEmailButton.setActionCommand("Recover By Email");
        recoverByEmailButton.addActionListener(new ForgotPassword(this));
        recoverByEmailButton.setBounds(50, 150, 300, 30);
        emailFrame.getRootPane().setDefaultButton(recoverByEmailButton);

        emailFrame.setLayout(null);
        emailFrame.add(emailLabel);
        emailFrame.add(emailField);
        emailFrame.add(recoverByEmailButton);

        emailFrame.setVisible(true);
    }

    /**
     * Retrieves the email entered by the user in the Forgot Password by Email
     * frame.
     *
     * @return A string representing the email entered by the user.
     *
     * @see JTextField#getText()
     */
    public String getEmail() {
        return emailField.getText();
    }

    /**
     * Closes the Forgot Password by Email frame.
     * If the emailFrame is not null, it disposes of the frame, effectively closing
     * it.
     * This method is typically called when the user has completed the email
     * recovery process
     * or when they decide to cancel the process.
     *
     * @return void
     */
    public void closeForgotPasswordByEmailFrame() {
        if (emailFrame != null) {
            emailFrame.dispose();
        }
    }

}
