package com.cms.dao;

import java.sql.*;
import com.cms.db.DBConnection;
import com.cms.model.User;
import com.cms.util.ConsoleFormatter;

public class UserDAO {

    public void register(String name, String pass, String role) {
        if (!role.equalsIgnoreCase("USER") && !role.equalsIgnoreCase("ADMIN")) {
            ConsoleFormatter.error("Role must be either USER or ADMIN!");
            return;
        }

        String sql = "INSERT INTO users(name,password,role) VALUES(?,?,?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setString(2, pass);
            ps.setString(3, role.toUpperCase());

            ps.executeUpdate();
            ConsoleFormatter.success("Registered Successfully!");

        } catch (Exception e) {
            ConsoleFormatter.error("Registration Failed!");
        }
    }

    public User login(String name, String pass) {
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

        } catch (Exception e) { e.printStackTrace(); }

        return false;
    }
}