package com.bookexchange.app.service;

import com.bookexchange.app.model.context.request.BooksPostAuthRequest;
import com.bookexchange.app.model.context.response.MyResponse;
import com.bookexchange.app.model.context.response.ResponseType;
import com.bookexchange.app.utils.SearchHelper;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.List;

@Path("/search")
@Service
public class TestSearchService {

    static int id=0;

    @Autowired
    private SearchHelper searchHelper;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public MyResponse getSearchResult(
            @DefaultValue("") @QueryParam("query") String query,
            @DefaultValue("10") @QueryParam("page_size") int pageSize,
            @DefaultValue("0") @QueryParam("page") int page) {
        System.out.println(query + " " + page + " " + pageSize);
        List<Integer> bookIdList;
        try {
            bookIdList = searchHelper.query(query, page, pageSize);
            if (bookIdList == null) {
                return MyResponse.getDefaultResponse(ResponseType.PARAMETERS_ERR);
            }
            else {
                for (Integer integer : bookIdList) {
                    System.out.println(integer);
                }
                return MyResponse.getDefaultResponse(ResponseType.SUCCESS);
            }
        } catch (Exception e) {
            return MyResponse.getDefaultResponse(ResponseType.INTERNAL_ERR);
        }
    }

    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public MyResponse getAll() {
        try {
            searchHelper.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return MyResponse.getDefaultResponse(ResponseType.SUCCESS);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public MyResponse postNewBook(BooksPostAuthRequest bookReq) {
        try {
            searchHelper.addNewDocument(bookReq.getBookDO().getBid(), bookReq.getBookDO().getTitle());
            return MyResponse.getDefaultResponse(ResponseType.SUCCESS);
        } catch (IOException e) {
            return MyResponse.getDefaultResponse(ResponseType.INTERNAL_ERR);
        }
    }

    @DELETE
    @Path("/{bid}")
    @Produces(MediaType.APPLICATION_JSON)
    public MyResponse deleteBook(@PathParam("bid") int bid) {
        try {
            searchHelper.deleteDocument(bid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return MyResponse.getDefaultResponse(ResponseType.SUCCESS);
    }

}
