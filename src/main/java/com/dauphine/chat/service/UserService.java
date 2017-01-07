package com.dauphine.chat.service;

import com.dauphine.chat.domain.User;

import java.util.List;

/**
 * Created by marti on 13/12/2016.
 */
public interface UserService {

       void create(final User user);

       List<User> findAll();

       User findByMail(final String mail);

       User findByMailPassword(final String mail, final String password);

       User findUser(final User user);

       User update(final User user);

       void disableUser(final String mail);

       void enableUser(final String mail);

       void deleteUser(final String username);
}
