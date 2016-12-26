package com.dauphine.chat.data;

import java.util.List;

import com.dauphine.chat.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @author belgacea
 * @date 18/12/2016
 */
@Repository
public interface UserRepository extends MongoRepository<User, String>, PagingAndSortingRepository<User, String> {

    @Query(value="{ 'mail' : ?0 }")
    User findByMail(@Param("mail") String mail);

    @Query(value="{ 'username' : ?0 }")
    List<User> findByUsername(@Param("username") String username);

    @Query("{ 'mail' : ?0, 'password' : ?1 }")
    User findByMailAndPassword(final String mail, final String password);

}