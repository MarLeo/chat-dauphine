package com.dauphine.chat.service;

import com.dauphine.chat.data.UserRepository;
import com.dauphine.chat.domain.User;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by marti on 13/12/2016.
 */
@Service("UserService")
public class UserServiceImplementation implements UserService {

    private final UserRepository userRepository;

    private static final Logger LOGGER = LogManager.getLogger(UserServiceImplementation.class);

    @Autowired
    public UserServiceImplementation(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void create(final User user) {
        //TODO encode password
        userRepository.save(user);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findByMail(final String mail) {
        return userRepository.findByMail(mail);
    }

    public User findByMailPassword(final String mail, final String password) {
        return userRepository.findByMailAndPassword(mail, password);
    }

    public List<User> findByUsername(final String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User findUser(final User user) {
        return userRepository.findByMail(user.getMail());
    }

    @Override
    public User update(User user) {
        //TODO encode password
        return userRepository.save(user);
    }

    public void disableUser(final String mail) {
        User user = userRepository.findByMail(mail);
        user.disable();
        userRepository.save(user);
    }

    public void enableUser(final String mail){
        User user = userRepository.findByMail(mail);
        user.enable();
        userRepository.save(user);
    }

    @Override
    public void deleteUser(final String mail) {
        userRepository.delete(mail);
    }
}
