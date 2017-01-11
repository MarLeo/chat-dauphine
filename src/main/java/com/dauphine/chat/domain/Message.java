package com.dauphine.chat.domain;

import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by marti on 06/01/2017.
 */
@Document(collection = "messages")
public class Message {

    private String room;
    private String sender;
    private String message;
    private String date;
    //private DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("d MMMM, yyyy, HH:mm:ss");

    @PersistenceConstructor
    public Message(String room, String sender, String message) {
        this.room = room;
        this.sender = sender;
        this.message = message;
        this.date = LocalDateTime.now().toString(DateTimeFormat.forPattern("d MMMM, yyyy, HH:mm:ss"));
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
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
        return "Message{" +
                "room='" + room + '\'' +
                ", sender='" + sender + '\'' +
                ", message='" + message + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
