package com.ars.dto;

import com.ars.po.Activity;
import com.ars.utils.WrappedBeanCopier;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Ocean Liang
 * @date 3/13/2019
 */
@Data
public class ActivityDto {
    private String id;
    private String author;
    private String title;
    private String content;
    private List<UserDto> participants;
    private LocalDateTime startDate;
    private LocalDateTime closingDate;

}
