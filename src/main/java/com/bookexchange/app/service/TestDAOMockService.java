package com.bookexchange.app.service;

import com.bookexchange.app.model.dao.BookDAO;
import com.bookexchange.app.model.dao.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/test")
@Component
public class TestDAOMockService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    BookDAO bookDAO;

    @GET
    @Path("/slow-write")
    @Produces(MediaType.APPLICATION_JSON)
    public String testSlow() {
//        try {
//            bookDAO.testTransactionSlow();
//        } catch (Exception e) {
//        }
        return "good luck";
    }

    @GET
    @Path("/fast-write")
    @Produces(MediaType.APPLICATION_JSON)
    public String testFast() {
//        try {
//            bookDAO.testTransactionFast();
//        } catch (Exception e) {
//        }
        return "good luck";
    }

}
