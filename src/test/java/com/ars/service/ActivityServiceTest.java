package com.ars.service;


import com.ars.dto.AcitivitySearchCriteria;
import com.ars.dto.ActivityDto;
import com.ars.po.Activity;
import com.ars.po.User;
import com.ars.repository.ActivityRepository;
import com.ars.repository.UserRepository;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * @author Ocean Liang
 * @date 3/11/2019
 */
public class ActivityServiceTest {

    @Tested
    private ActivityService activityService;

    @Injectable
    private ActivityRepository activityRepository;

    @Injectable
    private UserRepository userRepository;

    @Test
    public void should_return_activity_when_given_activity_id_existed_in_DB() {
        Activity activity = new Activity("ocean", "welcome","content");

        new Expectations() {{
            activityRepository.findById(anyString);
            result = Optional.of(activity);
        }};

        Activity resultActivity = activityService.getActivityById("00303");

        assertThat(resultActivity.getAuthor()).isEqualTo("ocean");
        assertThat(resultActivity.getTitle()).isEqualTo("welcome");
    }

    @Test
    public void should_return_created_activity_when_createActivity() throws Exception {
        Activity activity = new Activity("ocean", "welcome","content");
        new Expectations(){{
            activityRepository.save(activity);
            result = activity;
        }};

        Activity resultActivity = activityService.createActivity(activity);

        assertThat(resultActivity.getAuthor()).isEqualTo("ocean");
        assertThat(resultActivity.getTitle()).isEqualTo("welcome");
    }

    @Test
    public void should_return_updated_activity_when_updateActivity() throws Exception {
        Activity activity = new Activity("ocean", "welcome","content");
        Activity newActivity = new Activity("ocean","description","content");
        new Expectations(){{
            activityRepository.findById(anyString);
            result = Optional.of(activity);
            activityRepository.save(activity);
            result = newActivity;
        }};

        boolean result = activityService.updateActivity("1", newActivity);

        assertThat(result).isTrue();
    }

    @Test
    public void should_return_false_when_updateActivity_given_not_exited_activity() throws Exception {
        Activity newActivity = new Activity("ocean","description","content");
        new Expectations(){{
            activityRepository.findById(anyString);
        }};

        boolean result = activityService.updateActivity("1", newActivity);

        assertThat(result).isFalse();
    }

    @Test
    public void should_deleted_success_repository_when_given_deletedActivity_id_existed() throws Exception {
        Activity activity = new Activity("ocean", "welcome","content");
        new Expectations(){{
            activityRepository.findById("1");
            result = Optional.of(activity);
            activityRepository.delete(activity);
        }};

        Activity deleteActivity = activityService.deleteActivity("1");

        assertThat(deleteActivity.getAuthor()).isEqualTo("ocean");
        assertThat(deleteActivity.getTitle()).isEqualTo("welcome");
    }

    @Test
    public void should_deleted_fail_repository_when_given_deletedActivity_id_not_existed() throws Exception {
        Activity activity = new Activity("ocean", "welcome","content");
        new Expectations(){{
            activityRepository.findById("1");
        }};

        Activity deleteActivity = activityService.deleteActivity("1");

        assertThat(deleteActivity).isNull();
    }

    @Test
    public void should_return_all_activities_name_are_search_name_when_only_given_search_name() throws Exception {
        AcitivitySearchCriteria searchCriteria = new AcitivitySearchCriteria();
        searchCriteria.setAuthor("ocean");
        Activity activity = new Activity("ocean", "welcome", "content");
        List<User> userList = getUserList("userName");
        activity.setParticipants(userList);
        new Expectations(){{
            activityRepository.findAllByAuthor(anyString);
            result = Arrays.asList(activity);
        }};

        List<ActivityDto> activityByCriteria = activityService.getActivityByCriteria(searchCriteria);

        assertThat(activityByCriteria.get(0).getAuthor()).isEqualTo("ocean");
        assertThat(activityByCriteria.get(0).getTitle()).isEqualTo("welcome");
        assertThat(activityByCriteria.get(0).getContent()).isEqualTo("content");
        assertThat(activityByCriteria.get(0).getParticipants().get(0).getUserName()).isEqualTo("userName");
    }

    @Test
    public void should_return_all_activities_name_and_title_as_search_name_and_title_when_given_search_name_and_title() throws Exception {
        AcitivitySearchCriteria searchCriteria = new AcitivitySearchCriteria();
        searchCriteria.setAuthor("ocean");
        searchCriteria.setTitle("welcome");
        Activity activity = new Activity("ocean", "welcome", "content");
        List<User> userList = getUserList("userName");
        activity.setParticipants(userList);
        new Expectations(){{
            activityRepository.findAllByTitleLikeAndAuthor(anyString,anyString);
            result = Arrays.asList(activity);
        }};

        List<ActivityDto> activityByCriteria = activityService.getActivityByCriteria(searchCriteria);

        assertThat(activityByCriteria.get(0).getAuthor()).isEqualTo("ocean");
        assertThat(activityByCriteria.get(0).getTitle()).isEqualTo("welcome");
        assertThat(activityByCriteria.get(0).getContent()).isEqualTo("content");
        assertThat(activityByCriteria.get(0).getParticipants().get(0).getUserName()).isEqualTo("userName");
    }

