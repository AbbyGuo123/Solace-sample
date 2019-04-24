//package com.ars.controller;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.client.RestTemplate;
//
///**
// * @Author: Abby
// * @Description:
// * @Date: Create in 4:21 PM 4/17/2019
// * @Modified By:
// */
//@RestController
//public class ConsumerController {
//
//    @Autowired
//    RestTemplate restTemplate;
//
//    @RequestMapping(value = "/hello", method = RequestMethod.GET)
//    public String helloConsumer(){
//        return restTemplate.getForEntity("http://activity-service/activity/hello",String.class).getBody();
//    }
//}
