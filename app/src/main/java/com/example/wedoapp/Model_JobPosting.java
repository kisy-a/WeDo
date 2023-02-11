package com.example.wedoapp;

public class Model_JobPosting {

    private String PBudget,PCategory,PDeliveryDays,PDeliveryDate,PDeliveryTime,PDescription,PStatus,PTitle,ProjectID,UID ;


    public Model_JobPosting() {
    }

    public Model_JobPosting(String PBudget, String PCategory, String PDeliveryDate, String PDeliveryTime, String PDeliveryDays, String PDescription, String PStatus, String PTitle, String ProjectID, String UID) {
        this.PBudget = PBudget;
        this.PCategory = PCategory;
        this.PDeliveryDays = PDeliveryDays;
        this.PDeliveryDate = PDeliveryDate;
        this.PDeliveryTime = PDeliveryTime;
        this.PDescription = PDescription;
        this.PStatus = PStatus;
        this.PTitle = PTitle;
        this.ProjectID = ProjectID;
        this.UID = UID;
    }

    public String getPBudget() {
        return PBudget;
    }

    public void setPBudget(String PBudget) {
        this.PBudget = PBudget;
    }

    public String getPCategory() {
        return PCategory;
    }

    public void setPCategory(String PCategory) {
        this.PCategory = PCategory;
    }

    public String getPDeliveryDays() {
        return PDeliveryDays;
    }

    public void setPDeliveryDays(String PDeliveryDays) {
        this.PDeliveryDays = PDeliveryDays;
    }

    public String getPDeliveryDate() {
        return PDeliveryDate;
    }
    public void setPDeliveryDate(String PDeliveryDate) {
        this.PDeliveryDate = PDeliveryDate;
    }

    public String getPDeliveryTime() {
        return PDeliveryTime;
    }
    public void setPDeliveryTime(String PDeliveryTime) {
        this.PDeliveryTime = PDeliveryTime;
    }

    public String getPDescription() {
        return PDescription;
    }

    public void setPDescription(String PDescription) {
        this.PDescription = PDescription;
    }

    public String getPStatus() {
        return PStatus;
    }

    public void setPStatus(String PStatus) {
        this.PStatus = PStatus;
    }

    public String getPTitle() {
        return PTitle;
    }

    public void setPTitle(String PTitle) {
        this.PTitle = PTitle;
    }

    public String getProjectID() {
        return ProjectID;
    }

    public void setProjectID(String projectID) {
        ProjectID = projectID;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }
}
