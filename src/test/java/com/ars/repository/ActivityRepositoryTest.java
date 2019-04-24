package com.ars.repository;

import com.ars.po.Activity;
import com.ars.po.User;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@DataMongoTest
public class ActivityRepositoryTest {

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @After
    public void tearDown() {
        mongoTemplate.getDb().drop();
    }

    public ActivityRepositoryTest() {
    }

    @Test
    public void given_activities_whose_author_is_author1_when_findAllByAuthor_author2_then_get_empty() {
        // given
        Activity activity1 = new Activity("author1", "title1", "content1");
        Activity activity2 = new Activity("author1", "title2", "content2");
        mongoTemplate.save(activity1);
        mongoTemplate.save(activity2);

        // when
        List<Activity> activities = activityRepository.findAllByAuthor("author2");

        // then
        assertEquals(0, activities.size());
    }

    @Test
    public void given_activities_whose_author_is_author1_when_findAllByAuthor_author1_then_get_them() {
        // given
        Activity activity1 = new Activity("author1", "title1", "content1");
        Activity activity2 = new Activity("author1", "title2", "content2");
        mongoTemplate.save(activity1);
        mongoTemplate.save(activity2);

        // when
        List<Activity> activities = activityRepository.findAllByAuthor("author1");

        // then
        assertEquals(2, activities.size());
        assertEquals("title1", activities.get(0).getTitle());
        assertEquals("title2", activities.get(1).getTitle());
    }

    @Test
    public void given_activities_whose_title_like_title_when_findAllByTitleLike_eltit_then_get_empty() {
        // given
        Activity activity1 = new Activity("author1", "title1", "content1");
        Activity activity2 = new Activity("author2", "title2", "content2");
        mongoTemplate.save(activity1);
        mongoTemplate.save(activity2);

        //when
        List<Activity> activities = activityRepository.findAllByTitleLike("eltit");

        // then
        assertEquals(0, activities.size());
    }

    @Test
    public void given_activities_whose_title_like_title_when_findAllByTitleLike_title_then_get_them() {
        // given
        Activity activity1 = new Activity("author1", "title1", "content1");
        Activity activity2 = new Activity("author2", "title2", "content2");
        mongoTemplate.save(activity1);
        mongoTemplate.save(activity2);

        //when
        List<Activity> activities = activityRepository.findAllByTitleLike("title");

        // then
        assertEquals(2, activities.size());
        assertEquals("title1", activities.get(0).getTitle());
        assertEquals("title2", activities.get(1).getTitle());
    }

    @Test
    public void given_activities_whose_title_like_title_when_findAllByTitleLikeAndAuthor_title_author_then_get_empty() {
        // given
        Activity activity1 = new Activity("author1", "title1", "content1");
        Activity activity2 = new Activity("author2", "title2", "content2");
        mongoTemplate.save(activity1);
        mongoTemplate.save(activity2);

        //when
        List<Activity> activities = activityRepository.findAllByTitleLikeAndAuthor("title", "author");

        //then
        assertEquals(0, activities.size());
    }

    @Test
    public void given_activities_whose_title_like_title_when_findAllByTitleLikeAndAuthor_title_author2_then_get_activity_whose_title_is_title2() {
        // given
        Activity activity1 = new Activity("author1", "title1", "content1");
        Activity activity2 = new Activity("author2", "title2", "content2");
        Activity activity3 = new Activity("author2", "title3", "content3");
        mongoTemplate.save(activity1);
        mongoTemplate.save(activity2);
        mongoTemplate.save(activity3);

        //when
        List<Activity> activities = activityRepository.findAllByTitleLikeAndAuthor("title", "author2");

        //then
        assertEquals(2, activities.size());
        assertEquals("title2", activities.get(0).getTitle());
        assertEquals("title3", activities.get(1).getTitle());
    }

    @Test
    public void given_activities_whose_participants_is_user1_when_findAllByUserName_user2_then_get_empty() {
        //given
        Activity activity1 = new Activity("author1", "title1", "content1");
        Activity activity2 = new Activity("author2", "title2", "content2");
        User user1 = new User("user1", "123");
        activity1.setParticipants(Collections.singletonList(user1));
        activity2.setParticipants(Collections.singletonList(user1));
        mongoTemplate.save(activity1);
        mongoTemplate.save(activity2);

        //when
        List<Activity> activities = activityRepository.findAllByUserName("user2");

        //then
        assertEquals(0, activities.size());
    }

    @Test
    public void given_activities_whose_participants_is_user1_when_findAllByUserName_user1_then_get_them() {
        //given
        Activity activity1 = new Activity("author1", "title1", "content1");
        Activity activity2 = new Activity("author2", "title2", "content2");
        User user1 = new User("user1", "123");
        activity1.setParticipants(Collections.singletonList(user1));
        activity2.setParticipants(Collections.singletonList(user1));
        mongoTemplate.save(activity1);
        mongoTemplate.save(activity2);

        //when
        List<Activity> activities = activityRepository.findAllByUserName("user1");

        //then
        assertEquals(2, activities.size());
        assertEquals("title1", activities.get(0).getTitle());
        assertEquals("title2", activities.get(1).getTitle());
    }
}