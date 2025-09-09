package com.insurance.hip.entity;

public class TechStack {

    private String category;
    private String details;

    public TechStack(String category, String details) {
        this.category = category;
        this.details = details;
    }

    public String getCategory() {
        return category;
    }

    public String getDetails() {
        return details;
    }
}
