package com.dauphine.chat.domain;


import org.joda.time.LocalDate;

/**
 * Created by marti on 13/12/2016.
 */
public class User {

         private String mail;
         private String username;
         private String password;
         private String birthday;
         private String gender;
         private String phone;

         public User() {

         }

        public User(final String mail, final String username, final String password, final String birthday, final String gender, final String phone) {
                this.mail = mail;
                this.username = username;
                this.password = password;
                this.birthday = birthday;
                this.gender = gender;
                this.phone = phone;

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

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        @Override
        public String toString() {
            return "User{" +
                    "mail='" + mail + '\'' +
                    ", username='" + username + '\'' +
                    ", password='" + password + '\'' +
                    ", birthday=" + birthday +
                    ", gender='" + gender + '\'' +
                    ", phone=" + phone +
                    '}';
        }
}
