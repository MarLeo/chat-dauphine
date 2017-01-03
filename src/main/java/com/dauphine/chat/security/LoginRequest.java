package com.dauphine.chat.security;

/**
 * @author belgacea
 * @date 31/12/2016
 */
public class LoginRequest {
    public String mail;
    public String password;

    public String getMail() {
        return mail;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "LoginRequest{" +
                "mail='" + mail + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
