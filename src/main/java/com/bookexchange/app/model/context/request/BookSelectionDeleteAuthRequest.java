package com.bookexchange.app.model.context.request;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class BookSelectionDeleteAuthRequest extends MyAuthRequest {

    @JsonProperty("bid_selection")
    private @Getter @Setter List<Integer> bookSelection;

    public BookSelectionDeleteAuthRequest() {}

    public BookSelectionDeleteAuthRequest(List bookSelection) {
        this.bookSelection = bookSelection;
    }
}
