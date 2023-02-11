package com.example.wedoapp;

public class Model_PortfolioDesign{
    private String ImageName, PortfolioID;
    private String ImageSample;

    public Model_PortfolioDesign() {
    }

    public Model_PortfolioDesign(String ImageName, String ImageSample) {
        this.ImageName = ImageName;
        this.PortfolioID = PortfolioID;
        this.ImageSample = ImageSample;
    }

    public String getImageName() {
        return ImageName;
    }

    public void setImageName(String imageName) {
        ImageName = imageName;
    }

    public String getImageSample() {
        return ImageSample;
    }

    public void setImageSample(String imageSample) {
        ImageSample = imageSample;
    }

    public String getPortfolioID() {
        return PortfolioID;
    }

    public void setPortfolioID(String portfolioID) {
        PortfolioID = portfolioID;
    }
}
