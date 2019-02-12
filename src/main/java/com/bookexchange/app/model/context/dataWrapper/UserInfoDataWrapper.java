package com.bookexchange.app.model.context.dataWrapper;

import com.bookexchange.app.model.model.UserDO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

public class UserInfoDataWrapper {
    @JsonProperty("name")
    private @Getter @Setter String name;
    @JsonProperty("contact")
    private @Getter @Setter String contact;
    @JsonProperty("uid")
    private @Getter @Setter int uid;

    public UserInfoDataWrapper() {}

    public UserInfoDataWrapper(UserDO userDO) {
        this.name = userDO.getName();
        this.contact = userDO.getContact();
        this.uid = userDO.getUid();
    }

}
