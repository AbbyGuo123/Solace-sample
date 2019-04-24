package com.ars.dto;

import lombok.Data;

/**
 * @author Ocean Liang
 * @date 3/9/2019
 */
@Data
public class ResponseDto<T> {
    private String status;
    private String message;
    private T data;

    public static ResponseDto success() {
        ResponseDto responseDto = new ResponseDto();
        responseDto.setStatus("successful");
        return responseDto;
    }

    public static <V> ResponseDto success(V data) {
        ResponseDto responseDto = new ResponseDto();
        responseDto.setStatus("successful");
        responseDto.setData(data);
        return responseDto;
    }

    public static ResponseDto fail(String message) {
        ResponseDto responseDto = new ResponseDto();
        responseDto.setStatus("failed");
        responseDto.setMessage(message);
        return responseDto;
    }
}
