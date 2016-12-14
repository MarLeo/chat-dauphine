package com.dauphine.chat.utils;

import com.dauphine.chat.domain.User;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.joda.time.LocalDate;

/**
 * Created by marti on 13/12/2016.
 */
public class Utils {

        public static Document createDocument(final User user){
            Document document = new Document("mail", user.getMail())
                                            .append("password", user.getPassword())
                                            .append("username", user.getUsername())
                                            .append("birthday", user.getBirthday())
                                            .append("gender", user.getGender())
                                            .append("phone", user.getPhone());
            return document;
        }

        public static Bson excludeId(){
            return new Document("_id", 0);
        }

        public static LocalDate birthday(final int day, final int month, final int year) {
            LocalDate b = new LocalDate();
            b.minusDays(day);
            b.minusMonths(month);
            b.minusYears(year);
            return b;
        }

}
