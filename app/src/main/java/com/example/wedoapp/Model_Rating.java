package com.example.wedoapp;

public class Model_Rating {

    //Same variable name with firebase
    private String BuyerID, BuyerImage, BuyerName, OComment, ORating, OTitle, OrderID, RatingID, ServiceID;

    public Model_Rating() {

    }

    public Model_Rating(String BuyerID, String BuyerImage, String BuyerName, String OComment, String ORating, String OTitle, String OrderID, String RatingID, String ServiceID) {
        this.BuyerID = BuyerID;
        this.BuyerImage = BuyerImage;
        this.BuyerName = BuyerName;
        this.OComment = OComment;
        this.ORating = ORating;
        this.OTitle = OTitle;
        this.OrderID = OrderID;
        this.RatingID = RatingID;
        this.ServiceID = ServiceID;
    }

    public String getBuyerID() {
        return BuyerID;
    }

    public void setBuyerID(String buyerID) {
        BuyerID = buyerID;
    }

    public String getBuyerImage() {
        return BuyerImage;
    }

    public void setBuyerImage(String buyerImage) {
        BuyerImage = buyerImage;
    }

    public String getBuyerName() {
        return BuyerName;
    }

    public void setBuyerName(String buyerName) {
        BuyerName = buyerName;
    }

    public String getOComment() {
        return CensorWords.censor(OComment);
    }

    public void setOComment(String OComment) {
        this.OComment = OComment;
    }

    public String getORating() {
        return ORating;
    }

    public void setORating(String ORating) {
        this.ORating = ORating;
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

    public String getRatingID() {
        return RatingID;
    }

    public void setRatingID(String ratingID) {
        RatingID = ratingID;
    }

    public String getServiceID() {
        return ServiceID;
    }

    public void setServiceID(String serviceID) {
        ServiceID = serviceID;
    }
}
