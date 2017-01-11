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

    public Room(){}

    @PersistenceConstructor
    public Room(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Room{" +
                "name='" + name + '\'' +
                '}';
    }


    @Override
    public int compareTo(Room o) {
        return name.compareTo(o.getName());
    }
}
