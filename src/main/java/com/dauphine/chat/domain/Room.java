package com.dauphine.chat.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by marti on 08/01/2017.
 */

@Document(collection = "rooms")
public class Room implements Comparable<Room> {

    @Id
    private String name;
    private String user;
    private String date;

    public Room(){}

    @PersistenceConstructor
    public Room(final String name) {
        this.name = name;
    }

    public Room(String name, String user, String date) {
        this.name = name;
        this.user = user;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Room{" +
                "name='" + name + '\'' +
                ", user='" + user + '\'' +
                ", date='" + date + '\'' +
                '}';
    }

    @Override
    public int compareTo(Room o) {
        return name.compareTo(o.getName());
    }
}
