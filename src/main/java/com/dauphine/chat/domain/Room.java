package com.dauphine.chat.domain;

import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by marti on 06/01/2017.
 */
@Document(collection = "messages")
public class Room {

    private String room;
    private String user;
    private String message;
    private String date;
    //private DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("d MMMM, yyyy, HH:mm:ss");

    @PersistenceConstructor
    public Room(String room, String user, String message) {
        this.room = room;
        this.user = user;
        this.message = message;
        this.date = LocalDateTime.now().toString(DateTimeFormat.forPattern("d MMMM, yyyy, HH:mm:ss"));
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Room{" +
                "room='" + room + '\'' +
                ", user='" + user + '\'' +
                ", message='" + message + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
