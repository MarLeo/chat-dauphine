package com.dauphine.chat.service;

import com.dauphine.chat.domain.Message;
import com.dauphine.chat.domain.Room;

import java.util.List;
import java.util.Set;

/**
 * Created by marti on 14/01/2017.
 */
public interface MessageService {

    List<Message> findByMessage(final String message);

    List<Room> findRoomByMessage(final String message);


}
