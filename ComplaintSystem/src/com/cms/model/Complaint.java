package com.cms.model;

import java.sql.Timestamp;

public class Complaint {
    public int id;
    public int userId;
    public String category;
    public String description;
    public String status;
    public String priority;
    public Timestamp createdAt;
    public Timestamp resolvedAt;
}