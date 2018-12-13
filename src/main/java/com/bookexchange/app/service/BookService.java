package com.bookexchange.app.service;

import com.bookexchange.app.model.context.dataWrapper.BookListGetDataWrapper;
import com.bookexchange.app.model.context.dataWrapper.BookWithContactGetDataWrapper;
import com.bookexchange.app.model.context.request.BookSelectionDeleteAuthRequest;
import com.bookexchange.app.model.context.request.BooksPostAuthRequest;
import com.bookexchange.app.model.context.request.BooksPutAuthRequest;
import com.bookexchange.app.model.context.response.MyResponse;
import com.bookexchange.app.model.context.response.ResponseType;
import com.bookexchange.app.model.dao.BookDAO;
import com.bookexchange.app.model.model.BookDO;
import com.bookexchange.app.model.model.BookWithContactDO;
import com.bookexchange.app.utils.AuthValidationHelper;
import org.apache.lucene.queryparser.classic.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Path("books")
@Service
public class BookService {

    private final BookDAO bookDAO;

    private final AuthValidationHelper authValidationHelper;

    @Autowired
    public BookService(BookDAO bookDAO, AuthValidationHelper authValidationHelper) {
        this.bookDAO = bookDAO;
        this.authValidationHelper = authValidationHelper;
    }

    /**
     * This handles books query and api is like below:
     * /api/books?query=&limit=&offset= GET
     * Query is to match the title of books in the database.
     * If no query is provided, result will be books ordered by time,
     * else ordered by query relevance calced by mysql engine.
     * Limit is the number of returned results with default arg as 10.
     * Offset is the starting postion of the first returned results, default as 0.
     * Limit and offset are used as pagenation.
     * This function requires no further arguments like authentication key,
     * since it is of open access.
     * @param query 查询式
     * @param pageSize 一页结果数
     * @param page 当前页数
     * @return json
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public MyResponse getBookList(
            @DefaultValue("") @QueryParam("query") String query,
            @DefaultValue("10") @QueryParam("page_size") int pageSize,
            @DefaultValue("1") @QueryParam("page") int page) {

        MyResponse resp;
        if (page <= 0 || pageSize <= 0) {
            resp = MyResponse.getDefaultResponse(ResponseType.PARAMETERS_ERR);
        }
        else {
            try {
                List<Integer> bookIdList = bookDAO.queryToBooksId(query, page, pageSize);
                List<BookWithContactDO> bookList = new ArrayList<>(bookIdList.size());
                for (Integer bid : bookIdList) {
                    BookWithContactDO book = bookDAO.getBookInfo(bid);
                    if (book != null) {
                        bookList.add(book);
                    }
                }
                BookListGetDataWrapper bookRes = new BookListGetDataWrapper(bookList);
                resp = new MyResponse<>(ResponseType.SUCCESS, bookRes);
            } catch (IOException | ParseException e) {
                e.printStackTrace();
                resp = MyResponse.getDefaultResponse(ResponseType.INTERNAL_ERR);
            }
        }

        return resp;
    }

    /**
     * This handles books post request and api is like below:
     * /api/books POST
     * It requires authentication with 3rd_session and all the information
     * of the book.
     * The info of the book will be stored in database.
     * @param bookReq 请求json
     * @return json
     */
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public MyResponse postNewBook(BooksPostAuthRequest bookReq) {

        Integer uid = authValidationHelper.checkSessionId(bookReq.getSessionId());
        MyResponse resp;
        if (uid == null) {
            resp = MyResponse.getDefaultResponse(ResponseType.AUTHENTICATION_ERR);
        }
        else {
            // data persistence
            try {
                BookDO book = bookReq.getBookDO();
                bookDAO.addNewBook(uid, book);
                resp = MyResponse.getDefaultResponse(ResponseType.SUCCESS);
            } catch (Exception e) {
                e.printStackTrace();
                resp = MyResponse.getDefaultResponse(ResponseType.INTERNAL_ERR);
            }
        }
        return resp;
    }

    /**
     * get a certain book information
     * @param bid 书籍id
     * @return json
     */
    @GET
    @Path("/{bid}")
    @Produces(MediaType.APPLICATION_JSON)
    public MyResponse getBookInfo(@PathParam("bid") int bid) {

        MyResponse resp;
        try {
            BookWithContactDO book = bookDAO.getBookInfo(bid);
            if (book == null) {
                resp = MyResponse.getDefaultResponse(ResponseType.PARAMETERS_ERR);
            }
            else {
                resp = new MyResponse<>(ResponseType.SUCCESS, new BookWithContactGetDataWrapper(book));
            }
        } catch (Exception e) {
            e.printStackTrace();
            resp = MyResponse.getDefaultResponse(ResponseType.INTERNAL_ERR);
        }

        return resp;
    }

    /**
     * This handles books info change request and api is like below:
     * /api/books/{book_id} PUT
     * It requires authentication with 3rd_session and renewed information of the book.
     * @param bid 书籍id
     * @param bookReq 请求json
     * @return json
     */
    @PUT
    @Path("/{bid}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public MyResponse modifyBookInfo(@PathParam("bid") int bid, BooksPutAuthRequest bookReq) {
        Integer uid = authValidationHelper.checkSessionId(bookReq.getSessionId());
        MyResponse resp;
        BookDO book = bookReq.getBookDO();
        if (uid == null) {
            resp = MyResponse.getDefaultResponse(ResponseType.AUTHENTICATION_ERR);
        }
        else if (book.getTitle() == null || book.getTitle().equals("")){
            resp = MyResponse.getDefaultResponse(ResponseType.PARAMETERS_ERR);
        }
        else {
            try {
                bookDAO.modifyBook(uid, bid ,book);
                resp = MyResponse.getDefaultResponse(ResponseType.SUCCESS);
            } catch (Exception e) {
                e.printStackTrace();
                resp = MyResponse.getDefaultResponse(ResponseType.INTERNAL_ERR);
            }
        }

        return resp;
    }

    /**
     * delete a selection of books
     * @param bookReq 请求json
     * @return json
     */
    @DELETE
    @Path("/selection")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public MyResponse deleteBookSelection(BookSelectionDeleteAuthRequest bookReq) {
        Integer uid = authValidationHelper.checkSessionId(bookReq.getSessionId());
        MyResponse resp;
        if (uid == null) {
            resp = MyResponse.getDefaultResponse(ResponseType.AUTHENTICATION_ERR);
        }
        else {
            try {
                List<Integer> selection = bookReq.getBookSelection();
                bookDAO.deleteBookSelection(uid, selection);
                resp = MyResponse.getDefaultResponse(ResponseType.SUCCESS);
            } catch (Exception e) {
                e.printStackTrace();
                resp = MyResponse.getDefaultResponse(ResponseType.INTERNAL_ERR);
            }
        }
        return resp;
    }

}
