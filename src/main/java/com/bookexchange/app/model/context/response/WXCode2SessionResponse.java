package com.bookexchange.app.model.context.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class WXCode2SessionResponse {
    @JsonProperty("errcode")
    private @Getter @Setter int errcode;
    @JsonProperty("errmsg")
    private @Getter @Setter String errMessage;
    @JsonProperty("session_key")
    private @Getter @Setter String sessionId;
    @JsonProperty("openid")
    private @Getter @Setter String openid;
    @JsonProperty("unionid")
    private @Getter @Setter String unionid;

    public WXCode2SessionResponse() {}

    @Override
    public String toString() {
        return this.errcode + " " + this.sessionId + " " + this.openid;
    }

}
