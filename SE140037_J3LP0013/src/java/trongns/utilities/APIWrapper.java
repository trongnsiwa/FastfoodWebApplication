/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package trongns.utilities;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.IOException;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import trongns.google.GooglePOJO;

/**
 *
 * @author TrongNS
 */
public class APIWrapper {
    
    private static final String GOOGLE_CLIENT_ID = "963258687346-hku1b3ha2rlohiji95q2uk11e7hi0q02.apps.googleusercontent.com";
    private static final String GOOGLE_CLIENT_SECRET = "baEihXK0oLXdlB_lcFvxFohJ";
    private static final String GOOGLE_REDIRECT_URL = "http://localhost:8084/SE140037_J3LP0013/LoginGoogleController";
    private static String GOOGLE_LINK_GET_TOKEN = "https://accounts.google.com/o/oauth2/token";
    public static String GOOGLE_LINK_GET_USER_INFO = "https://www.googleapis.com/oauth2/v1/userinfo?alt=json&access_token=";
    public static String GOOGLE_GRANT_TYPE = "authorization_code";

    /**
     * @return the accessToken
     */
    public String getAccessToken(String code) throws IOException {
        String response = Request.Post(GOOGLE_LINK_GET_TOKEN)
                .bodyForm(Form.form().add("client_id", GOOGLE_CLIENT_ID)
                .add("client_secret", GOOGLE_CLIENT_SECRET)
                .add("redirect_uri", GOOGLE_REDIRECT_URL)
                .add("code", code)
                .add("grant_type", GOOGLE_GRANT_TYPE).build())
                .execute().returnContent().asString();
        
        JsonObject jsonObj = new Gson().fromJson(response, JsonObject.class);
        String accessToken = jsonObj.get("access_token").toString().replaceAll("\"", "");
        return accessToken;
    }
    
    public static GooglePOJO getUserDTO(final String accessToken) throws IOException {
        String infoUrl = GOOGLE_LINK_GET_USER_INFO + accessToken;
        String response = Request.Get(infoUrl).execute().returnContent().asString();
        GooglePOJO googlePojo = new Gson().fromJson(response, GooglePOJO.class);
        return googlePojo;
    }
}
