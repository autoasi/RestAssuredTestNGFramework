package com.spotify.oauth2.tests;

import com.spotify.oauth2.api.StatusCode;
import com.spotify.oauth2.api.applicationApi.PlaylistApi;
import com.spotify.oauth2.pojo.Error;
import com.spotify.oauth2.pojo.Playlist;
import com.spotify.oauth2.utils.DataLoader;
import io.qameta.allure.*;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import lombok.Getter;
import lombok.Setter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static com.spotify.oauth2.api.SpecBuilder.getRequestSpec;
import static com.spotify.oauth2.api.SpecBuilder.getResponseSpec;
import static com.spotify.oauth2.utils.FakerUtils.generateDescription;
import static com.spotify.oauth2.utils.FakerUtils.generateName;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

@Epic("Spotify OAuth 2.0")
@Feature("Playlist API")
public class PlaylistTests extends BaseTest{

    @Story("Create a playlist story")
    @Link("https://example.org")
    @Link(name = "allure", type = "mylink")
    @TmsLink("test-1") // Test management system (TMS)
    @Issue("432")  // For tracking defects/incidents
    @Description("Add test description here")
    @Test(description="Should be able to create a playlist")
    public void shouldBeAbleToCreateAPlaylist(){
        Playlist requestPlaylist = playlistBuilder(generateName(),generateDescription(),false);
        Response response = PlaylistApi.post(requestPlaylist);
        assertStatusCode(response.getStatusCode(), StatusCode.CODE_201);
        assertPlaylistEqual(response.as(Playlist.class),requestPlaylist);
    }

    @Severity(SeverityLevel.CRITICAL)
    @Issue("546")
    @Description("Add test description here")
    @Test(description="Should be able to get a playlist")
    public void shouldBeAbleToGetAPlaylist(){
        Playlist requestPlaylist = playlistBuilder("New Playlist","New playlist description",false);
        Response response = PlaylistApi.get(DataLoader.getInstance().getGetPlaylistId());
        assertStatusCode(response.getStatusCode(),StatusCode.CODE_200);
        assertPlaylistEqual(response.as(Playlist.class),requestPlaylist);
    }

    @Description("Add test description here")
    @Test(description="Should be able to update a playlist")
    public void shouldBeAbleToUpdateAPlaylist(){
        Playlist requestPlaylist = playlistBuilder(generateName(),generateDescription(),false);
        Response response = PlaylistApi.update(requestPlaylist, DataLoader.getInstance().getUpdatePlaylistId());
        assertStatusCode(response.getStatusCode(),StatusCode.CODE_200);
    }

    @Story("Create a playlist story")
    @Description("Add test description here")
    @Test(description="Should not be able to create a playlist without name")
    public void shouldNotBeAbleToCreateAPlaylistWithoutName(){
        Playlist requestPlaylist = playlistBuilder("",generateDescription(),false);
        Response response = PlaylistApi.post(requestPlaylist);
        assertStatusCode(response.getStatusCode(),StatusCode.CODE_400);
        assertError(response.as(Error.class), StatusCode.CODE_400);
    }

    @Story("Create a playlist story")
    @Description("Add test description here")
    @Test(description="Should not be able to create a playlist with expired token")
    public void shouldNotBeAbleToCreateAPlaylistWithExpiredToken(){
        String invalid_token = "12345678";
        Playlist requestPlaylist = playlistBuilder(generateName(),generateDescription(),false);
        Response response = PlaylistApi.post(requestPlaylist, invalid_token);
        assertStatusCode(response.getStatusCode(),StatusCode.CODE_401);
        assertError(response.as(Error.class), StatusCode.CODE_401);
    }

    /*   ------------- Common methods ----------*/
    @Step
    public Playlist playlistBuilder(String name, String description, boolean _public){
        return Playlist.builder().
                name(name).
                description(description).
                _public(_public).
                build();
    }

    @Step
    public void assertPlaylistEqual(Playlist responsePlaylist, Playlist requestPlaylist){
        assertThat(responsePlaylist.getName(),equalTo(requestPlaylist.getName()));
        assertThat(responsePlaylist.getDescription(),equalTo(requestPlaylist.getDescription()));
        assertThat(responsePlaylist.get_public(),equalTo(requestPlaylist.get_public()));
    }

    @Step
    public void assertStatusCode(int actualStatusCode, StatusCode statusCode){
        assertThat(actualStatusCode,equalTo(statusCode.getCode()));
    }

    @Step
    public void assertError(Error responseErr, StatusCode statusCode){
        assertThat(responseErr.getError().getStatus(), equalTo(statusCode.getCode()));
        assertThat(responseErr.getError().getMessage(), equalTo(statusCode.getMessage()));
    }
}
