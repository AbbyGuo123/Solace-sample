package com.ars.po;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;


/**
 * @author Ocean Liang
 * @date 3/8/2019
 */
@Data
public class Activity {
    @Id
    private String id;
    private String author;
    private String title;
    private String content;
    private List<User> participants;
    private LocalDateTime startDate;
    private LocalDateTime closingDate;

    public Activity(String author, String title, String content) {
        this.author = author;
        this.title = title;
        this.content = content;
    }
}