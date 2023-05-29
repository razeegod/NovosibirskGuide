package com.example.kursovaya.model;

import java.io.Serializable;

public class Places implements Serializable
{
    boolean isFavourite;
    String Name, Description, URL, Number, Schedule, Coordinates, Image, PlaceID;

    public Places()
    {

    }

    public Places(String name, String description, String url, String number, String schedule, String coordinates, String image, String placeID, boolean isFavourite) {
        Name = name;
        Description = description;
        URL = url;
        Number = number;
        Schedule = schedule;
        Coordinates = coordinates;
        Image = image;
        PlaceID = placeID;
        this.isFavourite = isFavourite;
    }

    public boolean isFavourite() {
        return isFavourite;
    }

    public void setFavourite(boolean favourite) {
        isFavourite = favourite;
    }

    public String getPlaceID() {
        return PlaceID;
    }

    public void setPlaceID(String placeID) {
        PlaceID = placeID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String url) {
        URL = url;
    }

    public String getNumber() {
        return Number;
    }

    public void setNumber(String number) {
        Number = number;
    }

    public String getSchedule() {
        return Schedule;
    }

    public void setSchedule(String schedule) {
        Schedule = schedule;
    }

    public String getCoordinates() {
        return Coordinates;
    }

    public void setCoordinates(String coordinates) {
        Coordinates = coordinates;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }
}
