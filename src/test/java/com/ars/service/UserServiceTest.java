package com.ars.service;

import com.ars.dto.ActivityDto;
import com.ars.po.Activity;
import com.ars.po.User;
import com.ars.repository.ActivityRepository;
import com.ars.repository.UserRepository;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Abby Guo
 * @date 3/11/2019
 */
public class UserServiceTest {

    @Tested
    private UserService userService;

    @Injectable
    private UserRepository userRepository;

    @Injectable
    private ActivityRepository activityRepository;

    @Test
    public void should_return_user_when_given_user_existed_in_DB() {
        User user = new User("ocean", "password");

        new Expectations() {{
            userRepository.findByUserName(anyString);
            result = user;
        }};

        User result = userService.getUserByUserName("ocean");

        assertThat(result.getUserName()).isEqualTo("ocean");
    }

    @Test
    public void should_return_created_user_when_createUser() throws Exception {
        User user = new User("ocean", "password");
        new Expectations(){{
            userRepository.save(user);
            result = user;
        }};

        User result = userService.createUser(user);

        assertThat(result.getUserName()).isEqualTo("ocean");
        assertThat(result.getPassword()).isEqualTo("password");
    }

    @Test
    public void should_deleted_success_when_given_deleted_user_id_existed() throws Exception {
        User user = new User("ocean", "password");
        new Expectations(){{
            userRepository.findById("1");
            result = Optional.of(user);
            userRepository.delete(user);
        }};

        User deleteUser = userService.deleteUser("1");

        assertThat(deleteUser.getUserName()).isEqualTo("ocean");
        assertThat(deleteUser.getPassword()).isEqualTo("password");
    }

    @Test
    public void should_deleted_fail_repository_when_given_deleted_uctivity_id_not_existed() throws Exception {
        new Expectations(){{
            userRepository.findById("1");
        }};

        User deleteUser = userService.deleteUser("1");

        assertThat(deleteUser).isNull();
    }

    @Test
    public void should_get_user_all_participated_activities_when_given_user_name() throws Exception {
        List<Activity> activities = Arrays.asList(new Activity("ocean1","title","content"));
        new Expectations(){{
            activityRepository.findAllByUserName("ocean");
            result = activities;
        }};

        List<ActivityDto> activityDtos = userService.getActivitiesByUserName("ocean");

        assertThat(activityDtos.get(0).getAuthor()).isEqualTo("ocean1");
        assertThat(activityDtos.get(0).getTitle()).isEqualTo("title");
        assertThat(activityDtos.get(0).getContent()).isEqualTo("content");
    }


}