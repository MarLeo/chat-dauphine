package com.dauphine.chat.data;

import com.dauphine.chat.domain.Room;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created by marti on 06/01/2017.
 */
public interface RoomRepository extends MongoRepository<Room, String>, PagingAndSortingRepository<Room, String> {
}
