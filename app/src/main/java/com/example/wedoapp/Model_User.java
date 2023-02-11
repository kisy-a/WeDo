package com.example.wedoapp;

public class Model_User {

    private String FullName, Location, Password, PhoneContact, ProfileImage, UID, EmailAddress, ConfirmPassword, OnlineStatus;

    public Model_User() {
    }

    public Model_User(String FullName, String Location, String Password, String PhoneContact, String ProfileImage, String UID, String EmailAddress, String ConfirmPassword , String OnlineStatus) {
        this.FullName = FullName;
        this.Location = Location;
        this.Password = Password;
        this.PhoneContact = PhoneContact;
        this.ProfileImage = ProfileImage;
        this.UID = UID;
        this.OnlineStatus = OnlineStatus;
        this.EmailAddress = EmailAddress;
        this.ConfirmPassword = ConfirmPassword;
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getPhoneContact() {
        return PhoneContact;
    }

    public void setPhoneContact(String phoneContact) {
        PhoneContact = phoneContact;
    }

    public String getProfileImage() {
        return ProfileImage;
    }

    public void setProfileImage(String profileImage) {
        ProfileImage = profileImage;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getEmailAddress() {
        return EmailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        EmailAddress = emailAddress;
    }

    public String getConfirmPassword() {
        return ConfirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        ConfirmPassword = confirmPassword;
    }

    public String getOnlineStatus() {
        return OnlineStatus;
    }

    public void setOnlineStatus(String onlineStatus) {
        OnlineStatus = onlineStatus;
    }
}
