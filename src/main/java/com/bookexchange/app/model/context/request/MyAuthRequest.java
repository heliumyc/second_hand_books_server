package com.bookexchange.app.model.context.request;

import com.bookexchange.app.model.context.request.MyRequest;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MyAuthRequest extends MyRequest {

    @JsonProperty("sessionId")
    private @Getter @Setter String sessionId;

    public MyAuthRequest() {
        this.sessionId = "";
    }

    public MyAuthRequest(String sessionId) {
        this.sessionId = sessionId;
    }
}
