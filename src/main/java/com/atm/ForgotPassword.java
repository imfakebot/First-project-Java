package com.atm;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import org.mindrot.jbcrypt.BCrypt;

public class ForgotPassword extends MouseAdapter implements ActionListener {
    private ForgotPasswordByUserName forgotPasswordByUserName;
    private ForgotPasswordByEmail forgotPasswordByEmail;
    private SqlServerConnection sqlConnection;
    @SuppressWarnings("unused")
    private LoginFrame loginFrame;

    public ForgotPassword(LoginFrame loginFrame) {
        this.loginFrame = loginFrame;

    }

    public ForgotPassword(ForgotPasswordByEmail forgotPasswordByEmail) {
        this.forgotPasswordByEmail = forgotPasswordByEmail;
    }

    public ForgotPassword(ForgotPasswordByUserName forgotPasswordByUserName) {
        this.forgotPasswordByUserName = forgotPasswordByUserName;
    }

    /**
     * Handles the recovery process for a user account using their username.
     *
     * @throws SQLException If an error occurs while interacting with the database.
     */
    private void handleRecoveryByUsername() throws SQLException {
        // Check if the ForgotPasswordByUserName instance is initialized
        if (forgotPasswordByUserName == null) {
            JOptionPane.showMessageDialog(null, "ForgotPasswordFrame is not initialized!");
            return;
        }

        // Retrieve the username from the ForgotPasswordByUserName instance
        String username = forgotPasswordByUserName.getUsername();

        // Validate the username
        if (username.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Username cannot be empty!");
            return;
        }

        // Call the recoverAccount method to initiate the recovery process
        recoverAccount(username, "Username");
    }

    /**
     * Handles the recovery process for a user account using their email.
     *
     * @throws SQLException If an error occurs while interacting with the database.
     */
    private void handleRecoveryByEmail() throws SQLException {
        // Check if the ForgotPasswordByEmail instance is initialized
        if (forgotPasswordByEmail == null) {
            JOptionPane.showMessageDialog(null, "ForgotPasswordByEmail is not initialized!");
            return;
        }

        // Retrieve the email from the ForgotPasswordByEmail instance
        String email = forgotPasswordByEmail.getEmail();

        // Validate the email
        if (email.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Email cannot be empty!");
            return;
        }

        // Call the recoverAccount method to initiate the recovery process
        recoverAccount(email, "Email");
    }

