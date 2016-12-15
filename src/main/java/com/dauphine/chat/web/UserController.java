package com.dauphine.chat.web;

import com.dauphine.chat.domain.User;
import com.dauphine.chat.service.UserService;
//import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.DeleteResult;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by marti on 13/12/2016.
 */



@RestController
@RequestMapping(value = UserController.URI_VALUE)
public class UserController {

            final UserService userService;

            protected static final String URI_VALUE = "/";
            private static final String URI_LOGIN = "login";
            private static final String URI_REGISTER = "register";
            private static final String URI_UPDATE = "update";
            private static final String URI_DELETE = "delete";


            private static final Logger LOGGER = LogManager.getLogger(UserController.class);

            @Autowired
            public UserController(UserService userService) {
                this.userService = userService;
            }


            @RequestMapping(value = URI_REGISTER, method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
            public ResponseEntity<User> createUser(@RequestBody final User user) {
                LOGGER.log(Level.INFO, String.format("Creating a new user %s", user.toString()));
                if (userService.findUser(user).isEmpty()){
                    userService.create(user);
                    return new ResponseEntity<>(user, HttpStatus.CREATED);
                }
                LOGGER.log(Level.INFO, String.format("User %s already exist", user.toString()));
                return new ResponseEntity<>(HttpStatus.IM_USED);
            }


            @RequestMapping(value = URI_LOGIN, method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
            public ResponseEntity<Document> findUser(@RequestBody final User user) {
                LOGGER.log(Level.INFO, String.format("finding user with user %s", user.toString()));
                if (userService.findUser(user).isEmpty())
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                return new ResponseEntity<>(userService.findUser(user).get(0), HttpStatus.FOUND);
            }



            @RequestMapping(method = RequestMethod.GET, produces = "application/json")
            public ResponseEntity<List<Document>> findAllUsers() {
                LOGGER.log(Level.INFO, String.format("find all users"));
                if (userService.findAll().isEmpty())
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);
            }

            @RequestMapping(value = URI_UPDATE, method = RequestMethod.PUT, consumes = "application/json", produces = "application/json")
            public ResponseEntity<User> updateUser(@RequestBody final User user) {
                LOGGER.log(Level.INFO, String.format("Updating user  %s", user.toString()));
                userService.update(user);
                return new ResponseEntity<>(user, HttpStatus.OK);
            }


            @RequestMapping(value = URI_DELETE, method = RequestMethod.DELETE, consumes = "application/json", produces = "application/json")
            public ResponseEntity<DeleteResult> deleteUser(@RequestBody final User user) {
                LOGGER.log(Level.INFO, String.format("deleting user  %s", user.toString()));
                return new ResponseEntity<>(userService.deleteUser(user.getUsername()), HttpStatus.OK);
            }























}
