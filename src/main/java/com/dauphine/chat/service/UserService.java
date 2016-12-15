package com.dauphine.chat.service;

import com.dauphine.chat.domain.User;
import com.mongodb.client.result.DeleteResult;
import org.bson.Document;

import java.util.List;

/**
 * Created by marti on 13/12/2016.
 */
public interface UserService {

       public void create(final User user);

       public List<Document> findAll();

       public List<Document> findUser(final User user);

       public Document update(final User user);

       public DeleteResult deleteUser(final String username);

















}
