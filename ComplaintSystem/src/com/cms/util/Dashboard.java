package com.cms.util;

import java.sql.*;
import com.cms.db.DBConnection;

public class Dashboard {

    // Draw horizontal bar for visual representation
    private static void drawBar(int count) {
        for (int i = 0; i < count; i++) System.out.print("█");
        System.out.println(" " + count);
    }

    // Display Priority Distribution as bars
    public static void priorityChart() {
        System.out.println("\n================ Priority Chart ================");
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT priority, COUNT(*) AS c FROM complaints GROUP BY priority")) {

            while (rs.next()) {
                System.out.printf("%-10s : ", rs.getString("priority"));
                drawBar(rs.getInt("c"));
            }

        } catch (Exception e) { e.printStackTrace(); }
    }

    // Display Status Distribution as bars
    public static void statusChart() {
        System.out.println("\n================ Status Chart ================");
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT status, COUNT(*) AS c FROM complaints GROUP BY status")) {

            while (rs.next()) {
                System.out.printf("%-12s : ", rs.getString("status"));
                drawBar(rs.getInt("c"));
            }

        } catch (Exception e) { e.printStackTrace(); }
    }

    // Top 5 Complainers as bars
    public static void topComplainersChart() {
        System.out.println("\n================ Top Complainers ================");
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(
                     "SELECT u.name, COUNT(c.id) AS c " +
                             "FROM users u JOIN complaints c ON u.id = c.user_id " +
                             "GROUP BY u.name ORDER BY c DESC LIMIT 5")) {

            while (rs.next()) {
                System.out.printf("%-15s : ", rs.getString("name"));
                drawBar(rs.getInt("c"));
            }

        } catch (Exception e) { e.printStackTrace(); }
    }

    // Call all charts together
    public static void showDashboard() {
        System.out.println("\n============== ADMIN DASHBOARD ==============");
        priorityChart();
        statusChart();
        topComplainersChart();
        System.out.println("============================================\n");
    }
}