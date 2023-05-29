package com.example.kursovaya.model;

public class Favourite {
    public String userId;
    public String placeId, userIud_PlaceID;
    public String category;

    public Favourite() {
    }

    public Favourite(String userId, String placeId, String category) {
        this.userId = userId;
        this.placeId = placeId;
        this.category = category;
        this.userIud_PlaceID = userId + "_" + placeId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getUserIud_PlaceID() {
        return userIud_PlaceID;
    }

    public void setUserIud_PlaceID(String userIud_PlaceID) {
        this.userIud_PlaceID = userIud_PlaceID;
    }
}

