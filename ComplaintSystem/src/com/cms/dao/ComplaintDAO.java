package com.cms.dao;

import java.sql.*;
import com.cms.db.DBConnection;
import com.cms.util.ConsoleFormatter;

public class ComplaintDAO {

	public void addComplaint(int userId, String ticket, String cat, String desc, String priority) {

	    String sql = "INSERT INTO complaints(user_id,ticket_number,category,description,status,priority,escalated) VALUES(?,?,?,?,?,?,?)";

	    try (Connection con = DBConnection.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql)) {

	        ps.setInt(1, userId);
	        ps.setString(2, ticket);
	        ps.setString(3, cat);
	        ps.setString(4, desc);
	        ps.setString(5, "Pending");
	        ps.setString(6, priority);
	        ps.setBoolean(7, false);

	        ps.executeUpdate();

	        ConsoleFormatter.success("Complaint Added Successfully! Ticket Number: " + ticket);

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

    public boolean complaintExists(int id) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT id FROM complaints WHERE id=?")) {

            ps.setInt(1, id);
            return ps.executeQuery().next();

        } catch (Exception e) {
            return false;
        }
    }
    
    public void updateStatus(int id, String status) {

        if (!complaintExists(id)) {
            ConsoleFormatter.error("Complaint ID not found!");
            return;
        }

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "UPDATE complaints SET status=?, resolved_at=NOW() WHERE id=?")) {

            ps.setString(1, status);
            ps.setInt(2, id);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                ConsoleFormatter.success("Status Updated");
            } else {
                ConsoleFormatter.error("Update Failed");
            }

        } catch (Exception e) {
            ConsoleFormatter.error("Update Failed");
        }
    }
    public void searchComplaintById(int id) {

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT * FROM complaints WHERE id=?")) {

            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                ConsoleFormatter.printHeader("Complaint Details");

                System.out.println("Ticket Number : " + rs.getString("ticket_number"));
                System.out.println("Category      : " + rs.getString("category"));
                System.out.println("Description   : " + rs.getString("description"));
                System.out.println("Status        : " + rs.getString("status"));
                System.out.println("Priority      : " + rs.getString("priority"));
                System.out.println("Escalated     : " + rs.getBoolean("escalated"));
                System.out.println("Created At    : " + rs.getTimestamp("created_at"));
                System.out.println("Updated At    : " + rs.getTimestamp("updated_at"));
                System.out.println("Resolved At   : " + rs.getTimestamp("resolved_at"));

            } else {
                ConsoleFormatter.error("Complaint not found!");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void addFeedback(int complaintId, int rating) {

        if (rating < 1 || rating > 5) {
            ConsoleFormatter.error("Rating must be between 1 and 5!");
            return;
        }

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "UPDATE complaints SET feedback_rating=? WHERE id=? AND status='Resolved'")) {

            ps.setInt(1, rating);
            ps.setInt(2, complaintId);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                ConsoleFormatter.success("Feedback submitted successfully!");
            } else {
                ConsoleFormatter.error("Only resolved complaints can be rated.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void autoEscalateComplaints() {

        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement()) {

            int rows = st.executeUpdate(
                    "UPDATE complaints " +
                    "SET escalated=TRUE, status='Escalated' " +
                    "WHERE status='Pending' AND created_at < NOW() - INTERVAL 7 DAY"
            );

            ConsoleFormatter.success(rows + " complaint(s) escalated successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}