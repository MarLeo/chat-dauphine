package com.dauphine.chat.domain;

/**
 * Created by marti on 13/12/2016.
 */
public class User {

        private String mail;
        private String username;
        private String password;

        public User(String mail, String username, String password) {
                this.mail = mail;
                this.username = username;
                this.password = password;
        }

        public String getMail() {
                return mail;
        }

        public void setMail(String mail) {
                this.mail = mail;
        }

        public String getUsername() {
                return username;
        }

        public void setUsername(String username) {
                this.username = username;
        }

        public String getPassword() {
                return password;
        }

        public void setPassword(String password) {
                this.password = password;
        }

        @Override
        public String toString() {
                return "User{" +
                        "mail='" + mail + '\'' +
                        ", username='" + username + '\'' +
                        ", password='" + password + '\'' +
                        '}';
        }
}
