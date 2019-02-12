package com.bookexchange.app.model.context.dataWrapper;

import com.bookexchange.app.model.model.BookDO;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class BookListGetDataWrapper {

    @JsonProperty("books")
    private @Getter @Setter List<BookDO.BookWithContactDO> bookList;

    public BookListGetDataWrapper(List<BookDO.BookWithContactDO> bookList) {
        this.bookList = bookList;
    }

}
