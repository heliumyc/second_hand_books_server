package com.bookexchange.app.service;

import com.bookexchange.app.model.context.dataWrapper.BookPostsGetDataWrapper;
import com.bookexchange.app.model.context.dataWrapper.LoginDataWrapper;
import com.bookexchange.app.model.context.dataWrapper.UserInfoDataWrapper;
import com.bookexchange.app.model.context.dataWrapper.UserPublicInfoDataWrapper;
import com.bookexchange.app.model.context.request.UserModifyAuthRequest;
import com.bookexchange.app.model.context.request.MyAuthRequest;
import com.bookexchange.app.model.context.response.MyResponse;
import com.bookexchange.app.model.context.response.ResponseType;
import com.bookexchange.app.model.context.response.WXCode2SessionResponse;
import com.bookexchange.app.model.dao.BookDAO;
import com.bookexchange.app.model.dao.UserDAO;
import com.bookexchange.app.model.model.BookDO;
import com.bookexchange.app.model.model.UserDO;
import com.bookexchange.app.utils.AuthValidationHelper;
import com.bookexchange.app.utils.WXLoginClient;
import org.jasypt.digest.StringDigester;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/users")
@Service // actually not necessary only for singleton pattern
public class UserService {

    private final StringDigester jasyptStringEncryptor;

    private final UserDAO userDAO;

    private final BookDAO bookDAO;

    private final AuthValidationHelper authValidationHelper;

    @Autowired
    public UserService(
            @Qualifier("jasyptStringEDigester") StringDigester jasyptStringEncryptor,
            UserDAO userDAO, BookDAO bookDAO,
            AuthValidationHelper authValidationHelper) {
        this.jasyptStringEncryptor = jasyptStringEncryptor;
        this.userDAO = userDAO;
        this.bookDAO = bookDAO;
        this.authValidationHelper = authValidationHelper;
    }

    /**
     * This handles user login and api is like below:
     * /api/users/login GET
     * Requests is set as get method because it carries code from wechat
     * and returns 3rd_session encrypted in the sever.
     * Code is carried in the header which will serve as authentication later on.
     * The main process of this function is
     * firstly getting code from the header,
     * then sending the appid + appsecret + code to wechat server
     * and receiving openid + session_key from thereof,
     * after that generating 3rd_session key and storing key-val pair
     * <3rd_session: session_key+openid> in redis or cache.
     * @param code 微信发的5min code
     * @return json
     */
    @GET
    @Path("login")
    @Produces(MediaType.APPLICATION_JSON)
    public MyResponse userLogin(@DefaultValue("") @QueryParam("code") String code){
        MyResponse loginRes;
        try {
            // 和微信的交互逻辑
            String APPID = System.getenv("WXAPPID");
            String APPSECRET = System.getenv("WXSECRET");
            WXCode2SessionResponse sessionResponse = WXLoginClient.getWXSessionResponse(APPID, APPSECRET, code);
            if (sessionResponse == null) {
                loginRes = MyResponse.getDefaultResponse(ResponseType.WX_COMMUNICATION_ERR);
            }
            else if (sessionResponse.getErrcode() == 0){
                // check exists
                // register if not in the db
                userDAO.tryRegister(sessionResponse.getOpenid());

                // generate new sessionId using sha 256
                String thirdSession = jasyptStringEncryptor.digest(sessionResponse.getSessionId()+" "+sessionResponse.getOpenid());
                // store in redis
                // 讲道理不需要存储sessionId, 这玩意拿着有什么用, 甚至于openid我都不想存,
                // 3rd_session就相当于JWT token, 反正都是rsa加密了的, 拿到这个token一定是用户, 除非是中间被人抓包
                // 但是用的又是SSL, 可能性很小, 而且就算我存了openid, sessionId, 做了一次校验, 但是我是rsa加密的
                // 一组openid+sessionId对应的有且仅有一个token呀(不考虑碰撞), 只用考虑这个东西存不存在就完了, 不存在
                // 就打回client重新发起login
                // 过了一天想想还是需要存openid的, 不然怎么验证身份, 真香
                // 又tm重构了, 其实可以存uid就够了, 存鸡毛openid
                Integer uid = userDAO.getUid(sessionResponse.getOpenid());
                if (uid == null) {
                    loginRes = MyResponse.getDefaultResponse(ResponseType.INTERNAL_ERR);
                }
                else {
                    authValidationHelper.storeSessionId(thirdSession, uid);
                    loginRes = new MyResponse<>(ResponseType.SUCCESS, new LoginDataWrapper(thirdSession));
                }
            }
            else {
                loginRes = MyResponse.getDefaultResponse(ResponseType.AUTHENTICATION_ERR);
            }
        } catch (Exception e) {
            e.printStackTrace();
            loginRes = MyResponse.getDefaultResponse(ResponseType.INTERNAL_ERR);
        }
        return loginRes;
    }

