package com.bookexchange.app.model.context.dataWrapper;

import com.bookexchange.app.model.model.UserDO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

public class UserPublicInfoDataWrapper {
    @JsonProperty("name")
    private @Getter @Setter String name;
    @JsonProperty("uid")
    private @Getter @Setter int uid;

    public UserPublicInfoDataWrapper() {}

    public UserPublicInfoDataWrapper(UserDO userDO) {
        this.name = userDO.getName();
        this.uid = userDO.getUid();
    }

}
