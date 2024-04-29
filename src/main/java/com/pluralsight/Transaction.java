package com.pluralsight;

import java.util.Scanner;

public class Transaction {
    public static void main(String [] args) {
        FinancialTracker financialTracker = new FinancialTracker();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to Your Financial Tracker App !");

        while (true) {
            System.out.println("\nHome Screen:");
            System.out.println("D) Add Deposit");
            System.out.println("P) Make a Payment (Debit)");
            System.out.println("L) Ledger");
            System.out.println("X) Exit");

            System.out.print("Please select an option: ");
            String choice = scanner.next().toUpperCase();

            switch (choice) {
                case "D":
                    financialTracker.addDeposit(scanner);
                    break;
                case "P":
                    financialTracker.makePayment(scanner);
                    break;
                case "L":
                    financialTracker.displayLedger();
                    break;
                case "X":
                    System.out.println("Exiting Financial Tracker App. Goodbye!");
                    System.exit(0);
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
}




