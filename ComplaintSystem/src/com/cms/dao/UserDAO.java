package com.cms.dao;
import com.cms.util.HashUtil;

import java.sql.*;
import com.cms.db.DBConnection;
import com.cms.model.User;
import com.cms.util.ConsoleFormatter;
import com.cms.util.ValidationUtil;

public class UserDAO {

    public boolean usernameExists(String name) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT id FROM users WHERE name=?")) {

            ps.setString(1, name);
            return ps.executeQuery().next();

        } catch (Exception e) {
            return false;
        }
    }

    public void register(String name, String pass, String role) {

        if (!ValidationUtil.isValidName(name)) {
            ConsoleFormatter.error("Invalid name! Only letters, 3–30 chars.");
            return;
        }

        if (!ValidationUtil.isValidPassword(pass)) {
            ConsoleFormatter.error("Password must contain uppercase, lowercase, number, special character.");
            return;
        }

        if (!ValidationUtil.isValidRole(role)) {
            ConsoleFormatter.error("Only USER registration allowed!");
            return;
        }

        if (usernameExists(name)) {
            ConsoleFormatter.error("Username already exists!");
            return;
        }

        pass = HashUtil.hashPassword(pass);
        String sql = "INSERT INTO users(name,password,role) VALUES(?,?,?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setString(2, pass);
            ps.setString(3, "USER");

            ps.executeUpdate();
            ConsoleFormatter.success("Registered Successfully!");

        } catch (Exception e) {
            ConsoleFormatter.error("Registration Failed!");
        }
    }

    public User login(String name, String pass) {
    	pass = HashUtil.hashPassword(pass);
        String sql = "SELECT id, role FROM users WHERE name=? AND password=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setString(2, pass);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                User u = new User();
                u.id = rs.getInt("id");
                u.name = name;
                u.role = rs.getString("role");
                return u;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean userExists(int id) {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement("SELECT id FROM users WHERE id=?")) {

            ps.setInt(1, id);
            return ps.executeQuery().next();

        } catch (Exception e) {
            return false;
        }
    }
    public void createDefaultAdmin() {

        String checkSql = "SELECT id FROM users WHERE role='ADMIN' LIMIT 1";
        String hashedPassword = HashUtil.hashPassword("Admin@123");
        String insertSql = "INSERT INTO users(name,password,role) VALUES('admin','" + hashedPassword + "','ADMIN')";

        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement()) {

            ResultSet rs = st.executeQuery(checkSql);

            if (!rs.next()) {
                st.executeUpdate(insertSql);
                ConsoleFormatter.success("Default Admin Created! Username: admin | Password: Admin@123");
            }

        } catch (Exception e) {
            ConsoleFormatter.error("Failed to create default admin.");
        }
    }
}