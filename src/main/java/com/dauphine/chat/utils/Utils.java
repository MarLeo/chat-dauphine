package com.dauphine.chat.utils;

import com.dauphine.chat.domain.User;
import org.bson.Document;
import org.bson.conversions.Bson;

/**
 * Created by marti on 13/12/2016.
 */
public class Utils {

        public static Document createDocument(User user){
            Document document = new Document("mail", user.getMail())
                                            .append("username", user.getUsername())
                                            .append("password", user.getPassword());
            return document;
        }

        public static Bson excludeId(){
            return new Document("_id", 0);
        }
}
