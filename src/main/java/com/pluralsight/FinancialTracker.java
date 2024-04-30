
package com.pluralsight;


import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;


public class FinancialTracker {


    private static final ArrayList<Transaction> transactions = new ArrayList<Transaction>();
    private static final String FILE_NAME = "transactions.csv";
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String TIME_FORMAT = "HH:mm:ss";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT);
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern(TIME_FORMAT);

    public static void main(String[] args)
            throws IOException {
        loadTransactions(FILE_NAME);
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("Welcome to TransactionApp");
            System.out.println("Choose an option:");
            System.out.println("D) Add Deposit");
            System.out.println("P) Make Payment (Debit)");
            System.out.println("L) Ledger");
            System.out.println("X) Exit");

            String input = scanner.nextLine().trim();

            switch (input.toUpperCase()) {
                case "D":
                    addDeposit(scanner);
                    break;
                case "P":
                    addPayment(scanner);
                    break;
                case "L":
                    ledgerMenu(scanner);
                    break;
                case "X":
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option");
                    break;
            }
        }

        scanner.close();
    }

    public static void loadTransactions(String fileName) throws IOException {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(FILE_NAME));
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 5) {
                    LocalDate date = LocalDate.parse(parts[0], DATE_FORMATTER);
                    LocalTime time = LocalTime.parse(parts[1], TIME_FORMATTER);
                    String vendor = parts[2];
                    String description = parts[3];
                    double amount = Double.parseDouble(parts[4]);
                    transactions.add(new Transaction(date, time, description, vendor,amount));
                } else {
                    System.out.println("Invalid transaction format: " + line);
                }
            }

        } catch (FileNotFoundException e) {
            System.out.println("File not found. Creating a new file");
            throw new RuntimeException(e);
        }
        // This method should load transactions from a file with the given file name.
        // If the file does not exist, it should be created.
        // The transactions should be stored in the `transactions` ArrayList.
        // Each line of the file represents a single transaction in the following format:
        // <date>,<time>,<vendor>,<type>,<amount>
        // For example: 2023-04-29,13:45:00,Amazon,PAYMENT,29.99
        // After reading all the transactions, the file should be closed.
        // If any errors occur, an appropriate error message should be displayed.
    }

    static void addDeposit(Scanner scanner) {
        scanner = new Scanner(System.in);

        System.out.println("Enter the date (yyyy-MM-dd):");
        String userDate = scanner.nextLine();
        LocalDate date = LocalDate.parse(userDate, DATE_FORMATTER);

        System.out.println("Enter the time (HH:mm:ss):");
        String userTime = scanner.nextLine();
        LocalTime time = LocalTime.parse(userTime, TIME_FORMATTER);

        System.out.println("Enter a description of deposit: ");
        String description = scanner.nextLine();

        System.out.println("Enter the vendor:");
        String vendor = scanner.nextLine();

        double amount;
        while (true) {
            System.out.println("Enter the amount:");
            try {
                amount = Double.parseDouble(scanner.nextLine());
                if (amount <= 0) {
                    System.out.println("Amount must be a positive number.");
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid amount format. Please enter a number.");
            }
        }

        Transaction deposit = new Transaction(date,time,description,vendor,amount);
        transactions.add(deposit);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("transactions.csv", true))){
            for (Transaction transaction : transactions) {
                String formattedTransaction = String.format("%s|%s|%s|%s|%.2f\n",transaction.getDate(),transaction.getTime(),
                        transaction.getDescription(),transaction.getVendor(),transaction.getAmount());
                writer.write(formattedTransaction);
                writer.newLine();
            }
            System.out.println("Deposit has been saved to file");
        }catch (IOException e) {
            System.out.println("Error writing file: " + e.getMessage());
        }
        scanner.close();
    }


    private static void addPayment(Scanner scanner) {
        scanner = new Scanner(System.in);

        System.out.println("Enter the date (yyyy-MM-dd):");
        String userDate = scanner.nextLine();
        LocalDate date = LocalDate.parse(userDate, DATE_FORMATTER);

        System.out.println("Enter the time (HH:mm:ss):");
        String userTime = scanner.nextLine();
        LocalTime time = LocalTime.parse(userTime, TIME_FORMATTER);

        System.out.println("Enter a description of payment: ");
        String description = scanner.nextLine();

        System.out.println("Enter the vendor:");
        String vendor = scanner.nextLine();

        double amount;
        while (true) {
            System.out.println("Enter the amount:");
            try {
                amount = Double.parseDouble(scanner.nextLine());
                if (amount <= 0) {
                    System.out.println("Amount must be a positive number");
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid . Please enter a number");
            }
        }

        transactions.add(new Transaction(date, time, vendor, description, -amount)); // Negative amount for payments
        System.out.println("Your payment was added successfully!");
    }

    private static void ledgerMenu(Scanner scanner) {
        boolean running = true;
        while (running) {
            System.out.println("Ledger");
            System.out.println("Choose an option:");
            System.out.println("A) A`ll");
            System.out.println("D) Deposits");
            System.out.println("P) Payments");
            System.out.println("R) Reports");
            System.out.println("H) Home");

            String input = scanner.nextLine().trim();

            switch (input.toUpperCase()) {
                case "A":
                    displayLedger();
                    break;
                case "D":
                    displayDeposits();
                    break;
                case "P":
                    displayPayments();
                    break;
                case "R":
                    reportsMenu(scanner);
                    break;
                case "H":
                    running = false;
                default:
                    System.out.println("Invalid option");
                    break;
            }
        }
    }

    static void displayLedger() {
        System.out.println("Ledger");
        for (Transaction transaction: transactions)
        System.out.println(transaction);
        // This method should display a table of all transactions in the `transactions` ArrayList.
        // The table should have columns for date, time, vendor, type, and amount.
    }

    private static void displayDeposits() {
        System.out.println("Date\nTime\nVendor\nAmount");
        System.out.println("xxxxxxxxxxxxxxxxxx");

        for (Transaction deposit : transactions) {
            double amount = deposit.getAmount();
            if (amount > 0) {
                System.out.println(deposit);
            }


        }

        // This method should display a table of all deposits in the `transactions` ArrayList.
        // The table should have columns for date, time, vendor, and amount.
    }

    private static void displayPayments() {
        System.out.println("Date\nTime\nVendor\nAmount");
        System.out.println("xxxxxxxxxxxxxxxxxx");

        for (Transaction payment : transactions) {
            double amount = payment.getAmount();
            if (amount < 0) {
                System.out.println(payment);
            }
            }



                // This method should display a table of all payments in the `transactions` ArrayList.
                // The table should have columns for date, time, vendor, and amount.
            }

    private static void reportsMenu(Scanner scanner) {
        boolean running = true;
        while (running) {
            System.out.println("Reports");
            System.out.println("Choose an option:");
            System.out.println("1) Month To Date");
            System.out.println("2) Previous Month");
            System.out.println("3) Year To Date");
            System.out.println("4) Previous Year");
            System.out.println("5) Search by Vendor");
            System.out.println("0) Back");

            String input = scanner.nextLine().trim();

            switch (input) {
                case "1":
                    LocalDate currentDate = LocalDate.now();
                    LocalDate startCurrentMonth = currentDate.withDayOfMonth(1);
                    LocalDate endCurrentMonth = currentDate.withDayOfMonth(currentDate.lengthOfMonth());
                    filterTransactionsByDate(transactions, startCurrentMonth, endCurrentMonth);
                    break;
                    // Generate a report for all transactions within the current month,
                    // including the date, vendor, and amount for each transaction.
                case "2":
                    
                    // Generate a report for all transactions within the previous month,
                    // including the date, vendor, and amount for each transaction.
                case "3":
                    // Generate a report for all transactions within the current year,
                    // including the date, vendor, and amount for each transaction.

                case "4":
                    // Generate a report for all transactions within the previous year,
                    // including the date, vendor, and amount for each transaction.
                case "5":
                    // Prompt the user to enter a vendor name, then generate a report for all transactions
                    // with that vendor, including the date, vendor, and amount for each transaction.
                case "0":
                    running = false;
                default:
                    System.out.println("Invalid option");
                    break;
            }
        }
    }

    private static void filterTransactionsByDate(ArrayList<Transaction> transactions, LocalDate startDate, LocalDate endDate) {
        boolean foundTransactions = false;

        System.out.println("Transactions between " + startDate + " and " + endDate + ":");

        for (Transaction transaction : transactions) {
            LocalDate transactionDate = transaction.getDate();
            if (!transactionDate.isBefore(startDate) || !transactionDate.isAfter(endDate)) {
                System.out.println(transaction);
                foundTransactions = true;
            }
        }

        if (!foundTransactions) {
            System.out.println("No transactions found between " + startDate + " and " + endDate + ".");
        }
    }

    private static void filterTransactionsByVendor(String vendor) {
    }

    public void printTransactionsByVendor(List<Transaction> transactions, String vendor) {
        boolean foundTransactions = false;

        System.out.println("Transactions for vendor: " + vendor);

        for (Transaction transaction : transactions) {
            if (transaction.getVendor().equalsIgnoreCase(vendor)) {
                System.out.println(transaction); // Assuming Transaction has a toString() method
                foundTransactions = true;
            }
        }

        if (!foundTransactions) {
            System.out.println("No transactions found for vendor: " + vendor);
        }
    }
}








// This method filters the transactions by vendor and prints a report to the console.
// It takes one parameter: vendor, which represents the name of the vendor to filter by.
// The method loops through the transactions list and checks each transaction's vendor name against the specified vendor name.
// Transactions with a matching vendor name are printed to the console.
// If no transactions match the specified vendor name, the method prints a message indicating that there are no results.

