package com.cms.service;

import com.cms.dao.*;
import com.cms.util.ConsoleFormatter;

public class ComplaintService {

    ComplaintDAO dao = new ComplaintDAO();
    UserDAO userDAO = new UserDAO();

    public void createComplaint(int uid, String cat, String desc) {

        if (!userDAO.userExists(uid)) {
            ConsoleFormatter.error("User not found!");
            return;
        }

        String priority = (desc.toLowerCase().contains("urgent") || cat.equalsIgnoreCase("Electricity")) ? "HIGH" : "LOW";
        dao.addComplaint(uid, cat, desc, priority);
    }

    public void viewAll() { dao.viewAll(); }

    public void viewUser(int id) { dao.viewByUser(id); }

    public void updateStatus(int id, String status) { dao.updateStatus(id, status); }
}