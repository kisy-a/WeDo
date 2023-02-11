package com.example.wedoapp;

public class Model_ReportedUsers {
    private String ReportCategory, ReportDescription, ReportID, ReportStatus, ReportedID, ReporterID;

    public Model_ReportedUsers() {
    }

    public Model_ReportedUsers(String ReportCategory, String ReportDescription, String ReportID,String ReportStatus,String ReportedID,String ReporterID) {
        this.ReportCategory = ReportCategory;
        this.ReportDescription = ReportDescription;
        this.ReportID = ReportID;
        this.ReportStatus = ReportStatus;
        this.ReportedID = ReportedID;
        this.ReporterID = ReporterID;
    }

    public String getReportCategory() {
        return ReportCategory;
    }
    public void setReportCategory(String ReportCategory) {
        this.ReportCategory = ReportCategory;
    }

    public String getReportDescription() {
        return ReportDescription;
    }
    public void setReportDescription(String ReportDescription) {
        this.ReportDescription = ReportDescription;
    }

    public String getReportID() {
        return ReportID;
    }
    public void setReportID(String ReportID) {
        this.ReportID = ReportID;
    }

    public String getReportStatus() {
        return ReportStatus;
    }
    public void setReportStatus(String ReportStatus) {
        this.ReportStatus = ReportStatus;
    }

    public String getReportedID() {
        return ReportedID;
    }
    public void setReportedID(String ReportedID) {
        this.ReportedID = ReportedID;
    }

    public String getReporterID() {
        return ReporterID;
    }
    public void setReporterID(String ReporterID) {
        this.ReporterID = ReporterID;
    }

}