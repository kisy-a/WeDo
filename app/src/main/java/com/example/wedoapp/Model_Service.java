package com.example.wedoapp;

public class Model_Service {

    private String UID, SID, SImage, SCategory, SDeliveryDays, SDescription, SPrice, STitle;

    public Model_Service() {
    }

    public Model_Service(String SImage, String UID, String SID, String SCategory, String SDeliveryDays, String SDescription, String SPrice, String STitle) {
        this.SImage = SImage;
        this.UID = UID;
        this.SID = SID;
        this.SCategory = SCategory;
        this.SDeliveryDays = SDeliveryDays;
        this.SDescription = SDescription;
        this.SPrice = SPrice;
        this.STitle = STitle;
    }

    public String getSImage() {
        return SImage;
    }

    public void setSImage(String SImage) {
        this.SImage = SImage;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getSID() {
        return SID;
    }

    public void setSID(String SID) {
        this.SID = SID;
    }

    public String getSCategory() {
        return SCategory;
    }

    public void setSCategory(String SCategory) {
        this.SCategory = SCategory;
    }

    public String getSDeliveryDays() {
        return SDeliveryDays;
    }

    public void setSDeliveryDays(String SDeliveryDays) {
        this.SDeliveryDays = SDeliveryDays;
    }

    public String getSDescription() {
        return CensorWords.censor(SDescription);
    }

    public void setSDescription(String SDescription) {
        this.SDescription = SDescription;
    }

    public String getSPrice() {
        return SPrice;
    }

    public void setSPrice(String SPrice) {
        this.SPrice = SPrice;
    }

    public String getSTitle() {
        return CensorWords.censor(STitle);
    }

    public void setSTitle(String STitle) {
        this.STitle = STitle;
    }
}
