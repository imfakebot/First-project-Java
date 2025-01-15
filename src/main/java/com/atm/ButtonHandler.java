package com.atm;

import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.util.Random;
import java.util.prefs.Preferences;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import org.mindrot.jbcrypt.BCrypt;

public class ButtonHandler extends MouseAdapter implements ActionListener {

    private SqlServerConnection sqlConnection;
    private LoginFrame loginFrame;
    private QRCodeDisplay qrCodeDisplay;
    private UserGUI usergui;
    private Withdraw withDraw;
    private Deposit deposit;
    private Transfer transfer;

    public ButtonHandler(LoginFrame frame) {
        this.loginFrame = frame;
    }

    public ButtonHandler(UserGUI usergui) {
        this.usergui = usergui;
    }

    public ButtonHandler(QRCodeDisplay qrCodeDisplay) {
        this.qrCodeDisplay = qrCodeDisplay;
    }

    public ButtonHandler(Withdraw withDraw) {
        this.withDraw = withDraw;
    }

    public ButtonHandler(Deposit deposit) {
        this.deposit = deposit;
    }

    public ButtonHandler(Transfer transfer) {
        this.transfer = transfer;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        switch (command) {
            case "QRcodesButton" -> {
                try {
                    showQRcodes();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage());
                }
            }
            case "withdrawButton" -> {
                try {
                    handleWithdrawButton();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Database error: " + ex.getMessage());
                }
            }
            case "depositButton" -> handleDepositButton();
            case "Withdraw" -> handleWithdraw();
            case "Check" -> handleCheck();
            case "Deposit" -> {
                try {
                    handleDeposit();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage());
                }
            }
            case "transferButton" -> {
                double CurrentBalance = getCurrentBalance();
                transfer = new Transfer(CurrentBalance);
                transfer.setVisible(true);
            }
            case "Transfer" -> {
                try {
                    handleTransfer();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid amount. Please enter a valid number.");
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage());
                }
            }

            case "Login" -> {
                try {
                    handleLogin();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid number format: " + ex.getMessage());
                }
            }
            case "Logout" -> handleLogout();
            case "Signup" -> handleSignup();

            case "changePassword" -> {
                try {
                    handleChangePassword();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage());
                }
            }
        }
    }

    /**
     * Handles the change password process.
     * 
     * This method collects the old password, new password, and confirm password
     * from the user, validates the input, hashes the new password, and calls a
     * stored procedure to update the password in the database.
     * 
     * @throws SQLException if a database access error occurs.
     */
    private void handleChangePassword() throws SQLException {
        String oldPassword = new String(usergui.getOldPasswordField().getPassword());
        String newPassword = new String(usergui.getNewPasswordField().getPassword());
        String confirmPassword = new String(usergui.getConfirmPasswordField().getPassword());

        // Validate input
        if (!newPassword.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(usergui, "Passwords do not match.");
            return;
        }

        if (oldPassword.isEmpty()) {
            JOptionPane.showMessageDialog(usergui, "Old password cannot be empty.");
            return;
        }

        if (newPassword.isEmpty()) {
            JOptionPane.showMessageDialog(usergui, "New password cannot be empty.");
            return;
        }

        if (confirmPassword.isEmpty()) {
            JOptionPane.showMessageDialog(usergui, "Confirm password cannot be empty.");
            return;
        }

        // Hash the new password
        String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());
        String CustomerID = loadCustomerID();
        String userName = loadUserName();

        // Database interaction
        sqlConnection = new SqlServerConnection();
        try (Connection conn = sqlConnection.getConnection();
                CallableStatement stmt = conn.prepareCall("{CALL UpdatePassword(?,?,?)}")) {
            stmt.setString(1, userName);
            stmt.setString(2, CustomerID);
            stmt.setString(3, hashedPassword);

            boolean isSuccess = stmt.executeUpdate() > 0;

            if (isSuccess) {
                JOptionPane.showMessageDialog(null, "Update Password successfully.");
            } else {
                JOptionPane.showMessageDialog(null, "Update Password failed. Please try again.");
            }
        }

    }

    /**
     * Handles the transfer process.
     * 
     * This method collects the transfer amount, recipient account number, and
     * transaction content from the user, validates the input, checks for sufficient
     * balance, and calls a stored procedure to perform the transfer.
     * 
     * @throws NumberFormatException if the transfer amount is not a valid number.
     * @throws SQLException          if a database access error occurs.
     */
    private void handleTransfer() throws NumberFormatException, SQLException {
        double amountToTransfer = Double.parseDouble(transfer.getAmountField());
        String recipientAccount = transfer.getAccountNumberField();
        String transactionContent = transfer.getContentTransactionField();

        // Validate input
        if (recipientAccount.length() != 10 || !recipientAccount.matches("\\d+")) {
            JOptionPane.showMessageDialog(null, "Recipient account number must be a 10-digit number.");
            return;
        }

        if (amountToTransfer > 1000000) {
            JOptionPane.showMessageDialog(null, "Transfer amount exceeds the maximum limit.");
            return;
        }

        if (amountToTransfer <= 0) {
            JOptionPane.showMessageDialog(null, "The transfer amount must be greater than 0.");
            return;
        }

        if (recipientAccount.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Recipient account number cannot be empty.");
            return;
        }

        String accountNumber = loadAccountNumbber();
        if (accountNumber.equals(recipientAccount)) {
            JOptionPane.showMessageDialog(null, "You can't make a transfer to yourself.");
            return;
        }

        double currentBalance = getCurrentBalance();
        if (amountToTransfer > currentBalance) {
            JOptionPane.showMessageDialog(null, "Insufficient balance to complete the transaction.");
            return;
        }

        int confirmation = JOptionPane.showConfirmDialog(null,
                "Are you sure you want to transfer " + amountToTransfer + " to account " + recipientAccount + "?",
                "Confirm Transfer", JOptionPane.YES_NO_OPTION);

        if (confirmation != JOptionPane.YES_OPTION) {
            return;
        }

        String CustomerID = loadCustomerID();
        sqlConnection = new SqlServerConnection();
        try (Connection conn = sqlConnection.getConnection();
                CallableStatement stmt = conn.prepareCall("{CALL TransferMoney(?, ?, ?, ?)}")) {

            stmt.setString(1, CustomerID);
            stmt.setString(2, recipientAccount);
            stmt.setDouble(3, amountToTransfer);
            stmt.setString(4, transactionContent);

            boolean isTransferred = stmt.executeUpdate() > 0;

            if (isTransferred) {
                JOptionPane.showMessageDialog(null, "Transfer completed successfully.");
            } else {
                JOptionPane.showMessageDialog(null, "Transfer failed. Please try again.");
            }
        }
    }

    private void handleLogin() throws SQLException, NumberFormatException {
        String username = loginFrame.getUserNameFieldInLogin().getText();
        String password = loginFrame.getPaswordFieldInLogin().getText();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Username and password cannot be empty!");
            return;
        }

        sqlConnection = new SqlServerConnection();
        try (CallableStatement stmt = sqlConnection.getConnection().prepareCall("{CALL sp_LoginAndFetchDetails(?)}")) {
            stmt.setString(1, username);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String storedPasswordHash = rs.getString("PasswordHash");
                boolean isAccountActive = rs.getBoolean("AccountStatus");

                if (!isAccountActive) {
                    JOptionPane.showMessageDialog(null,
                            "Your account is deactivated. \nDon't hesitate to get in touch with support through email:Anh1412sun@gmail.com");
                    return;
                }

                if (BCrypt.checkpw(password, storedPasswordHash)) {
                    JOptionPane.showMessageDialog(null, "Login successful!");

                    String fullName = rs.getString("FullName");
                    String accountNumber = rs.getString("AccountNumber");
                    double currentBalance = rs.getDouble("CurrentBalance");
                    String customerID = rs.getString("CustomerID");

                    saveCustomerID(customerID);
                    saveUserName(username);

                    loginFrame.closeLoginFrame();
                    usergui = new UserGUI(fullName, accountNumber, currentBalance, customerID);
                    usergui.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid username or password!");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Invalid username or password!");
            }
        }
    }

    /**
     * Saves the account number to the user's preferences.
     * 
     * This method stores the provided account number in the user's preferences
     * using the Preferences API, allowing it to be retrieved later.
     * 
     * @param accountNumber the account number to be saved.
     */
    public void saveAccountNumber(String accountNumber) {
        Preferences prefs = Preferences.userNodeForPackage(LoginFrame.class);
        prefs.put("AccountNumber", accountNumber);
    }

    /**
     * Loads the account number from the user's preferences.
     * 
     * This method retrieves the stored account number from the user's preferences
     * using the Preferences API. If no account number is found, it returns an empty
     * string.
     * 
     * @return the stored account number, or an empty string if not found.
     */
    public String loadAccountNumbber() {
        Preferences prefs = Preferences.userNodeForPackage(LoginFrame.class);
        return prefs.get("AccountNumber", "");
    }

    /**
     * Saves the customer ID to the user's preferences.
     * 
     * This method stores the provided customer ID in the user's preferences
     * using the Preferences API, allowing it to be retrieved later.
     * 
     * @param customerID the customer ID to be saved.
     */
    private void saveCustomerID(String customerID) {
        Preferences prefs = Preferences.userNodeForPackage(LoginFrame.class);
        prefs.put("CustomerID", customerID);
    }

    /**
     * Loads the customer ID from the user's preferences.
     * 
     * This method retrieves the stored customer ID from the user's preferences
     * using the Preferences API. If no customer ID is found, it returns an empty
     * string.
     * 
     * @return the stored customer ID, or an empty string if not found.
     */
    private String loadCustomerID() {
        Preferences prefs = Preferences.userNodeForPackage(LoginFrame.class);
        return prefs.get("CustomerID", "");
    }

    /**
     * Saves the username to the user's preferences.
     * 
     * This method stores the provided username in the user's preferences
     * using the Preferences API, allowing it to be retrieved later.
     * 
     * @param userName the username to be saved.
     */
    private void saveUserName(String userName) {
        Preferences prefs = Preferences.userNodeForPackage(LoginFrame.class);
        prefs.put("userName", userName);
    }

    /**
     * Loads the username from the user's preferences.
     * 
     * This method retrieves the stored username from the user's preferences
     * using the Preferences API. If no username is found, it returns an empty
     * string.
     * 
     * @return the stored username, or an empty string if not found.
     */
    private String loadUserName() {
        Preferences prefs = Preferences.userNodeForPackage(LoginFrame.class);
        return prefs.get("userName", "");
    }

    /**
     * Handles the check process.
     * 
     * This method generates a random number between 1 and 10000, and sets it as
     * the deposit amount in the Deposit object.
     */
    private void handleCheck() {
        Random rand = new Random();
        double randomNumber = rand.nextInt(10000) + 1;
        deposit.setDepositAmount(randomNumber);
    }

    /**
     * Handles the deposit process.
     * 
     * This method retrieves the deposit amount from the Deposit object, validates
     * the amount, checks the current balance, performs the deposit, updates the
     * balance, and displays appropriate messages.
     * 
     * @throws SQLException if a database access error occurs.
     */
    private void handleDeposit() throws SQLException {
        double amountToDeposit = deposit.getDepositAmount();

        if (amountToDeposit <= 0) {
            JOptionPane.showMessageDialog(null, "Amount must be greater than zero!");
            return;
        }

        double currentBalance = getCurrentBalance();

        boolean success = performDeposit(amountToDeposit);

        if (success) {
            double newBalance = currentBalance + amountToDeposit;
            deposit.updateBalance(newBalance);
            JOptionPane.showMessageDialog(null,
                    "Deposit successful! Amount deposited: " + amountToDeposit + "\nNew balance: " + newBalance);
        } else {
            JOptionPane.showMessageDialog(null, "An error occurred while processing the deposit.");
        }
    }

    /**
     * Performs the deposit operation.
     * 
     * This method connects to the database, calls a stored procedure to update the
     * balance and log the transaction, and returns whether the operation was
     * successful.
     * 
     * @param amountToDeposit the amount to be deposited.
     * @return true if the deposit was successful, false otherwise.
     */
    private boolean performDeposit(double amountToDeposit) {
        try {
            sqlConnection = new SqlServerConnection();
            try (Connection conn = sqlConnection.getConnection();
                    CallableStatement stmt = conn.prepareCall("{CALL UpdateBalanceAndLogTransaction(?, ?, ?)}")) {

                String customerID = loadCustomerID();

                stmt.setString(1, customerID);
                stmt.setDouble(2, amountToDeposit);
                stmt.setString(3, null);

                int rowsUpdated = stmt.executeUpdate();

                if (rowsUpdated > 0) {
                    deposit.setDepositAmount(0.0);
                    return true;
                } else {
                    JOptionPane.showMessageDialog(null, "Deposit failed, no rows affected.");
                    return false;
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Database error: " + ex.getMessage());
            return false;
        }
    }

    /**
     * Handles the deposit button action.
     * 
     * This method retrieves the current balance of the user and creates a new
     * Deposit object with the balance.
     */
    private void handleDepositButton() {
        double balance = getCurrentBalance();
        deposit = new Deposit(balance);
    }

    /**
     * Displays the QR codes for the user.
     * 
     * This method retrieves the customer's full name, account number, and account
     * ID
     * from the database using a stored procedure, and displays the information in a
     * QR code display window.
     * 
     * @throws SQLException if a database access error occurs.
     */
    private void showQRcodes() throws SQLException {
        String customerID = loadCustomerID();
        sqlConnection = new SqlServerConnection();

        try (Connection conn = sqlConnection.getConnection();
                CallableStatement stmt = conn.prepareCall("{CALL sp_GetFullNameAndAccountNumber(?)}")) {
            stmt.setString(1, customerID);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String fullName = rs.getString("FullName");
                String accountNumber = rs.getString("AccountNumber");
                String accountID = rs.getString("AccountID");

                qrCodeDisplay = new QRCodeDisplay(accountID, fullName, accountNumber);
                qrCodeDisplay.setVisible(true);
            }

        }
    }

    /**
     * Scales an ImageIcon to the specified width and height.
     * 
     * @param icon   The original ImageIcon to be scaled.
     * @param width  The desired width of the scaled image.
     * @param height The desired height of the scaled image.
     * @return A new ImageIcon containing the scaled image.
     */
    public ImageIcon scaleImage(ImageIcon icon, int width, int height) {
        Image img = icon.getImage();

        Image scaledImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);

        return new ImageIcon(scaledImg);

    }

    /**
     * Handles the user signup process.
     * 
     * This method collects user input from the signup form, validates the input,
     * hashes the password, and calls a stored procedure to insert the user data
     * into the database.
     * 
     * @throws SQLException if a database access error occurs.
     */
    private void handleSignup() {
        // Collect user input from the signup form
        String username = loginFrame.getUserNameFieldInSignup().trim();
        String password = new String(loginFrame.getPasswordInSignUp()).trim();
        String confirmPassword = new String(loginFrame.getConfirmPasswordField()).trim();
        String fullName = loginFrame.getFullNameField().trim();
        String dob = loginFrame.getDOB().trim();
        String address = loginFrame.getAddressField().getText().trim();
        String securityQuestion = loginFrame.getSecurityQuestionField().getText().trim();
        String answer = loginFrame.getAnswerField().getText().trim();
        String phoneNumber = loginFrame.getPhoneNumber().trim();
        char gender = loginFrame.getGender();
        String email = loginFrame.getemailField().getText().trim();

        // Validate input
        if (username.isEmpty() || password.isEmpty() || !password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(loginFrame, "Check your input fields!");
            return;
        }

        try {
            LocalDate dobDate = LocalDate.parse(dob);
            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

            sqlConnection = new SqlServerConnection();
            try (Connection conn = sqlConnection.getConnection();
                    CallableStatement stmt = conn.prepareCall("{CALL SignupProcedure(?,?,?,?,?,?,?,?,?,?,?)}")) {

                stmt.setString(1, fullName);
                stmt.setDate(2, Date.valueOf(dobDate));
                stmt.setString(3, address);
                stmt.setString(4, phoneNumber);
                stmt.setString(5, email);
                stmt.setString(6, String.valueOf(gender));
                stmt.setString(7, username);
                stmt.setString(8, hashedPassword);
                stmt.setString(9, securityQuestion);
                stmt.setString(10, answer);

                stmt.registerOutParameter(11, Types.INTEGER);
                stmt.execute();

                int status = stmt.getInt(11);
                switch (status) {
                    case -1 -> JOptionPane.showMessageDialog(loginFrame, "Username already exists!");
                    case -2 -> JOptionPane.showMessageDialog(loginFrame, "Phone number already exists!");
                    case -3 -> JOptionPane.showMessageDialog(loginFrame, "Email already exists!");
                    case -4 -> JOptionPane.showMessageDialog(loginFrame, "Invalid email format!");
                    case 0 -> JOptionPane.showMessageDialog(loginFrame, "Signup successful!");
                    default -> JOptionPane.showMessageDialog(loginFrame, "Signup failed. Please try again!");
                }

                if (status == 0) {
                    loginFrame.closeLoginFrame();
                    loginFrame = new LoginFrame();
                }
            }
        } catch (HeadlessException | SQLException e) {
            JOptionPane.showMessageDialog(loginFrame, "Error: " + e.getMessage());
        }
    }

    /**
     * Handles the withdraw button action.
     * 
     * This method retrieves the current balance of the user, creates a new
     * Withdraw object with the balance, and makes the Withdraw window visible.
     * 
     * @throws SQLException if a database access error occurs.
     */
    private void handleWithdrawButton() throws SQLException {
        double balance = getCurrentBalance();
        withDraw = new Withdraw(balance);
        withDraw.setVisible(true);
    }

    /**
     * Handles the withdraw process.
     * 
     * This method retrieves the current balance of the user, validates the
     * withdrawal amount, checks for sufficient balance, performs the withdrawal,
     * and updates the balance.
     */
    private void handleWithdraw() {
        double currentBalance = getCurrentBalance();

        String amountStr = withDraw.getAmountTextField();

        if (amountStr != null && !amountStr.isEmpty()) {
            try {
                double amountToWithdraw = Double.parseDouble(amountStr);

                if (amountToWithdraw <= 0) {
                    JOptionPane.showMessageDialog(null, "Amount must be greater than zero!");
                    return;
                }

                if (amountToWithdraw > currentBalance) {
                    JOptionPane.showMessageDialog(null, "Insufficient balance!");
                    return;
                }

                boolean success = performWithdrawal(amountToWithdraw);
                if (success) {
                    double newBalance = currentBalance - amountToWithdraw;
                    withDraw.updateBalance(newBalance);
                } else {
                    JOptionPane.showMessageDialog(null, "An error occurred while processing the withdrawal.");
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Invalid amount. Please enter a valid number.");
            }
        } else {
            JOptionPane.showMessageDialog(null, "Withdrawal cancelled.");
        }
    }

    /**
     * Retrieves the current balance of the user.
     *
     * This method connects to the database, calls a stored procedure to get the
     * balance for the current user, and returns the balance.
     *
     * @return the current balance of the user.
     */
    private double getCurrentBalance() {
        double balance = 0;
        try {
            sqlConnection = new SqlServerConnection();
            try (Connection conn = sqlConnection.getConnection();
                    CallableStatement stmt = conn.prepareCall("{CALL GetBalanceByCustomerID(?)}")) {

                String customerID = loadCustomerID();
                stmt.setString(1, customerID);

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        balance = rs.getDouble("Balance");
                    }
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Database error: " + ex.getMessage());
        }
        return balance;
    }

    /**
     * Handles the logout process for the current user.
     * This method displays a confirmation dialog, and if confirmed, it:
     * 1. Removes user preferences (username and CustomerID)
     * 2. Closes the current user GUI
     * 3. Opens a new login frame
     * 
     * If the user cancels the logout, no action is taken.
     */
    private void handleLogout() {
        int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to logout?", "Logout Confirmation",
                JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            Preferences prefs = Preferences.userNodeForPackage(UserGUI.class);
            prefs.remove("username");
            prefs.remove("CustomerID");
            usergui.closeUserGUI();
            loginFrame = new LoginFrame();
        }
    }

    /**
     * This method performs a withdrawal operation for a customer. It updates
     * the
     * customer's account balance
     *
     * 
     * @param amountToWithdraw the amount to withdraw
     *                         Must be greater than zero.
     * 
     * @return {@code true} if the withdrawal is successful; {@code false}
     *         otherwise.
     */
    private boolean performWithdrawal(double amountToWithdraw) {
        if (amountToWithdraw <= 0) {
            JOptionPane.showMessageDialog(null, "Amount to withdraw must be greater than zero.");
            return false;
        }

        try {
            sqlConnection = new SqlServerConnection();
            try (Connection conn = sqlConnection.getConnection();
                    CallableStatement stmt = conn.prepareCall("{CALL UpdateBalanceAndLogTransaction(?, ?, ?)}")) {

                String CustomerID = loadCustomerID();

                stmt.setString(1, CustomerID);
                stmt.setDouble(2, -amountToWithdraw);
                stmt.setString(3, null);

                int rowsUpdated = stmt.executeUpdate();

                if (rowsUpdated > 0) {
                    withDraw.clearAmountTextField();
                    JOptionPane.showMessageDialog(null, "Withdrawal successful!");
                    return true;
                } else {
                    JOptionPane.showMessageDialog(null, "Withdrawal failed");
                    return false;
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Database error: " + ex.getMessage());
            return false;
        }
    }
}
