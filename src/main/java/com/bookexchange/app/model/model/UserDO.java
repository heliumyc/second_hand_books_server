package com.bookexchange.app.model.model;

import lombok.Getter;
import lombok.Setter;

public class UserDO {
    private @Getter @Setter int uid;
    private @Getter @Setter String contact = "";
    private @Getter @Setter String openid;

    public UserDO() {}

    public UserDO(Integer uid) { this.uid = uid; }

    public UserDO(Integer uid, String contact) {
        this.contact = contact;
        this.uid = uid;
    }

    public UserDO(String openid) {
        this.openid = openid;
    }

    public UserDO(String openid, String contact) {
        this.contact = contact;
        this.openid = openid;
    }

}
