package com.bookexchange.app.utils;

import com.bookexchange.app.model.context.response.WXCode2SessionResponse;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

// singleton implementation
public enum WXLoginClient {

    INSTANCE;

    private Client loginClient;
    private WebTarget loginTarget;
    private String wxServerUri = "https://api.weixin.qq.com/sns/jscode2session";

    WXLoginClient() {
        this.loginClient = ClientBuilder.newClient();
        this.loginTarget = this.loginClient.target(this.wxServerUri);
    }

    public static WXLoginClient getInstance() {
        return INSTANCE;
    }

    public static WXCode2SessionResponse getWXSessionResponse(String appid, String secret, String code) {
        String GRAND_TYPE = "authorization_code";
        WXCode2SessionResponse sessionResp = null;
        WebTarget codeToSession = WXLoginClient.getInstance().loginTarget.queryParam("appid", appid)
                .queryParam("secret", secret)
                .queryParam("js_code", code)
                .queryParam("grand_type", GRAND_TYPE);
        Response response = codeToSession.request(MediaType.APPLICATION_JSON).get();
        if (response.getStatus() == 200) {
            try {
                sessionResp = new ObjectMapper().readValue(response.readEntity(String.class), WXCode2SessionResponse.class);
            } catch (JsonParseException e) {
                e.printStackTrace();
            } catch (JsonMappingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sessionResp;
    }

}
