package com.ars.controller;

import com.ars.dto.ResponseDto;
import com.ars.po.User;
import com.ars.service.ActivityService;
import com.ars.service.UserService;
import com.ars.utils.JMSConnecter;
import com.solacesystems.jcsmp.*;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.*;

/**
 * Created by guowanyi on 2019/3/12.
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Resource private UserService userService;
    @Resource private ActivityService activityService;

    @PostMapping
    public ResponseDto createUser(@RequestBody User user) {
        userService.createUser(user);
        return ResponseDto.success(user);
    }

    @GetMapping("/{userName}")
    public ResponseDto getUserByUserName(@PathVariable String userName) {
        User user = userService.getUserByUserName(userName);
        if (Objects.isNull(user)) {
            return ResponseDto.fail("can not find user");
        }
        return ResponseDto.fail("can not login, password error");
    }

    @DeleteMapping("/{userId}")
    public ResponseDto deleteUser(@PathVariable String userId) {
        User deletedUser = userService.deleteUser(userId);
        if (Objects.isNull(deletedUser)) {
            return ResponseDto.fail("delete user failed");
        }
        return ResponseDto.success(deletedUser);
    }

    @GetMapping("/{userName}/activity")
    public ResponseDto getUserParticipatedActivitiesByUserName(@PathVariable String userName) {
        return ResponseDto.success(userService.getActivitiesByUserName(userName));
    }

    @GetMapping("/activity/request")
    public void sendCheckActivityMsg() throws JCSMPException {
        final JCSMPSession session = JMSConnecter.getJcsmpSession();

        XMLMessageProducer producer = session.getMessageProducer(new JCSMPStreamingPublishEventHandler() {
            @Override
            public void handleError(String messageId, JCSMPException e, long timestamp) {
                System.out.printf("Producer received error for msg: %s@%s - %s%n", messageId, timestamp, e);
            }

            @Override
            public void responseReceived(String messageID) {
                System.out.println("Producer received response for msg: " + messageID);
            }
        });

        XMLMessageConsumer consumer = session.getMessageConsumer((XMLMessageListener)null);
        consumer.start();

        final Topic topic = JCSMPFactory.onlyInstance().createTopic("topic/activity");
        TextMessage msg = JCSMPFactory.onlyInstance().createMessage(TextMessage.class);
        String messageText = "审核Info";
        msg.setText(messageText);
        producer.send(msg, topic);
        //todo： update status as 审核中
        System.out.println("审核中");
        final int timeoutMs = 1000000;
        try {
            Requestor requestor = session.createRequestor();
            BytesXMLMessage reply = requestor.request(msg, timeoutMs, topic);

            // Process the reply
            if (reply instanceof TextMessage) {
                System.out.printf("TextMessage response received: '%s'%n",
                    ((TextMessage)reply).getText());
                //todo： update status as 审核结果
            }
            System.out.printf("Response Message Dump:%n%s%n",reply.dump());
        } catch (JCSMPRequestTimeoutException e) {
            System.out.println("Failed to receive a reply in " + timeoutMs + " msecs");
        }

    }


}
