package com.dauphine.chat.web;

import com.dauphine.chat.data.MessageRepository;
import com.dauphine.chat.domain.Message;
import com.dauphine.chat.domain.Room;
import com.dauphine.chat.service.MessageService;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

/**
 * Created by marti on 08/01/2017.
 */
@RestController
@RequestMapping(value = MessageController.URI_ROOT, produces = MediaType.APPLICATION_JSON_VALUE)
public class MessageController {


    protected static final String URI_ROOT = "/messages/{room}";
    //protected static final String URI_ROOT = "/user/{message}"; for my own test


    private static final Logger LOGGER = LogManager.getLogger(MessageController.class);

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private MessageService messageService;


    /*@RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Message>> findMessages(@PathVariable("room") final String room) {
        List<Message> messages = messageRepository.findByRoom(room, sortByDateDesc());
        LOGGER.log(Level.INFO, String.format("get % room all messages", room));
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }
    */

    /*@RequestMapping(method = RequestMethod.GET)
     public ResponseEntity<List<Message>> findRoomByMessage(@PathVariable("message") final String message) {
         LOGGER.log(Level.INFO, String.format("rooms where they are discuting about %s are %s", message, messageService.findByMessage(message).toString()));
         return new ResponseEntity<>(messageService.findByMessage(message), HttpStatus.OK);
     }

 */
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Set<Room>> findRoomByMessage(@PathVariable("message") final String message) {
        LOGGER.log(Level.INFO, String.format("rooms where they are discuting about %s are %s", message, messageService.findRoomByMessage(message).toString()));
        return new ResponseEntity<>(messageService.findRoomByMessage(message), HttpStatus.OK);
    }


    @RequestMapping(value = "/{page}", method = RequestMethod.GET)
    public ResponseEntity<Page<Message>> findMessagesByPage(@PathVariable("room") final String room, @PathVariable("page") final String page) {
        Integer pageNum = Integer.parseInt(page);
        Page<Message> messages = messageRepository.findByRoom(room, pageBy(pageNum, 5));
        LOGGER.log(Level.INFO, String.format("get %s room messages page number %s", room, page));
        return new ResponseEntity<>(messages, HttpStatus.PARTIAL_CONTENT);
    }


    private Sort sortByDateDesc() {
        return new Sort(Sort.Direction.DESC, "date");
    }

    private Pageable pageBy(Integer nPage, Integer nMessage) {
        return new PageRequest(nPage, nMessage, Sort.Direction.DESC, "date");
    }
}
