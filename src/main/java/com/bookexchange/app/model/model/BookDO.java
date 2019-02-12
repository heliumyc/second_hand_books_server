package com.bookexchange.app.model.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

public class BookDO {

    @JsonProperty("bid")
    private @Getter @Setter int bid;
    @JsonProperty("title")
    private @Getter @Setter String title;
    @JsonProperty("isbn")
    private @Getter @Setter String isbn;
    @JsonProperty("major")
    private @Getter @Setter int major;
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

    public BookDO(int bid, String title, String isbn, int major, double price, String note, int uid) {
        this.bid = bid;
        this.title = title;
        this.isbn = isbn;
        this.price = price;
        this.note = note;
        this.uid = uid;
        this.major = major;
    }

    public static class BookWithContactDO extends BookDO {
        @JsonProperty("contact")
        private @Getter @Setter String contact;
        public BookWithContactDO(BookDO bookDO, String contact) {
            super(bookDO.getBid(), bookDO.getTitle(), bookDO.getIsbn(), bookDO.getMajor(), bookDO.getPrice(), bookDO.getNote(), bookDO.getUid());
            this.contact = contact;
        }
    }

    @Override
    public String toString() {
        return this.title+" "+this.isbn+" "+this.price+" "+this.note + " " + this.uid;
    }

}
