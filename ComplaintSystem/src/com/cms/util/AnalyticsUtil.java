package com.cms.util;

import java.sql.*;
import com.cms.db.DBConnection;

public class AnalyticsUtil {

    // 1️⃣ Most Common Category
    public static void mostCommon() {
        System.out.println("\n================ Most Common Issue ================");
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(
                     "SELECT category, COUNT(*) as c FROM complaints GROUP BY category ORDER BY c DESC LIMIT 1")) {

            if (rs.next()) {
                System.out.println("Category: " + rs.getString("category") + " | Count: " + rs.getInt("c"));
            }

        } catch (Exception e) { e.printStackTrace(); }
    }

    // 2️⃣ Average Resolution Time (hours)
    public static void averageResolutionTime() {
        System.out.println("\n================ Average Resolution Time ================");
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(
                     "SELECT AVG(TIMESTAMPDIFF(HOUR, created_at, resolved_at)) AS avg_hours FROM complaints WHERE resolved_at IS NOT NULL")) {

            if (rs.next()) {
                System.out.println("Average Time to Resolve Complaints: " + rs.getDouble("avg_hours") + " hours");
            } else {
                System.out.println("No resolved complaints yet!");
            }

        } catch (Exception e) { e.printStackTrace(); }
    }

    // 3️⃣ Priority Distribution (HIGH vs LOW)
    public static void priorityDistribution() {
        System.out.println("\n================ Priority Distribution ================");
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(
                     "SELECT priority, COUNT(*) AS c FROM complaints GROUP BY priority")) {

            System.out.printf("%-10s %-5s\n", "Priority", "Count");
            System.out.println("----------------------");
            while (rs.next()) {
                System.out.printf("%-10s %-5d\n", rs.getString("priority"), rs.getInt("c"));
            }

        } catch (Exception e) { e.printStackTrace(); }
    }

    // 4️⃣ Status Distribution (Pending / In Progress / Resolved)
    public static void statusDistribution() {
        System.out.println("\n================ Status Distribution ================");
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(
                     "SELECT status, COUNT(*) AS c FROM complaints GROUP BY status")) {

            System.out.printf("%-12s %-5s\n", "Status", "Count");
            System.out.println("----------------------");
            while (rs.next()) {
                System.out.printf("%-12s %-5d\n", rs.getString("status"), rs.getInt("c"));
            }

        } catch (Exception e) { e.printStackTrace(); }
    }

    // 5️⃣ Top Complainers (users with max complaints)
    public static void topComplainers() {
        System.out.println("\n================ Top Complainers ================");
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(
                     "SELECT u.name, COUNT(c.id) AS c " +
                             "FROM users u JOIN complaints c ON u.id = c.user_id " +
                             "GROUP BY u.name ORDER BY c DESC LIMIT 5")) {

            System.out.printf("%-15s %-5s\n", "User", "Complaints");
            System.out.println("----------------------");
            while (rs.next()) {
                System.out.printf("%-15s %-5d\n", rs.getString("name"), rs.getInt("c"));
            }

        } catch (Exception e) { e.printStackTrace(); }
    }
}