    @Test
    public void should_return_all_activities_title_as_search_title_when_only_given_search_title() throws Exception {
        AcitivitySearchCriteria searchCriteria = new AcitivitySearchCriteria();
        searchCriteria.setTitle("welcome");
        Activity activity = new Activity("ocean", "welcome", "content");
        List<User> userList = getUserList("userName");
        activity.setParticipants(userList);
        new Expectations(){{
            activityRepository.findAllByTitleLike(anyString);
            result = Arrays.asList(activity);
        }};

        List<ActivityDto> activityByCriteria = activityService.getActivityByCriteria(searchCriteria);

        assertThat(activityByCriteria.get(0).getAuthor()).isEqualTo("ocean");
        assertThat(activityByCriteria.get(0).getTitle()).isEqualTo("welcome");
        assertThat(activityByCriteria.get(0).getContent()).isEqualTo("content");
        assertThat(activityByCriteria.get(0).getParticipants().get(0).getUserName()).isEqualTo("userName");
    }

    @Test
    public void should_return_all_activities_when_not_given_search_title_and_name() throws Exception {
        AcitivitySearchCriteria searchCriteria = new AcitivitySearchCriteria();
        Activity activity = new Activity("ocean", "welcome", "content");
        List<User> userList = getUserList("userName");
        activity.setParticipants(userList);
        new Expectations(){{
            activityRepository.findAll();
            result = Arrays.asList(activity);
        }};

        List<ActivityDto> activityByCriteria = activityService.getActivityByCriteria(searchCriteria);

        assertThat(activityByCriteria.get(0).getAuthor()).isEqualTo("ocean");
        assertThat(activityByCriteria.get(0).getTitle()).isEqualTo("welcome");
        assertThat(activityByCriteria.get(0).getContent()).isEqualTo("content");
        assertThat(activityByCriteria.get(0).getParticipants().get(0).getUserName()).isEqualTo("userName");
    }

    @Test
    public void should_return_not_find_user_when_given_user_name_not_exited_in_DB() throws Exception {
        new Expectations(){{
         userRepository.findByUserName(anyString);
         result = null;
        }};

        String result = activityService.participateActivity("activity_id","invalidate user");

        assertThat(result).isEqualTo("can not find user");

    }

    @Test
    public void should_return_not_find_activity_when_given_activity_not_exited_in_DB() throws Exception {
        User user = new User("ocean","password");
        new Expectations(){{
            userRepository.findByUserName(anyString);
            result = user;
            activityRepository.findById(anyString);
        }};

        String result = activityService.participateActivity("invalidate activity id","ocean");

        assertThat(result).isEqualTo("can not find activity");

    }

    @Test
    public void should_set_participate_user_in_activity_when_given_user_and_activity_exited_in_DB_and_activity_have_no_user_participated() throws Exception {
        User user = new User("ocean","password");
        Activity activity = new Activity("ocean","title","content");
        new Expectations(){{
            userRepository.findByUserName(anyString);
            result = user;
            activityRepository.findById(anyString);
            result = Optional.of(activity);
            activityRepository.save(activity);
            result = activity;
        }};

        String result = activityService.participateActivity("activity id","ocean");

        assertThat(result).isEqualTo("");
        String participatedUser = activity.getParticipants().get(0).getUserName();
        assertThat(participatedUser).isEqualTo("ocean");
    }

    @Test
    public void should_return_has_already_participate_when_given_user_has_participated() throws Exception {
        User user = new User("ocean","password");
        Activity activity = new Activity("ocean","title","content");
        activity.setParticipants(Arrays.asList(user));
        new Expectations(){{
            userRepository.findByUserName(anyString);
            result = user;
            activityRepository.findById(anyString);
            result = Optional.of(activity);
        }};

        String result = activityService.participateActivity("activity id","ocean");

        assertThat(result).isEqualTo("has already participate");
        String participatedUser = activity.getParticipants().get(0).getUserName();
        assertThat(participatedUser).isEqualTo("ocean");
    }

//    @Test
//    public void should_add_user_participate_acitvity_when_given_user_not_has_participated() throws Exception {
//        User user = new User("ocean1","password");
//        Activity activity = new Activity("ocean","title","content");
//        activity.setParticipants(Arrays.asList(new User("ocean","password")));
//        new Expectations(){{
//            userRepository.findByUserName(anyString);
//            result = user;
//            activityRepository.findById(anyString);
//            result = Optional.of(activity);
//        }};
//
//        String result = activityService.participateActivity("activity id","ocean1");
//
//        assertThat(result).isEqualTo("");
//    }

    private List<User> getUserList(String userName){
        List<User> userDtoList = new ArrayList<>();
        User user = new User(userName,null);
        userDtoList.add(user);
        return userDtoList;
    }
}