package com.example.varadsp.talentschool_myproject;

/**
 * Created by VARAD on 14-04-2018.
 */

public class EventsClass {
    String eventName;
    String eventDate;
    String eventDescription;
    String eventImageUrl;
    String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public EventsClass(String eventName, String eventDescription, String eventImageUrl, String eventDate,String key) {
        this.eventName = eventName;
        this.eventDescription = eventDescription;
        this.eventImageUrl = eventImageUrl;
        this.eventDate = eventDate;
        this.key = key;
    }


    public EventsClass() {


    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public String getEventImageUrl() {
        return eventImageUrl;
    }

    public void setEventImageUrl(String eventImageUrl) {
        this.eventImageUrl = eventImageUrl;
    }
}
