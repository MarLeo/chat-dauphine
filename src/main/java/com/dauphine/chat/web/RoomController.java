package com.dauphine.chat.web;

import com.dauphine.chat.data.RoomRepository;
import com.dauphine.chat.domain.Room;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by marti on 08/01/2017.
 */

@RestController
@RequestMapping(value = RoomController.URI_ROOT, produces = MediaType.APPLICATION_JSON_VALUE)
public class RoomController {

    protected static final String URI_ROOT = "/rooms";

    private static final Logger LOGGER = LogManager.getLogger(RoomController.class);
    @Autowired
    private RoomRepository roomRepository;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Room>> findRooms() {
        List<Room> rooms = roomRepository.findAll();
        LOGGER.log(Level.INFO, String.format("rooms in my colletion %s", rooms.toString()));
        return new ResponseEntity<>(rooms, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Room> createRoom(@RequestBody final Room room) {
        if (roomRepository.findByName(room.getName()) == null) {
            roomRepository.save(room);
            LOGGER.log(Level.INFO, String.format("created new room %s", room));
            return new ResponseEntity<>(room, HttpStatus.CREATED);
        }
        LOGGER.log(Level.INFO, String.format("room %s already exist", room.getName()));
        return new ResponseEntity<>(HttpStatus.IM_USED);
    }

    @RequestMapping(method = RequestMethod.DELETE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteRoom(@RequestBody final Room room) {
        LOGGER.log(Level.INFO, String.format("deleting room %s", room.toString()));
        roomRepository.delete(room);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
