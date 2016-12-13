package com.dauphine.chat.domain;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by marti on 10/12/2016.
 */
public class ChatMessage {

        private String message;
        private String username;
        private Calendar dateTime;
        private DateFormat datetimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        /* com.sun.xml.internal.bind.v2.TODO: refaire la date avec joda */

        public ChatMessage() {
            this.dateTime = Calendar.getInstance();
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(final String message) {
            this.message = message;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(final String username) {
            this.username = username;
        }

        public String getDateTime(){
            return datetimeFormat.format(dateTime.getTime());
        }

    @Override
    public String toString() {
        return "ChatMessage{" +
                "message='" + message + '\'' +
                ", username='" + username + '\'' +
                ", datetimeFormat=" + this.getDateTime() +
                '}';
    }
}
