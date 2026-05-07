package com.cms.service;

import com.cms.dao.*;
import com.cms.util.ConsoleFormatter;
import com.cms.util.ValidationUtil;

public class ComplaintService {

    ComplaintDAO dao = new ComplaintDAO();
    UserDAO userDAO = new UserDAO();

    public void createComplaint(int uid, String cat, String desc) {

        if (!userDAO.userExists(uid)) {
            ConsoleFormatter.error("User not found!");
            return;
        }

        if (!ValidationUtil.isValidCategory(cat)) {
            ConsoleFormatter.error("Invalid category!");
            return;
        }

        if (!ValidationUtil.isValidDescription(desc)) {
            ConsoleFormatter.error("Description must be 10–500 characters.");
            return;
        }

        String priority;

        String lowerDesc = desc.toLowerCase();
        String lowerCat = cat.toLowerCase();

        if (lowerDesc.contains("urgent") ||
            lowerDesc.contains("fire") ||
            lowerDesc.contains("danger") ||
            lowerDesc.contains("emergency") ||
            lowerDesc.contains("short circuit") ||
            lowerCat.contains("fire") ||
            lowerCat.contains("electricity") ||
            lowerCat.contains("security")) {

            priority = "HIGH";

        } else if (lowerDesc.contains("delay") ||
                   lowerDesc.contains("slow") ||
                   lowerDesc.contains("issue") ||
                   lowerCat.contains("internet") ||
                   lowerCat.contains("network")) {

            priority = "MEDIUM";

        } else {

            priority = "LOW";
        }

        String ticket = "CMP-" + java.time.Year.now().getValue() + "-" + uid + "-" + (System.currentTimeMillis() % 10000);
        dao.addComplaint(uid, ticket, cat, desc, priority);
    }

    public void viewAll() {
        dao.viewAll();
    }

    public void viewUser(int id) {
        dao.viewByUser(id);
    }

    public void updateStatus(int id, String status) {

        if (status.equalsIgnoreCase("done")) {
            status = "Resolved";
        }

        if (!ValidationUtil.isValidStatus(status)) {
            ConsoleFormatter.error("Invalid status! Use Pending / In Progress / Resolved / Rejected");
            return;
        }

        dao.updateStatus(id, status);
    }
}