    /**
     * return the info in the dao
     * @param headers 3rd_session
     * @return json
     */
    @GET
    @Path("info")
    @Produces(MediaType.APPLICATION_JSON)
    public MyResponse getUserInfo(@Context HttpHeaders headers) {
        MyResponse resp;
        Integer uid = authValidationHelper.validate(headers.getHeaderString(HttpHeaders.AUTHORIZATION));
        if (uid != null) {
            UserDO user = userDAO.getUserInfo(uid);
            if (user != null)
                resp = new MyResponse<>(ResponseType.SUCCESS, new UserInfoDataWrapper(user));
            else
                resp = MyResponse.getDefaultResponse(ResponseType.INTERNAL_ERR);
        }
        else {
            resp = MyResponse.getDefaultResponse(ResponseType.AUTHENTICATION_ERR);
        }
        return resp;
    }

    /**
     * modify info in dao
     * @param request 用户修改请求json
     * @return json
     */
    @PUT
    @Path("info")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public MyResponse modifyUserIfo(UserModifyAuthRequest request) {
        MyResponse resp;
        Integer uid = authValidationHelper.checkSessionId(request.getSessionId());
        if (uid == null) {
            resp = MyResponse.getDefaultResponse(ResponseType.AUTHENTICATION_ERR);
            return resp;
        }
        else if (request.getContact() == null || request.getName() == null ||
                request.getName().length() > 30 || request.getContact().length() > 50){
            resp = MyResponse.getDefaultResponse(ResponseType.PARAMETERS_ERR);
            return resp;
        }
        else {
            try {
                userDAO.modifyUserInfo(uid, request.getContact(), request.getName());
                resp = MyResponse.getDefaultResponse(ResponseType.SUCCESS);
            } catch (Exception e) {
                e.printStackTrace();
                resp = MyResponse.getDefaultResponse(ResponseType.INTERNAL_ERR);
            }
        }
        return resp;
    }

    /**
     * delete a user information
     * @param request 标准请求json
     * @return json
     */
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public MyResponse deleteUser(MyAuthRequest request) {
        Integer uid = authValidationHelper.checkSessionId(request.getSessionId());
        MyResponse resp;
        if (uid == null) {
            resp = MyResponse.getDefaultResponse(ResponseType.AUTHENTICATION_ERR);
        }
        else {
            try {
                userDAO.deleteUser(uid);
                resp = MyResponse.getDefaultResponse(ResponseType.SUCCESS);
            } catch (Exception e) {
                e.printStackTrace();
                resp = MyResponse.getDefaultResponse(ResponseType.INTERNAL_ERR);
            }
        }
        return resp;
    }

    /**
     * return the books the user has posted
     * @param uid user id
     * @return json
     */
    @GET
    @Path("/{uid}/posts")
    @Produces(MediaType.APPLICATION_JSON)
    public MyResponse getUserBooks(@PathParam("uid") int uid) {
        // 不需要进行检测, 任何人可以访问
        MyResponse resp;
        try {
            List<BookDO> books = bookDAO.getBooksOfUser(uid);
            String contact = userDAO.getUserContact(uid);
            BookPostsGetDataWrapper bookRes = new BookPostsGetDataWrapper(books, contact);
            resp = new MyResponse<>(ResponseType.SUCCESS, bookRes);
        } catch (Exception e) {
            e.printStackTrace();
            resp = MyResponse.getDefaultResponse(ResponseType.INTERNAL_ERR);
        }
        return resp;
    }

    /**
     * get user public info which does not require any authentication
     * @param uid user id
     * @return json
     */
    @GET
    @Path("/{uid}/public-info")
    @Produces(MediaType.APPLICATION_JSON)
    public MyResponse getUserPublicInfo(@PathParam("uid") int uid) {
        // 不需要进行检测, 任何人可以访问
        MyResponse resp;
        try {
            UserDO user = userDAO.getUserInfo(uid);
            if (user != null)
                resp = new MyResponse<>(ResponseType.SUCCESS, new UserPublicInfoDataWrapper(user));
            else
                resp = MyResponse.getDefaultResponse(ResponseType.INTERNAL_ERR);
        } catch (Exception e) {
            e.printStackTrace();
            resp = MyResponse.getDefaultResponse(ResponseType.INTERNAL_ERR);
        }
        return resp;
    }

}
