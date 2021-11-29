package com.example.outdoorpartners;

public class Event {
    // declare variables
    int event_id;
    String name;
    int image;
    String date;
    String time;
    String type;
    String description;
    String location;

    public Event(int id, String name, int image, String date, String time, String type, String description, String location){
        this.event_id = id;
        this.name = name;
        this.image = image;
        this.date = date;
        this.time = time;
        this.type = type;
        this.description = description;
        this.location = location;
    }

    public int getEvent_id() {
        return event_id;
    }

    public String getDate() {
        return date;
    }

    public String getLocation() {
        return location;
    }

    public String getTime() {
        return time;
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

    public void setDate(String date) {
        this.date = date;
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

    public void setTime(String time) {
        this.time = time;
    }

    public void setType(String type) {
        this.type = type;
    }
}
