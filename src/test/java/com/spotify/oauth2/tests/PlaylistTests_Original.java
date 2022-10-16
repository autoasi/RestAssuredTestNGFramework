package com.spotify.oauth2.tests;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class PlaylistTests_Original {
    RequestSpecification requestSpecification;
    ResponseSpecification responseSpecification;
    String access_token = "BQDBQVB3OAm7mUkCZ0BpZZ_L2KF9-M1_BW8g-hJnJ1Q5kGL8rnhG4DmNvYTbax3eWrenp2rI9Lav7IMihYLi1oeeL_SW9MPxm6jQyD88ek2kN1lNuwLwUhMuIYSo1fTeajjGrc42ygkc78I4szf4oQMgAqDAru92tZH0HWggnNYbkVyyslVKbazR2G0o0Mrca63iZiMJ-s1ykhiixhc5qwWs7FeekOso8YIy8NdRgUBRS3gdXTYkQN_OoH8BWnRTFRPe1Y6R4N1TCAoQ";

    @BeforeClass
    public void beforeClass(){
        RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder().
                setBaseUri("https://api.spotify.com").
                setBasePath("/v1").
                addHeader("Authorization","Bearer " + access_token).
                setContentType(ContentType.JSON).
                log(LogDetail.ALL);
        requestSpecification = requestSpecBuilder.build();

        ResponseSpecBuilder responseSpecBuilder = new ResponseSpecBuilder().
                //expectContentType(ContentType.JSON). // Not needed for all the tests
                log(LogDetail.ALL);
        responseSpecification = responseSpecBuilder.build();
    }

    @Test
    public void shouldBeAbleToCreateAPlaylist(){
        String payload = "{\n" +
                "  \"name\": \"New Playlist\",\n" +
                "  \"description\": \"New playlist description\",\n" +
                "  \"public\": false\n" +
                "}";

        given(requestSpecification).
                body(payload).
        when()
                .post("users/31grlwwqnolhu5j72mf6ppb62n7u/playlists"). // Endpoint
        then()
                .spec(responseSpecification).
                assertThat().
                statusCode(201).
                contentType(ContentType.JSON).
                body("name",equalTo("New Playlist"),
                        "description",equalTo("New playlist description"),
                        "public", equalTo(false));
    }

    @Test
    public void shouldBeAbleToGetAPlaylist(){
        given(requestSpecification).
        when()
                .get("playlists/7CElVoJMf4x395kRFlvfQq"). // Endpoint
        then()
                .spec(responseSpecification).
                assertThat().
                statusCode(200).
                contentType(ContentType.JSON).
                body("name",equalTo("New Playlist"),
                        "description",equalTo("New playlist description"),
                        "public", equalTo(false));
    }

    @Test
    public void shouldBeAbleToUpdateAPlaylist(){
        String payload = "{\n" +
                "  \"name\": \"Updated Playlist\",\n" +
                "  \"description\": \"Updated playlist description\",\n" +
                "  \"public\": false\n" +
                "}";

        given(requestSpecification).
                body(payload).
        when()
                .put("playlists/2kA990TxJRjQVqh7M0c6Fd"). // Endpoint
        then()
                .spec(responseSpecification).
                assertThat().
                statusCode(200);
    }

    @Test
    public void shouldNotBeAbleToCreateAPlaylistWithoutName(){
        String payload = "{\n" +
                "  \"name\": \"\",\n" +   // Name field is null
                "  \"description\": \"New playlist description\",\n" +
                "  \"public\": false\n" +
                "}";

        given(requestSpecification).
                body(payload).
        when()
                .post("users/31grlwwqnolhu5j72mf6ppb62n7u/playlists"). // Endpoint
        then()
                .spec(responseSpecification).
                assertThat().
                statusCode(400).
                contentType(ContentType.JSON).
                body("error.status",equalTo(400),
                        "error.message",equalTo("Missing required field: name"));
    }

    @Test
    public void shouldNotBeAbleToCreateAPlaylistWithExpiredToken(){
        String payload = "{\n" +
                "  \"name\": \"New Playlist\",\n" +   // Name field is null
                "  \"description\": \"New playlist description\",\n" +
                "  \"public\": false\n" +
                "}";

        given().
                baseUri("https://api.spotify.com").
                basePath("/v1").
                header("Authorization","Bearer 12345678").  // Invalid access token
                contentType(ContentType.JSON).
                log().all().
                body(payload).
        when()
                .post("users/31grlwwqnolhu5j72mf6ppb62n7u/playlists"). // Endpoint
        then()
                .spec(responseSpecification).
                assertThat().
                statusCode(401).
                contentType(ContentType.JSON).
                body("error.status",equalTo(401),
                        "error.message",equalTo("Invalid access token"));
    }
}
