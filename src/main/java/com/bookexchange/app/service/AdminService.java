package com.bookexchange.app.service;

import com.bookexchange.app.model.context.response.MyResponse;
import com.bookexchange.app.model.context.response.ResponseType;
import org.springframework.stereotype.Service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/admin")
@Service
public class AdminService {

    @GET
    @Path("/reindex")
    public MyResponse reindex() {

        return MyResponse.getDefaultResponse(ResponseType.SUCCESS);
    }
}
