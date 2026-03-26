package com.cms.util;

public class ConsoleFormatter {

    public static void printHeader(String title) {
        System.out.println("\n=======================================");
        System.out.println("   " + title.toUpperCase());
        System.out.println("=======================================");
    }

    public static void printLine() {
        System.out.println("---------------------------------------");
    }

    public static void printComplaintTableHeader() {
        printLine();
        System.out.printf("%-5s %-15s %-12s %-10s\n", "ID", "Category", "Status", "Priority");
        printLine();
    }

    public static void printComplaintRow(int id, String cat, String status, String priority) {
        System.out.printf("%-5d %-15s %-12s %-10s\n", id, cat, status, priority);
    }

    public static void success(String msg) {
        System.out.println("[SUCCESS] " + msg);
    }

    public static void error(String msg) {
        System.out.println("[ERROR] " + msg);
    }
}