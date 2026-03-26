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

        while (true) {

            ConsoleFormatter.printHeader("Complaint Management System");

            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Enter choice: ");

            int ch = sc.nextInt(); sc.nextLine();

            if (ch == 1) {
                System.out.print("Name: ");
                String n = sc.nextLine();

                System.out.print("Password: ");
                String p = sc.nextLine();

                System.out.print("Role (USER/ADMIN): ");
                String r = sc.nextLine();

                userDAO.register(n, p, r);

            } else if (ch == 2) {
                System.out.print("Name: ");
                String n = sc.nextLine();

                System.out.print("Password: ");
                String p = sc.nextLine();

                User loggedUser = userDAO.login(n, p);

                if (loggedUser == null) {
                    ConsoleFormatter.error("Invalid Login!");
                } else if (loggedUser.role.equalsIgnoreCase("ADMIN")) {
                    adminMenu(sc, service, loggedUser);
                } else {
                    userMenu(sc, service, loggedUser);
                }

            } else break;
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
            System.out.println("5. Exit");

            int ch = sc.nextInt();

            if (ch == 1) {
                System.out.print("Complaint ID: ");
                int id = sc.nextInt();

                System.out.print("Status: ");
                String st = sc.next();

                s.updateStatus(id, st);

            } else if (ch == 2) {
                s.viewAll();

            } else if (ch == 3) { // Analytics
                AnalyticsUtil.mostCommon();
                AnalyticsUtil.averageResolutionTime();
                AnalyticsUtil.priorityDistribution();
                AnalyticsUtil.statusDistribution();
                AnalyticsUtil.topComplainers();

            } else if (ch == 4) {
            	Dashboard.showDashboard();
            } else if (ch == 5) {
                break;
            }
        }
    }

    static void userMenu(Scanner sc, ComplaintService s, User loggedUser) {

        while (true) {
            ConsoleFormatter.printHeader("User Dashboard - " + loggedUser.name);

            System.out.println("1. Add Complaint");
            System.out.println("2. View My Complaints");
            System.out.println("3. Exit");

            int ch = sc.nextInt(); sc.nextLine();

            if (ch == 1) {
                System.out.print("Category: ");
                String cat = sc.nextLine();

                System.out.print("Description: ");
                String desc = sc.nextLine();

                s.createComplaint(loggedUser.id, cat, desc);

            } else if (ch == 2) {
                s.viewUser(loggedUser.id);

            } else if (ch == 3) {
                break;
            }
        }
    }
}