    /**
     * Initiates the recovery process for a user account using either their username
     * or email.
     *
     * @param identifier The username or email of the user account to recover.
     * @param type       The type of identifier provided (either "Username" or
     *                   "Email").
     * @throws SQLException If an error occurs while interacting with the database.
     */
    private void recoverAccount(String identifier, String type) throws SQLException {
        String procedure = switch (type) {
            case "Username" -> "{CALL RecoverAccountByUsername(?)}";
            case "Email" -> "{CALL RecoverAccountByEmail(?)}";
            default -> throw new IllegalArgumentException("Invalid recovery type");
        };

        sqlConnection = new SqlServerConnection();
        try (Connection conn = sqlConnection.getConnection();
                CallableStatement cstmt = conn.prepareCall(procedure)) {
            cstmt.setString(1, identifier);
            ResultSet rs = cstmt.executeQuery();

            if (rs.next()) {

                String username = rs.getString(1);
                String securityQuestion = rs.getString(2);
                String securityAnswer = rs.getString(3);

                if (securityQuestion != null && securityAnswer != null) {
                    String answer = JOptionPane.showInputDialog(null, securityQuestion, "Security Question",
                            JOptionPane.QUESTION_MESSAGE);
                    if (answer != null && answer.equalsIgnoreCase(securityAnswer)) {
                        updatePassword(username);
                    } else {
                        JOptionPane.showMessageDialog(null, "Incorrect security answer.");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Security question or answer not found.");
                }
            } else {
                JOptionPane.showMessageDialog(null, type + " not found!");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Database error: " + ex.getMessage());
        }
    }

    /**
     * Updates the password for a user account using their username.
     *
     * @param username The username of the user account to update the password for.
     * @throws SQLException If an error occurs while interacting with the database.
     */
    private void updatePassword(String username) throws SQLException {
        String message = "Username: " + username + "\nEnter your new password:";
        String newPassword = JOptionPane.showInputDialog(null, message, "New Password", JOptionPane.PLAIN_MESSAGE);

        if (newPassword == null || newPassword.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Password cannot be empty!");
            return;
        }

        String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());

        try {
            sqlConnection = new SqlServerConnection();
            try (Connection connection = sqlConnection.getConnection();
                    CallableStatement callableStatement = connection.prepareCall("{CALL UpdatePassword(?, ?, ?)}")) {

                callableStatement.setString(1, username);
                callableStatement.setNull(2, Types.NULL);
                callableStatement.setString(3, hashedPassword);

                int rowsAffected = callableStatement.executeUpdate();

                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(null, "Password updated successfully!");
                    closeCurrentFrame();
                } else {
                    JOptionPane.showMessageDialog(null, "Username not found. Password update failed!");
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
        }
    }

    /**
     * Closes the currently active frame for password recovery.
     * If the {@link ForgotPasswordByUserName} instance is not null, it calls the
     * {@link ForgotPasswordByUserName#closeForgotPasswordByUserNameFrame()} method.
     * If the {@link ForgotPasswordByEmail} instance is not null, it calls the
     * {@link ForgotPasswordByEmail#closeForgotPasswordByEmailFrame()} method.
     * If both instances are null, it displays a message indicating that no active
     * frame to close.
     */
    private void closeCurrentFrame() {
        if (forgotPasswordByUserName != null) {
            forgotPasswordByUserName.closeForgotPasswordByUserNameFrame();
        } else if (forgotPasswordByEmail != null) {
            forgotPasswordByEmail.closeForgotPasswordByEmailFrame();
        } else {
            JOptionPane.showMessageDialog(null, "No active frame to close.");
        }
    }

    /**
     * Handles the mouse click event on a JLabel component.
     * If the clicked JLabel contains specific text, it triggers the creation of a
     * new frame for password recovery.
     *
     * @param e The mouse event that triggered this method.
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        // Check if the source of the event is a JLabel
        if (e.getSource() instanceof JLabel sourceLabel) {
            String text = sourceLabel.getText().trim();

            // If the JLabel text matches the specified conditions, create a new frame for
            // password recovery
            if ("Forgot username or password? Click here".equals(text.trim())) {
                createForgotPasswordFrame();
            } else if ("Don't remember username? Click here.".equals(text)) {
                createForgotEmailFrame();
            }
        }
    }

    /**
     * Handles the action events triggered by buttons or menu items.
     *
     * @param e The action event that triggered this method.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        // Switch statement to handle different actions based on the command
        switch (command) {
            case "Recover By Username" -> {
                // Calls the method to initiate the recovery process by username
                recoverByUsername();
            }
            case "Recover By Email" -> {
                // Calls the method to initiate the recovery process by email
                recoverByEmail();
            }
            default -> {
                // Displays a message for unknown actions
                JOptionPane.showMessageDialog(null, "Unknown action: " + command);
            }
        }
    }

    /**
     * Creates a new instance of the {@link ForgotPasswordByUserName} frame for
     * password recovery.
     * This frame allows users to enter their username to initiate the password
     * recovery process.
     *
     * @return {@code void} - This method does not return any value.
     *
     * @throws None - This method does not throw any exceptions.
     */
    private void createForgotPasswordFrame() {
        forgotPasswordByUserName = new ForgotPasswordByUserName();
    }

    /**
     * Creates a new instance of the {@link ForgotPasswordByEmail} frame for
     * password recovery.
     * This frame allows users to enter their email to initiate the password
     * recovery process.
     *
     * @return {@code void} - This method does not return any value.
     *
     * @throws None - This method does not throw any exceptions.
     */
    private void createForgotEmailFrame() {
        forgotPasswordByEmail = new ForgotPasswordByEmail(forgotPasswordByUserName);
    }

    /**
     * Initiates the password recovery process for a user account using their
     * username.
     * If the {@link ForgotPasswordByUserName} instance is not initialized, it
     * displays a message
     * indicating that the ForgotPasswordFrame is not initialized.
     * If the username is empty, it displays a message indicating that the username
     * cannot be empty.
     * Otherwise, it calls the {@link #recoverAccount(String, String)} method to
     * initiate the recovery process.
     *
     * @throws SQLException If an error occurs while interacting with the database.
     */
    public void recoverByUsername() {
        try {
            handleRecoveryByUsername();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }

    /**
     * Initiates the password recovery process for a user account using their email.
     * If the {@link ForgotPasswordByEmail} instance is not initialized, it displays
     * a message
     * indicating that the ForgotPasswordByEmail is not initialized.
     * If the email is empty, it displays a message indicating that the email cannot
     * be empty.
     * Otherwise, it calls the {@link #recoverAccount(String, String)} method to
     * initiate the recovery process.
     *
     * @throws SQLException If an error occurs while interacting with the database.
     */
    public void recoverByEmail() {
        try {
            handleRecoveryByEmail();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }

}
