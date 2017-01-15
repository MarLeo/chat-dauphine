package com.dauphine.chat.service;

import com.dauphine.chat.data.MessageRepository;
import com.dauphine.chat.domain.Message;
import com.dauphine.chat.domain.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by marti on 14/01/2017.
 */
@Service("MessageService")
public class MessageServiceImplementation implements MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Override
    public List<Message> findByMessage(final String message) {
        List<Message> messages = new LinkedList<>();
        for (Message m : messageRepository.findAll()) {
            if (m.getMessage().contains(message)) {
                messages.add(m);
            }
        }
        return messages;
    }

    //TODO replace contains
    @Override
    public Set<Room> findRoomByMessage(final String message) {
        Set<Room> rooms = new TreeSet<>();
        for (Message m : messageRepository.findAll()) {
            if (m.getMessage().contains(message)) {
                rooms.add(new Room(m.getRoom()));
            }
        }
        return rooms;
    }


}
