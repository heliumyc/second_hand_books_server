package com.bookexchange.app.model.model;

import lombok.Getter;
import lombok.Setter;

public class UserDO {
    private @Getter @Setter int uid;
    private @Getter @Setter String contact = "";
    private @Getter @Setter String openid;
    private @Getter @Setter String name;

    public UserDO() {}

    public UserDO(Integer uid) { this.uid = uid; }

    public UserDO(Integer uid, String contact, String name) {
        this.contact = contact;
        this.uid = uid;
        this.name = name;
    }

    public UserDO(String openid) {
        this.openid = openid;
    }

    public UserDO(String openid, String contact, String name) {
        this.contact = contact;
        this.openid = openid;
        this.name = name;
    }

}
