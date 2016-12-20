package com.dauphine.chat.data;

import com.dauphine.chat.domain.User;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.repository.query.MongoEntityInformation;
import org.springframework.data.mongodb.repository.support.SimpleMongoRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author belgacea
 * @date 18/12/2016
 */
public class UserRepositoryImplementation extends SimpleMongoRepository<User, String> implements UserRepository {

    public UserRepositoryImplementation(MongoEntityInformation<User, String> metadata, MongoOperations mongoOperations) {
        super(metadata, mongoOperations);
    }

    @Override
    public User findByMail(@Param("mail") String mail) {
        return null;
    }

    @Override
    public List<User> findByUsername(@Param("username") String username) {
        return null;
    }
}
