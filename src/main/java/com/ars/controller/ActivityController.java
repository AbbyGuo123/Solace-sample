package com.ars.controller;

import com.alibaba.fastjson.JSONObject;
import com.ars.dto.AcitivitySearchCriteria;
import com.ars.dto.ResponseDto;
import com.ars.po.Activity;
import com.ars.service.ActivityService;
import com.ars.utils.JMSConnecter;
import com.solacesystems.jcsmp.*;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.*;

import org.slf4j.Logger;

/**
 * @author Ocean Liang
 * @date 3/8/2019
 */
@Slf4j
@RestController
@RequestMapping("/activity")
public class ActivityController {
    @Resource
    private ActivityService activityService;

    @Autowired
    private Registration registration;

    @Autowired
    private DiscoveryClient client;

    private final static Logger logger = LoggerFactory.getLogger(ActivityController.class);

    @PostMapping
    public ResponseDto createActivity(@RequestBody Activity activity) {
        if (Objects.isNull(activity.getAuthor()) || Objects.isNull(activity.getTitle())
                || Objects.isNull(activity.getContent())) {
            return ResponseDto.fail("author,title,content should not be empty");
        }
        activityService.createActivity(activity);
        return ResponseDto.success(activity);
    }

    @GetMapping("/{activityId}")
    public ResponseDto getActivityById(@PathVariable String activityId) {
        Activity activity = activityService.getActivityById(activityId);
        if (Objects.isNull(activity)) {
            return ResponseDto.fail("can not find activity");
        }
        return ResponseDto.success(activity);
    }

    @GetMapping
    public ResponseDto getActivityByCriteria(
            @RequestParam(name = "title", required = false) String title,
            @RequestParam(name = "author", required = false) String author) {
        AcitivitySearchCriteria searchCriteria = new AcitivitySearchCriteria();
        searchCriteria.setTitle(title);
        searchCriteria.setAuthor(author);
        return ResponseDto.success(activityService.getActivityByCriteria(searchCriteria));
    }

    @PutMapping("/{activityId}")
    public ResponseDto updateActivity(@PathVariable String activityId, @RequestBody Activity newActivity) {
        if (activityService.updateActivity(activityId, newActivity)) {
            return ResponseDto.success();
        }
        return ResponseDto.fail("update activity failed");
    }

    @DeleteMapping("/{activityId}")
    public ResponseDto deleteActivity(@PathVariable String activityId) {
        Activity deletedActivity = activityService.deleteActivity(activityId);
        if (Objects.isNull(deletedActivity)) {
            return ResponseDto.fail("delete activity failed");
        }
        return ResponseDto.success(deletedActivity);
    }

    @PatchMapping("/{activityId}")
    public ResponseDto participateActivity(@PathVariable String activityId, @RequestBody JSONObject jsonObject) {
        String username = (String) jsonObject.get("userName");
        if (Objects.isNull(username)) {
            return ResponseDto.fail("userName should not be empty");
        }
        String result = activityService.participateActivity(activityId, username);
        if (StringUtils.isEmpty(result)) {
            return ResponseDto.success();
        }
        return ResponseDto.fail(result);
    }

    @GetMapping("/user/receiver")
    public ResponseDto receivedMessage() throws JCSMPException {
        final JCSMPSession session = JMSConnecter.getJcsmpSession();
        final Topic topic = JCSMPFactory.onlyInstance().createTopic("topic/activity");
        final String[] message = new String[1];

        session.connect();

        final CountDownLatch latch = new CountDownLatch(1);

        final XMLMessageProducer producer = session.getMessageProducer(new JCSMPStreamingPublishEventHandler() {
            @Override
            public void responseReceived(String messageID) {
                System.out.println("Producer received response for msg: " + messageID);
            }

            @Override
            public void handleError(String messageID, JCSMPException e, long timestamp) {
                System.out.printf("Producer received error for msg: %s@%s - %s%n", messageID, timestamp, e);
            }
        });

        final XMLMessageConsumer consumer = session.getMessageConsumer(new XMLMessageListener() {
            @Override
            public void onReceive(BytesXMLMessage request) {
                if(request.getReplyTo() != null){
                    TextMessage reply = JCSMPFactory.onlyInstance().createMessage(TextMessage.class);
                    reply.setText("审核结果");
                    try {
                        producer.sendReply(request, reply);
                    } catch (JCSMPException e) {
                        System.out.println("Error sending reply.");
                    }
                }
                else{
                    System.out.println("Received message without reply-to field");
                }
            }

            @Override
            public void onException(JCSMPException e) {
                System.out.printf("Consumer received exception:%s%n",e);
                latch.countDown();
            }
        });

        // subscribe to a topic in order to express interest in receiving messages
        session.addSubscription(topic);
        consumer.start();

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        consumer.close();
        System.out.println("Exiting.");
        session.closeSession();
        return ResponseDto.success(message[0]);
    }

}
