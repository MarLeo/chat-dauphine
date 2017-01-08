package com.dauphine.chat.domain;

import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by marti on 08/01/2017.
 */

@Document(collection = "rooms")
public class Room {

    private String room;

    @PersistenceConstructor
    public Room(final String room) {
        this.room = room;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }
}
