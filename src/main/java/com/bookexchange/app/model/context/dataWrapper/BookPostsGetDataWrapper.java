package com.bookexchange.app.model.context.dataWrapper;

import com.bookexchange.app.model.model.BookDO;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookPostsGetDataWrapper {

    @JsonProperty("contact")
    private @Getter @Setter String contact;

    @JsonProperty("books")
    private @Getter @Setter List<BookDO> bookList;

    public BookPostsGetDataWrapper(List<BookDO> bookList, String contact) {
        this.contact = contact;
        this.bookList = bookList;
    }

}
