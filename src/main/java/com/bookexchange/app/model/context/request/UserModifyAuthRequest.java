package com.bookexchange.app.model.context.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

public class UserModifyAuthRequest extends MyAuthRequest {

    @JsonProperty("contact")
    private @Getter @Setter String contact;

    @JsonProperty("name")
    private @Getter @Setter String name;

    public UserModifyAuthRequest() {}


}
