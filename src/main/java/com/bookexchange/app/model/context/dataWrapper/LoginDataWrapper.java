package com.bookexchange.app.model.context.dataWrapper;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoginDataWrapper {

    @JsonProperty("sessionId")
    private @Getter @Setter String thridSession;

    public LoginDataWrapper() {}

    public LoginDataWrapper(String thridSession) {
        this.thridSession = thridSession;
    }

}
