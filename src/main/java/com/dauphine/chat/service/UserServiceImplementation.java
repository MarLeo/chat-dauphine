package com.dauphine.chat.service;

import com.dauphine.chat.domain.User;
import com.dauphine.chat.utils.Utils;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.DeleteResult;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by marti on 13/12/2016.
 */
@Service("UserService")
public class UserServiceImplementation implements UserService {

        @Autowired private MongoCollection<Document> mongoCollection;

        @Override
        public void create(final User user) {
            Document document = Utils.createDocument(user);
            mongoCollection.insertOne(document);
        }

        @Override
        public List<Document> findAll() {
            return mongoCollection.find().projection(Utils.excludeId()).into(new ArrayList<Document>());
        }

        @Override
        public List<Document> findUser(final User user) {
            return mongoCollection.find(new Document("username", user.getUsername()).append("password", user.getPassword())).projection(Utils.excludeId()).into(new ArrayList<Document>());
        }

        @Override
        public Document update(User user) {
            return mongoCollection.findOneAndReplace(new Document("username", user.getUsername()), Utils.createDocument(user));
        }

        @Override
        public DeleteResult deleteUser(final String username) {
            return mongoCollection.deleteOne(new Document("username", username));
        }
}
