package com.ars.controller;

import com.ars.dto.ActivityDto;
import com.ars.po.User;
import com.ars.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by guowanyi on 2019/3/10.
 */
@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private UserService userService;

    @Test
    public void should_return_user_when_create_user() throws Exception {
        User user = new User("ocean","password");
        when(userService.createUser(any(User.class))).thenReturn(user);

        ResultActions resultActions = this.mockMvc.perform(post("/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(user)));;

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.data.userName",is("ocean")))
                .andExpect(jsonPath("$.data.password",is("password")));
        verify(userService, times(1)).createUser(any());

    }


    @Test
    public void should_return_delete_user_when_given_user_existed_in_DB() throws Exception {
        User user = new User("ocean","password");
        when(userService.deleteUser(anyString())).thenReturn(user);

        ResultActions resultActions = this.mockMvc.perform(delete("/user/1"));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.data.userName",is("ocean")))
                .andExpect(jsonPath("$.data.password",is("password")));
        verify(userService, times(1)).deleteUser(any());

    }

    @Test
    public void should_delete_user_fail_when_given_user_not_existed_in_DB() throws Exception {
        when(userService.deleteUser(anyString())).thenReturn(null);

        ResultActions resultActions = this.mockMvc.perform(delete("/user/1"));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.message",is("delete user failed")));
        verify(userService, times(1)).deleteUser(any());

    }

    @Test
    public void should_get_user_participated_activity_when_given_user_name() throws Exception {
        ActivityDto activityDto = new ActivityDto();
        activityDto.setAuthor("ocean");
        activityDto.setTitle("title");
        activityDto.setContent("content");
        when(userService.getActivitiesByUserName(anyString())).thenReturn(Arrays.asList(activityDto));

        ResultActions resultActions = this.mockMvc.perform(get("/user/ocean/activity"));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].author",is("ocean")))
                .andExpect(jsonPath("$.data[0].title",is("title")))
                .andExpect(jsonPath("$.data[0].content",is("content")));

    }

    @Test
    public void should_can_not_find_user_when_given_not_existed_user_name() throws Exception {
        User user = new User("ocean","password");
        when(userService.getUserByUserName(anyString())).thenReturn(null);

        ResultActions resultActions = this.mockMvc.perform(get("/user/ocean")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(user)));;

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.message",is("can not find user")));

    }

    @Test
    public void should_return_can_not_login_password_error_when_given_user_name() throws Exception {
        User user = new User("ocean","password");

        when(userService.getUserByUserName(anyString())).thenReturn(user);

        ResultActions resultActions = this.mockMvc.perform(get("/user/ocean")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(user)));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.message",is("can not login, password error")));

    }

}
