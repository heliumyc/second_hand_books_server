package com.bookexchange.app.model.context.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MyResponse<T> {
    @JsonProperty("code")
    private @Getter @Setter int code;
    @JsonProperty("message")
    private @Getter @Setter String message;
    @JsonProperty("data")
    private @Getter @Setter T data;

    public MyResponse() {
        this.code = 0;
        this.message = "";
        this.data = null;
    }

    private MyResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }

    private MyResponse(int code, String message, T data) {
        this(code, message);
        this.data = data;
    }

    public MyResponse(ResponseType type, T data) {
        this(type.getCode(), type.getMessage(), data);
    }

    public static MyResponse getDefaultResponse(ResponseType type) {
        return new MyResponse(type.getCode(), type.getMessage());
    }

}
