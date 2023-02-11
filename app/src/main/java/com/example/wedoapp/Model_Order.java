package com.example.wedoapp;

public class Model_Order {

    private String AcceptedTime, CompletedTime, BuyerID, BuyerName, ServiceImage, OCategory, ODeliveryDays, OImage, OPaymentMethod, OPrice, OStatus, OTitle, OrderID, SellerID, SellerName, ServiceID, OrderDate, OrderTime,
            SellerEmail, SellerLocation, BuyerEmail, BuyerLocation;


    public Model_Order() {
    }

    public Model_Order(String ServiceImage, String BuyerID, String OCategory, String OPaymentMethod, String ServiceID, String BuyerName, String ODeliveryDays,
                       String OImage, String OPrice, String OStatus, String OTitle, String OrderID, String SellerID, String SellerName, String OrderDate,
                       String OrderTime, String AcceptedTime, String CompletedTime, String SellerEmail, String SellerLocation, String BuyerEmail, String BuyerLocation) {
        this.BuyerID = BuyerID;
        this.ServiceImage = ServiceImage;
        this.BuyerName = BuyerName;
        this.ODeliveryDays = ODeliveryDays;
        this.OImage = OImage;
        this.OPrice = OPrice;
        this.OStatus = OStatus;
        this.OTitle = OTitle;
        this.OrderID = OrderID;
        this.SellerID = SellerID;
        this.SellerName = SellerName;
        this.ServiceID = ServiceID;
        this.OCategory = OCategory;
        this.OPaymentMethod = OPaymentMethod;
        this.AcceptedTime = AcceptedTime;
        this.CompletedTime = CompletedTime;
        this.OrderDate = OrderDate;
        this.OrderTime = OrderTime;
        this.SellerEmail = SellerEmail;
        this.SellerLocation = SellerLocation;
        this.BuyerEmail = BuyerEmail;
        this.BuyerLocation = BuyerLocation;
    }
    public String getSellerEmail() {
        return SellerEmail;
    }

    public void setSellerEmail(String sellerEmail) {
        SellerEmail = sellerEmail;
    }

    public String getSellerLocation() {
        return SellerLocation;
    }

    public void setSellerLocation(String sellerLocation) {
        SellerLocation = sellerLocation;
    }

    public String getBuyerEmail() {
        return BuyerEmail;
    }

    public void setBuyerEmail(String buyerEmail) {
        BuyerEmail = buyerEmail;
    }

    public String getBuyerLocation() {
        return BuyerLocation;
    }

    public void setBuyerLocation(String buyerLocation) {
        BuyerLocation = buyerLocation;
    }

    public String getAcceptedTime() {
        return AcceptedTime;
    }

    public void setCompletedTime(String completedTime) {
        CompletedTime = completedTime;
    }

    public String getCompletedTime() {
        return CompletedTime;
    }

    public void setAcceptedTime(String acceptedTime) {
        AcceptedTime = acceptedTime;
    }

    public String getServiceImage() {
        return ServiceImage;
    }

    public void setServiceImage(String serviceImage) {
        ServiceImage = serviceImage;
    }

    public String getOCategory() {
        return OCategory;
    }

    public void setOCategory(String OCategory) {
        this.OCategory = OCategory;
    }

    public String getOPaymentMethod() {
        return OPaymentMethod;
    }

    public void setOPaymentMethod(String OPaymentMethod) {
        this.OPaymentMethod = OPaymentMethod;
    }

    public String getServiceID() {
        return ServiceID;
    }

    public void setServiceID(String serviceID) {
        ServiceID = serviceID;
    }

    public String getBuyerID() {
        return BuyerID;
    }

    public void setBuyerID(String buyerID) {
        BuyerID = buyerID;
    }

    public String getBuyerName() {
        return BuyerName;
    }

    public void setBuyerName(String buyerName) {
        BuyerName = buyerName;
    }

    public String getODeliveryDays() {
        return ODeliveryDays;
    }

    public void setODeliveryDays(String ODeliveryDays) {
        this.ODeliveryDays = ODeliveryDays;
    }

    public String getOImage() {
        return OImage;
    }

    public void setOImage(String OImage) {
        this.OImage = OImage;
    }

    public String getOPrice() {
        return OPrice;
    }

    public void setOPrice(String OPrice) {
        this.OPrice = OPrice;
    }

    public String getOStatus() {
        return OStatus;
    }

    public void setOStatus(String OStatus) {
        this.OStatus = OStatus;
    }

    public String getOTitle() {
        return OTitle;
    }

    public void setOTitle(String OTitle) {
        this.OTitle = OTitle;
    }

    public String getOrderID() {
        return OrderID;
    }

    public void setOrderID(String orderID) {
        OrderID = orderID;
    }

    public String getSellerID() {
        return SellerID;
    }

    public void setSellerID(String sellerID) {
        SellerID = sellerID;
    }

    public String getSellerName() {
        return SellerName;
    }

    public void setSellerName(String sellerName) {
        SellerName = sellerName;
    }

    public String getOrderDate() {
        return OrderDate;
    }

    public void setOrderDate(String orderDate) {
        OrderDate = orderDate;
    }

    public String getOrderTime() {
        return OrderTime;
    }

    public void setOrderTime(String orderTime) {
        OrderTime = orderTime;
    }
}