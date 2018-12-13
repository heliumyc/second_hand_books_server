package com.bookexchange.app.model.context.response;

public enum ResponseType {
//    enum
    SUCCESS(0, "ok"),
    AUTHENTICATION_ERR(1, "Authentication Error: invalid 3rd_session"),
    PARAMETERS_ERR(2, "Parameters Error: invalid parameters"),
    INTERNAL_ERR(3, "Internal Error: internal server process exception"),
    WX_COMMUNICATION_ERR(4, "Network Error: wechat server communication error");

    private int type;
    private String message;

    private ResponseType(int type, String message) {
        this.type = type;
        this.message = message;
    }

    public int getCode() {
        return this.type;
    }

    public String getMessage() {
        return this.message;
    }

}
