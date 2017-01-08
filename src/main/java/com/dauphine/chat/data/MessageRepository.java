package com.dauphine.chat.data;

import com.dauphine.chat.domain.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

/**
 * Created by marti on 06/01/2017.
 */
public interface MessageRepository extends MongoRepository<Message, String>, PagingAndSortingRepository<Message, String> {
    @Query(value = "{ 'room' : ?0 }")
    Page<Message> findByRoom(@Param("room") final String room, Sort sort, Pageable pageable);
}
