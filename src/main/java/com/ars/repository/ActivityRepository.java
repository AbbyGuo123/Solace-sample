package com.ars.repository;

import com.ars.po.Activity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Ocean Liang
 * @date 3/8/2019
 */
@Repository
public interface ActivityRepository extends MongoRepository<Activity, String> {
    List<Activity> findAllByAuthor(String author);

    List<Activity> findAllByTitleLike(String title);

    List<Activity> findAllByTitleLikeAndAuthor(String title, String author);

    @Query(value = "{'participants.userName': ?0 }")
    List<Activity> findAllByUserName(String userName);
}
