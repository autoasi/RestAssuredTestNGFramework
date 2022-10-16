package com.spotify.oauth2.api;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import java.util.HashMap;

import static com.spotify.oauth2.api.Route.*;
import static com.spotify.oauth2.api.SpecBuilder.*;
import static io.restassured.RestAssured.given;

// Reusable methods that can be used across all the different Spotify's APIs
public class RestResource {
    // The request should be an Object type as it should work with all APIs calls (Playlist, Artist, Album etc.)
    @Step
    public static Response post(Object requestPlaylist, String path, String token) {
        return given(getRequestSpec()).
                auth().oauth2(token).
                // header("Authorization","Bearer " + token).  // replaced by oauth2 method
                body(requestPlaylist).
        when().
                post(path). // Endpoint
        then().
                spec(getResponseSpec()).
                extract().
                response();
    }

    @Step
    public static Response get(String path, String token){
        return given(getRequestSpec()).
                auth().oauth2(token).
                // header("Authorization","Bearer " + token).   // replaced by oauth2 method
        when()
                .get(path). // Endpoint
        then()
                .spec(getResponseSpec()).
                extract().
                response();
    }

    @Step
    public static Response update(Object requestPlaylist, String path, String token){
        return given(getRequestSpec()).
                auth().oauth2(token).
                // header("Authorization","Bearer " + token).  // replaced by oauth2 method
                body(requestPlaylist).
        when()
                .put(path). // Endpoint
        then()
                .spec(getResponseSpec()).
                extract().
                response();
    }

    @Step
    public static Response postAccount(HashMap<String,String> formParams){
        return given(getAccountRequestSpec()).
                formParams(formParams).
        when().
                post(API + TOKEN).
        then().
                extract().
                response();
    }
}

