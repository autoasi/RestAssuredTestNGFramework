package com.spotify.oauth2.api;

import com.spotify.oauth2.utils.ConfigLoader;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.time.Instant;
import java.util.HashMap;

import static io.restassured.RestAssured.given;

public class TokenManager {

    private static String access_token;
    private static Instant expiry_time;

    // Synchronized methods enable a simple strategy for preventing thread interference,
    // when a thread accessing the method, it will for the previous thread to release the method
    public synchronized static String getToken(){
        try{
            if(access_token == null || Instant.now().isAfter(expiry_time)){
                System.out.println("Renewing token ...");
                Response response = renewToken();
                access_token = response.path("access_token");
                int expiryDurationInSeconds = response.path("expires_in");
                expiry_time = Instant.now().plusSeconds(expiryDurationInSeconds - 60);
            }else {
                System.out.println("Access token is still valid.");
            }
        }catch (Exception e){
            throw new RuntimeException("Abort! Failed to renew access token.");
        }
        return access_token;
    }

    private static Response renewToken(){
        HashMap<String,String> formParams = new HashMap<String,String>();
        formParams.put("grant_type", ConfigLoader.getInstance().getGrantType());
        formParams.put("client_id", ConfigLoader.getInstance().getClientId());
        formParams.put("client_secret", ConfigLoader.getInstance().getClientSecret());
        formParams.put("refresh_token", ConfigLoader.getInstance().getRefreshToken());

        Response response = RestResource.postAccount(formParams);

        if(response.getStatusCode() != 200){
            throw new RuntimeException("ABORT!!! Renew token failed.");
        }
        return response;
    }
}

