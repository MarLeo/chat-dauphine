package com.dauphine.chat.data;

import com.dauphine.chat.domain.Room;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

/**
 * Created by marti on 08/01/2017.
 */
public interface RoomRepository extends MongoRepository<Room, String>, PagingAndSortingRepository<Room, String> {

    @Query(value = "{ 'name' : ?0 }")
    Room findByName(@Param("name") final String name);
}
