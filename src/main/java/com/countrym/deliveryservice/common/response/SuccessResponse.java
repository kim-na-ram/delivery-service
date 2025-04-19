package com.countrym.deliveryservice.common.response;

import com.countrym.deliveryservice.common.exception.ResponseCode;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SuccessResponse<T> extends BaseResponse {
    private T data;

    private SuccessResponse(T data) {
        super(HttpStatus.OK.value(), ResponseCode.SUCCESS.getMessage());
        this.data = data;
    }

    public static <T> SuccessResponse<T> of(T data) {
        return new SuccessResponse<>(data);
    }
}
