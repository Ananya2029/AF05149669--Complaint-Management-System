package com.cms.dao;

import java.sql.*;
import com.cms.db.DBConnection;
import com.cms.util.ConsoleFormatter;

public class ComplaintDAO {

    public void addComplaint(int userId, String cat, String desc, String priority) {
        String sql = "INSERT INTO complaints(user_id,category,description,status,priority) VALUES(?,?,?,'Pending',?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setString(2, cat);
            ps.setString(3, desc);
            ps.setString(4, priority);

            ps.executeUpdate();
            ConsoleFormatter.success("Complaint Added Successfully");

        } catch (Exception e) {
            ConsoleFormatter.error("Failed to add complaint");
        }
    }

    public void viewAll() {
        ConsoleFormatter.printHeader("All Complaints");

        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM complaints")) {

            ConsoleFormatter.printComplaintTableHeader();

            while (rs.next()) {
                ConsoleFormatter.printComplaintRow(
                        rs.getInt("id"),
                        rs.getString("category"),
                        rs.getString("status"),
                        rs.getString("priority")
                );
            }

        } catch (Exception e) { e.printStackTrace(); }
    }

    public void viewByUser(int uid) {
        ConsoleFormatter.printHeader("Your Complaints");

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT * FROM complaints WHERE user_id=?")) {

            ps.setInt(1, uid);
            ResultSet rs = ps.executeQuery();

            ConsoleFormatter.printComplaintTableHeader();

            while (rs.next()) {
                ConsoleFormatter.printComplaintRow(
                        rs.getInt("id"),
                        rs.getString("category"),
                        rs.getString("status"),
                        rs.getString("priority")
                );
            }

        } catch (Exception e) { e.printStackTrace(); }
    }

    public void updateStatus(int id, String status) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement("UPDATE complaints SET status=?, resolved_at=NOW() WHERE id=?")) {

            ps.setString(1, status);
            ps.setInt(2, id);
            ps.executeUpdate();

            ConsoleFormatter.success("Status Updated");

        } catch (Exception e) {
            ConsoleFormatter.error("Update Failed");
        }
    }
}