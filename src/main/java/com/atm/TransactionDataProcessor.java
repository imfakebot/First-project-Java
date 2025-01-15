package com.atm;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class TransactionDataProcessor {
    /**
     * Aggregates transactionhistory data by month and type (Deducated or Received).
     *
     * @param transactions A list of transaction history data. Each transaction
     *                     contains the following fields:
     *                     - date: The date and time of the transaction in the
     *                     format "yyyy-MM-dd HH:mm:ss" or "yyyy-MM-dd".
     *                     - transactionType: The type of transaction, which can be
     *                     "Withdrawal", "Deposit", or "Transfer".
     *                     - amount: The amount of money involved in the
     *                     transaction.
     *
     * @return A map where the keys are the months in the format "yyyy-MM" and the
     *         values are maps.
     *         Each inner map contains the types
     * 
     *         of transactions ("Deducated" or "Received") as keys and the total
     *         amounts as values.
     *         The map is sorted by the month in ascending order.
     */

    public static Map<String, Map<String, Double>> aggregateTransactionsByMonth(
            List<DatabaseHelper.TransactionHistory> transactions) {
        Map<String, Map<String, Double>> aggregatedData = new TreeMap<>();

        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .appendPattern("yyyy-MM-dd HH:mm:ss")
                .optionalStart()
                .appendFraction(ChronoField.MILLI_OF_SECOND, 1, 3, true)
                .optionalEnd()
                .optionalStart()
                .appendPattern("[ HH:mm:ss]")
                .optionalEnd()
                .toFormatter();

        for (DatabaseHelper.TransactionHistory transaction : transactions) {
            try {
                LocalDateTime dateTime = LocalDateTime.parse(transaction.getDate(), formatter);
                String monthYear = dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM"));

                aggregatedData.putIfAbsent(monthYear, new HashMap<>());
                Map<String, Double> monthlyData = aggregatedData.get(monthYear);

                String type;
                if (transaction.getTransactionType().equals("Withdrawal") ||
                        (transaction.getTransactionType().equals("Transfer") && transaction.getAmount() < 0)) {
                    type = "Deducated";
                } else if (transaction.getTransactionType().equals("Deposit") ||
                        (transaction.getTransactionType().equals("Transfer") && transaction.getAmount() > 0)) {
                    type = "Received";
                } else {
                    continue;
                }

                monthlyData.put(type, monthlyData.getOrDefault(type, 0.0) + Math.abs(transaction.getAmount()));
            } catch (DateTimeParseException e) {
                System.err.println("Error parsing date: " + transaction.getDate());
            }
        }

        return aggregatedData;
    }
}
