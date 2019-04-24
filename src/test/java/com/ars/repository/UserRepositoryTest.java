package com.ars.repository;

import com.ars.po.User;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(SpringRunner.class)
@DataMongoTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @After
    public void tearDown() {
        mongoTemplate.getDb().drop();
    }

    @Test
    public void given_user_whose_name_is_user1_when_findByUserName_user_then_get_null() {
        //given
        User user = new User("user1", "123");
        mongoTemplate.save(user);

        //when
        User gotUser = userRepository.findByUserName("user");

        //then
        assertNull(gotUser);
    }

    @Test
    public void given_user_whose_name_is_user1_when_findByUserName_user1_then_get_it() {
        //given
        User user = new User("user1", "123");
        mongoTemplate.save(user);

        //when
        User gotUser = userRepository.findByUserName("user1");

        //then
        assertEquals("123", gotUser.getPassword());
    }
}