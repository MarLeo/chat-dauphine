package com.dauphine.chat.web;

import com.dauphine.chat.domain.User;
import com.dauphine.chat.service.UserService;
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

        @Autowired UserService userService;

        protected static final String URI_VALUE = "/";
        private static final String URI_LOGIN = "login";
        private static final String URI_REGISTER = "register";
        private static final String URI_VALUE_USER = "{username}/{password}";


        private static final Logger LOGGER = LogManager.getLogger(UserController.class);


        @RequestMapping( value = URI_REGISTER, method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
        public ResponseEntity<User> createUser(@RequestBody User user) {
            LOGGER.log(Level.INFO, String.format("Creating a new user %s", user.toString()));
            if (userService.findUser(user.getUsername(), user.getPassword()).isEmpty()){
                userService.create(user);
                return new ResponseEntity<User>(user, HttpStatus.CREATED);
            }
            LOGGER.log(Level.INFO, String.format("User %s already exist", user.toString()));
            return new ResponseEntity<User>(HttpStatus.IM_USED);
        }

        @RequestMapping(value = URI_LOGIN, method = RequestMethod.GET, produces = "application/json")
        public ResponseEntity<Document> findUser(@PathVariable("username") final String username, @PathVariable("password") final String password) {
            LOGGER.log(Level.INFO, String.format("finding user with username: %s", username));
            if (userService.findUser(username, password).isEmpty())
                return new ResponseEntity<Document>(HttpStatus.NOT_FOUND);
            return new ResponseEntity<Document>(userService.findUser(username, password).get(0), HttpStatus.FOUND);
        }


        /*@RequestMapping(method = RequestMethod.GET, produces = "application/json")
        public ResponseEntity<List<Document>> findAllUsers() {
            LOGGER.log(Level.INFO, String.format("find all users"));
            if (userService.findAll().isEmpty())
                return new ResponseEntity<List<Document>>(HttpStatus.NO_CONTENT);
            return new ResponseEntity<List<Document>>(userService.findAll(), HttpStatus.OK);
        }


        @RequestMapping(value = URI_VALUE_USER, method = RequestMethod.PUT, consumes = "application/json", produces = "application/json")
        public ResponseEntity<User> updateUser(@PathVariable("username") final String username, @RequestBody final User user) {
            LOGGER.log(Level.INFO, String.format("Updating user with username %s", username));
            userService.update(user);
            return new ResponseEntity<User>(user, HttpStatus.OK);
        }

        @RequestMapping(value = URI_VALUE_USER, method = RequestMethod.DELETE, consumes = "application/json", produces = "application/json")
        public ResponseEntity<DeleteResult> deleteUser(@PathVariable("username") final String username) {
            LOGGER.log(Level.INFO, String.format("deleting user with username %s", username));
            return new ResponseEntity<DeleteResult>(userService.deleteUser(username), HttpStatus.NO_CONTENT);
        }*/























}
