
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
                    String description = parts[2];
                    String vendor = parts[3];
                    double amount = Double.parseDouble(parts[4]);
                    transactions.add(new Transaction(date, time, description, vendor,amount));
                } else {
                    System.out.println("Invalid transaction format: " + line);
                }
            }
            bufferedReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found. Creating a new file");
            throw new RuntimeException(e);
        }

    }

    static void addDeposit(Scanner scanner) {

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
                if (amount < 0) {
                    System.out.println("Amount must be a positive number.");
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid amount. Please enter a number.");
            }
        }

        Transaction deposit = new Transaction(date,time,description,vendor,amount);
        transactions.add(deposit);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("transactions.csv", true))){
             {
                String formattedTransaction = String.format("%s|%s|%s|%s|%.2f\n",deposit.getDate(),deposit.getTime(),
                        deposit.getDescription(),deposit.getVendor(),deposit.getAmount());
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
                if (amount < 0) {
                    System.out.println("Amount must be a positive number");
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid . Please enter a number");
            }
        }
        Transaction payment = new Transaction(date, time, description,vendor,-amount);

        transactions.add(payment); // Negative amount for payments
        System.out.println("Your payment was added successfully!");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("transactions.csv", true))){
             {
                String formattedTransaction = String.format("%s|%s|%s|%s|%.2f\n",payment.getDate(),payment.getTime(),
                        payment.getDescription(),payment.getVendor(),payment.getAmount());
                writer.write(formattedTransaction);
                writer.newLine();
            }
            System.out.println("Payment has been saved to file");
        }catch (IOException e) {
            System.out.println("Error writing file: " + e.getMessage());
        }
        scanner.close();
    }

    private static void ledgerMenu(Scanner scanner) {
        boolean running = true;
        while (running) {
            System.out.println("Ledger");
            System.out.println("Choose an option:");
            System.out.println("A) All");
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
                    break;
                default:
                    System.out.println("Invalid option");
                    break;
            }
        }
    }

    static void displayLedger() {
        System.out.println("Ledger");
        for (Transaction transaction: transactions) {
            System.out.println(transaction);
        }
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

            // https://docs.oracle.com/javase/8/docs/api/java/time/LocalDate.html#minusMonths-long-

            switch (input) {
                case "1":
                    LocalDate currentDate = LocalDate.now();
                    LocalDate startCurrentMonth = currentDate.withDayOfMonth(1);
                    filterTransactionsByDate( startCurrentMonth, currentDate);
                    break;

                case "2":
                    LocalDate previousDate = LocalDate.now().minusMonths(1);
                    LocalDate startPreviousMonth = previousDate.withDayOfMonth(1);
                    LocalDate endPreviousMonth = previousDate.withDayOfMonth(previousDate.lengthOfMonth());
                    filterTransactionsByDate( startPreviousMonth, endPreviousMonth);
                    break;

                case "3":
                    currentDate = LocalDate.now();
                    LocalDate startCurrentYear = currentDate.withDayOfYear(1);
                    filterTransactionsByDate( startCurrentYear, currentDate);
                    break;

                case "4":
                    previousDate = LocalDate.now().minusYears(1);
                    LocalDate startPreviousYear = previousDate.withDayOfYear(1);
                    LocalDate endPreviousYear = previousDate.withDayOfYear(previousDate.lengthOfYear());
                    filterTransactionsByDate( startPreviousYear, endPreviousYear);
                    break;

                case "5":
                    System.out.println("Enter the vendor:");
                    String vendor = scanner.nextLine();
                    printTransactionsByVendor(transactions,vendor);
                    break;

                case "0":
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option");
                    break;
            }
        }
    }

    private static void filterTransactionsByDate(LocalDate startDate, LocalDate endDate) {
        boolean foundTransactions = false;

        System.out.println("Transactions between " + startDate + " and " + endDate + ":");

        for (Transaction transaction : transactions) {
            LocalDate transactionDate = transaction.getDate();
            if (!transactionDate.isBefore(startDate) && !transactionDate.isAfter(endDate)) {
                System.out.println(transaction);
                foundTransactions = true;
            }
        }

        if (!foundTransactions) {
            System.out.println("No transactions found between " + startDate + " and " + endDate + ".");
        }

    }

    private static void printTransactionsByVendor(List<Transaction> transactions, String vendor) {
        boolean foundTransactions = false;

        System.out.println("Transactions for vendor: " + vendor);

        for (Transaction transaction : transactions) {
            if (transaction.getVendor().equalsIgnoreCase(vendor)) {
                System.out.println(transaction);
                foundTransactions = true;
            }
        }

        if (!foundTransactions) {
            System.out.println("No transactions found for vendor: " + vendor);
        }
    }
}










