package com.dauphine.chat.service;

import com.dauphine.chat.data.MessageRepository;
import com.dauphine.chat.domain.Message;
import com.dauphine.chat.domain.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by marti on 14/01/2017.
 */
@Service("MessageService")
public class MessageServiceImplementation implements MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Override
    public List<Message> findByMessage(final String message) {
        List<Message> messages = messageRepository.findAll().stream().filter(m -> m.getMessage().contains(message)).collect(Collectors.toCollection(LinkedList::new));
        return messages;
    }

    //TODO replace contains
    @Override
    public List<Room> findRoomByMessage(final String message) {
        List<Room> rooms = messageRepository.findAll().stream().filter(m -> m.getMessage().contains(message)).map(m -> new Room(m.getRoom())).collect(Collectors.toCollection(LinkedList::new));
        return rooms;
    }

}
