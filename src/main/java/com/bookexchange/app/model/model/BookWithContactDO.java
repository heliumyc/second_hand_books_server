package com.bookexchange.app.model.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookWithContactDO extends BookDO {

    @JsonProperty("contact")
    private @Getter @Setter String contact;

    public BookWithContactDO() {}

    public BookWithContactDO(int bid, String title, String isbn, double price, String note, int uid, String contact) {
        super(bid, title, isbn, price, note, uid);
        this.contact = contact;
    }

    public BookWithContactDO(BookDO book, String contact) {
        super(book.getBid(), book.getTitle(), book.getIsbn(), book.getPrice(), book.getNote(), book.getUid());
        this.contact = contact;
    }

    @Override
    public String toString() {
        return super.toString() + " " + this.contact;
    }

}
