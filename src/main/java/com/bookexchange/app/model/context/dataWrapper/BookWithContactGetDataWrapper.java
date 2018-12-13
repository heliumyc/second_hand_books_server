package com.bookexchange.app.model.context.dataWrapper;

import com.bookexchange.app.model.model.BookDO;
import com.bookexchange.app.model.model.BookWithContactDO;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookWithContactGetDataWrapper {

    @JsonProperty("book")
    private @Getter @Setter
    BookWithContactDO bookDO; // normally use book with contact

    public BookWithContactGetDataWrapper() {}

    public BookWithContactGetDataWrapper(BookWithContactDO bookDO) {
        this.bookDO = bookDO;
    }

}
