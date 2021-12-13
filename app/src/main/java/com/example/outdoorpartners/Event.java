package com.example.outdoorpartners;

public class Event {
    // declare variables
    int event_id;
    String name;
    int image;
    int day;
    int month;
    int year;
    int hour;
    int min;
    String type;
    String description;
    String location;

    public Event(String description, int id, int image, String location, String name, int year, int month, int day, int hour, int min, String type){
        this.event_id = id;
        this.name = name;
        this.image = image;
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.min = min;
        this.type = type;
        this.description = description;
        this.location = location;
    }
    public Event(){

    }

    public int getEvent_id() {
        return event_id;
    }


    public String getLocation() {
        return location;
    }


    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public int getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public void setEvent_id(int event_id) {
        this.event_id = event_id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }
}
