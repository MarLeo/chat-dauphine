package com.dauphine.chat.domain;


import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Created by marti on 13/12/2016.
 */
public class User {

        private String mail;
        private String username;
        private String password;
        private LocalDate birthday;
        private String gender;
        private Integer phone;

        private DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("d/MM/YYYY");

        public User(final String mail, final String username, final String password, final LocalDate birthday, final String gender, final Integer phone) {
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

        public void setMail(final String mail) {
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

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public Integer getPhone() {
            return phone;
        }

        public void setPhone(Integer phone) {
            this.phone = phone;
        }

        public String getBirthday() {
            return birthday.toString(dateTimeFormatter);
        }


    @Override
    public String toString() {
        return "User{" +
                "mail='" + mail + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", birthday=" + this.getBirthday() +
                ", gender='" + gender + '\'' +
                ", phone=" + phone +
                '}';
    }
}
