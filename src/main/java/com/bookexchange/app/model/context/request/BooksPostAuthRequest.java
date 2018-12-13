package com.bookexchange.app.model.context.request;

import com.bookexchange.app.model.model.BookDO;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

public class BooksPostAuthRequest extends MyAuthRequest {

    @JsonProperty("book")
    private @Getter @Setter
    BookDO bookDO;

    public BooksPostAuthRequest() {}

    @Override
    public String toString() {
        return this.bookDO.toString();
    }
}
