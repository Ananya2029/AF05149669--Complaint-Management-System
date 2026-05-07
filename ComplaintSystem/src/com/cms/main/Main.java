package com.cms.main;

import java.util.*;
import com.cms.dao.*;
import com.cms.model.User;
import com.cms.service.*;
import com.cms.util.*;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        UserDAO userDAO = new UserDAO();
        ComplaintService service = new ComplaintService();

        userDAO.createDefaultAdmin();

        while (true) {

            ConsoleFormatter.printHeader("Complaint Management System");

            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Enter choice: ");

            int ch;

            try {
                ch = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                ConsoleFormatter.error("Invalid input! Enter numbers only.");
                continue;
            }

            if (ch == 1) {

                System.out.print("Name: ");
                String n = sc.nextLine();

                System.out.print("Password: ");
                String p = sc.nextLine();

                String r = "USER";

                userDAO.register(n, p, r);

            } else if (ch == 2) {

                int attempts = 0;
                User loggedUser = null;

                while (attempts < 3) {

                    System.out.print("Name: ");
                    String n = sc.nextLine();

                    System.out.print("Password: ");
                    String p = sc.nextLine();

                    loggedUser = userDAO.login(n, p);

                    if (loggedUser != null) {
                        break;
                    }

                    attempts++;
                    ConsoleFormatter.error("Invalid Login! Attempts left: " + (3 - attempts));
                }

                if (loggedUser == null) {
                    ConsoleFormatter.error("Too many failed attempts!");
                    continue;
                }

                if (loggedUser.role.equalsIgnoreCase("ADMIN")) {
                    adminMenu(sc, service, loggedUser);
                } else {
                    userMenu(sc, service, loggedUser);
                }

            } else if (ch == 3) {

                ConsoleFormatter.success("Exiting system...");
                break;

            } else {
                ConsoleFormatter.error("Invalid choice! Please select 1, 2, or 3.");
            }
        }

        sc.close();
    }

    static void adminMenu(Scanner sc, ComplaintService s, User loggedAdmin) {

        while (true) {

            ConsoleFormatter.printHeader("Admin Dashboard - " + loggedAdmin.name);

            System.out.println("1. Update Complaint Status");
            System.out.println("2. View All Complaints");
            System.out.println("3. Analytics");
            System.out.println("4. Dashboard");
            System.out.println("5. Search Complaint by ID");
            System.out.println("6. Auto Escalate Complaints");
            System.out.println("7. Exit");
            System.out.print("Enter choice: ");

            int ch;

            try {
                ch = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                ConsoleFormatter.error("Invalid input!");
                continue;
            }

            if (ch == 1) {

                int id;

                try {
                    System.out.print("Complaint ID: ");
                    id = Integer.parseInt(sc.nextLine());
                } catch (NumberFormatException e) {
                    ConsoleFormatter.error("Invalid Complaint ID!");
                    continue;
                }

                System.out.print("Status (Pending/In Progress/Resolved/Rejected): ");
                String st = sc.nextLine();

                s.updateStatus(id, st);

            } else if (ch == 2) {

                s.viewAll();

            } else if (ch == 3) {

                AnalyticsUtil.mostCommon();
                AnalyticsUtil.averageResolutionTime();
                AnalyticsUtil.priorityDistribution();
                AnalyticsUtil.statusDistribution();
                AnalyticsUtil.topComplainers();

            } else if (ch == 4) {

                Dashboard.showDashboard();

            } else if (ch == 5) {

                int id;

                try {
                    System.out.print("Enter Complaint ID: ");
                    id = Integer.parseInt(sc.nextLine());
                } catch (NumberFormatException e) {
                    ConsoleFormatter.error("Invalid Complaint ID!");
                    continue;
                }

                new ComplaintDAO().searchComplaintById(id);

            } else if (ch == 6) {

                new ComplaintDAO().autoEscalateComplaints();

            } else if (ch == 7) {
                break;

            } else {

                ConsoleFormatter.error("Invalid choice!");
            }
        }
    }

    static void userMenu(Scanner sc, ComplaintService s, User loggedUser) {

        while (true) {

            ConsoleFormatter.printHeader("User Dashboard - " + loggedUser.name);

            System.out.println("1. Add Complaint");
            System.out.println("2. View My Complaints");
            System.out.println("3. Rate Complaint");
            System.out.println("4. Exit");
            System.out.print("Enter choice: ");

            int ch;

            try {
                ch = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                ConsoleFormatter.error("Invalid input! Enter numbers only.");
                continue;
            }

            if (ch == 1) {

                System.out.print("Category (Electricity/Water/Billing/Technical/Other): ");
                String cat = sc.nextLine();

                System.out.print("Description: ");
                String desc = sc.nextLine();

                s.createComplaint(loggedUser.id, cat, desc);

            } else if (ch == 2) {

                s.viewUser(loggedUser.id);

            } else if (ch == 3) {

                try {
                    System.out.print("Complaint ID: ");
                    int id = Integer.parseInt(sc.nextLine());

                    System.out.print("Rating (1-5): ");
                    int rating = Integer.parseInt(sc.nextLine());

                    new ComplaintDAO().addFeedback(id, rating);

                } catch (NumberFormatException e) {
                    ConsoleFormatter.error("Invalid input!");
                }

            } else if (ch == 4) {

                break;

            } else {

                ConsoleFormatter.error("Invalid choice!");
            }
        }
    }
}