package com.example.dogcupid.Models;

public class Dog {
    public static final int MAX_LINES_COLLAPSED = 4;
    public static final int MIN_LINES_COLLAPSED = 1;

    private String dogName;
    private int dogAge;
    private String dogBreed;
    private String gender;
    private String description;
    private double latitude;
    private double longitude;
    private boolean isCollapsed = true;
    private String imageUrl;
    private String ownerName;
    private String ownerPhone;

    public Dog() {
    }

    public String getDogName() {
        return dogName;
    }

    public Dog setDogName(String dogName) {
        this.dogName = dogName;
        return this;
    }

    public int getDogAge() {
        return dogAge;
    }

    public Dog setDogAge(int dogAge) {
        this.dogAge = dogAge;
        return this;
    }

    public String getDogBreed() {
        return dogBreed;
    }

    public Dog setDogBreed(String dogBreed) {
        this.dogBreed = dogBreed;
        return this;
    }

    public String getGender() {
        return gender;
    }

    public Dog setGender(String gender) {
        this.gender = gender;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Dog setDescription(String description) {
        this.description = description;
        return this;
    }

    public double getLatitude() {
        return latitude;
    }

    public Dog setLatitude(double latitude) {
        this.latitude = latitude;
        return this;
    }

    public double getLongitude() {
        return longitude;
    }

    public Dog setLongitude(double longitude) {
        this.longitude = longitude;
        return this;
    }

    public boolean isCollapsed() {
        return isCollapsed;
    }

    public Dog setCollapsed(boolean collapsed) {
        isCollapsed = collapsed;
        return this;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Dog setImageUri(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public Dog setOwnerName(String ownerName) {
        this.ownerName = ownerName;
        return this;
    }

    public String getOwnerPhone() {
        return ownerPhone;
    }

    public Dog setOwnerPhone(String ownerPhone) {
        this.ownerPhone = ownerPhone;
        return this;
    }

    @Override
    public String toString() {
        return "Dog{" +
                "dogName='" + dogName + '\'' +
                ", age=" + dogAge +
                ", breed='" + dogBreed + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}

