package com.bookexchange.app.model.context.dataWrapper;

import com.bookexchange.app.model.model.BookWithContactDO;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookListGetDataWrapper {

    @JsonProperty("books")
    private @Getter @Setter List<BookWithContactDO> bookList;

    public BookListGetDataWrapper(List<BookWithContactDO> bookList) {
        this.bookList = bookList;
    }

}
