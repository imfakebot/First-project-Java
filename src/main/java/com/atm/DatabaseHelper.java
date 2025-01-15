package com.atm;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

public class DatabaseHelper {
    private final SqlServerConnection sqlConnection;

    public DatabaseHelper() {
        this.sqlConnection = new SqlServerConnection();
    }

    /**
     * Retrieves the transaction history for a specific customer from the database.
     *
     * @param customerID The unique identifier of the customer.
     * @return A list of {@link TransactionHistory} objects representing the
     *         transaction history of the customer.
     *         If an error occurs while fetching the transaction history, an empty
     *         list is returned.
     */
    public List<TransactionHistory> getTransactionHistory(String customerID) {
        List<TransactionHistory> transactions = new ArrayList<>();
        String procedureCall = "{CALL GetTransactionDetailsByCustomerID(?)}";

        try (Connection connection = sqlConnection.getConnection();
                CallableStatement statement = connection.prepareCall(procedureCall)) {

            statement.setString(1, customerID);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    transactions.add(new TransactionHistory(
                            resultSet.getString("TransactionDate"),
                            resultSet.getString("TransactionType"),
                            resultSet.getDouble("Amount"),
                            resultSet.getDouble("BalanceAfter"),
                            resultSet.getString("SenderFullName"),
                            resultSet.getString("ReceiverFullName"),
                            resultSet.getString("TransactionContent")));

                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error fetching transaction history: " + e.getMessage());
        }

        return transactions;
    }

    public static class TransactionHistory {
        private final String date;
        private final String transactionType;
        private final double amount;
        private final double balanceAfter;
        private final String senderFullName;
        private final String ReceiverFullName;
        private final String transactionContent;

        public TransactionHistory(String date, String transactionType, double amount, double balanceAfter,
                String senderFullName,
                String ReceiverFullName, String transactionContent) {
            this.date = date;
            this.transactionType = transactionType;
            this.amount = amount;
            this.balanceAfter = balanceAfter;
            this.senderFullName = senderFullName;
            this.ReceiverFullName = ReceiverFullName;
            this.transactionContent = transactionContent;
        }

        public String getDate() {
            return date;
        }

        public String getTransactionType() {
            return transactionType;
        }

        public double getAmount() {
            return amount;
        }

        public double getBalanceAfter() {
            return balanceAfter;
        }

        public String getSenderFullName() {
            return senderFullName;
        }

        public String getReceiverFullName() {
            return ReceiverFullName;
        }

        public String getTransactionContent() {
            return transactionContent;
        }
    }
}
