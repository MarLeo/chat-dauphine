package com.dauphine.chat.data;

import com.dauphine.chat.domain.Message;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by marti on 06/01/2017.
 */
public interface MessageRepository extends MongoRepository<Message, String>, PagingAndSortingRepository<Message, String> {
}
