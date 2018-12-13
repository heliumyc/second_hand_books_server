package com.bookexchange.app.model.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookDO {

    @JsonProperty("bid")
    private @Getter @Setter int bid;
    @JsonProperty("title")
    private @Getter @Setter String title;
    @JsonProperty("isbn")
    private @Getter @Setter String isbn;
    @JsonProperty("price")
    private @Getter @Setter double price;
    @JsonProperty("note")
    private @Getter @Setter String note;

    @JsonProperty("uid")
    private @Getter @Setter int uid;

    public BookDO() {
        this.isbn = "";
        this.note = "";
    }

    public BookDO(String title, double price, int uid) {
        this();
        this.title = title;
        this.price = price;
        this.uid = uid;
    }

    public BookDO(int bid, String title, String isbn, double price, String note, int uid) {
        this.bid = bid;
        this.title = title;
        this.isbn = isbn;
        this.price = price;
        this.note = note;
        this.uid = uid;
    }

    @Override
    public String toString() {
        return this.title+" "+this.isbn+" "+this.price+" "+this.note + " " + this.uid;
    }

}
