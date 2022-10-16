package com.spotify.oauth2.tests;

import com.spotify.oauth2.api.applicationApi.PlaylistApi;
import com.spotify.oauth2.pojo.Error;
import com.spotify.oauth2.pojo.Playlist;
import com.spotify.oauth2.utils.DataLoader;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class PlaylistTests_Refactor1 {

    @Test
    public void shouldBeAbleToCreateAPlaylist(){
        // Create payload using POJO and Builder Pattern
        Playlist requestPlaylist = Playlist.builder().
                name("New Playlist").
                description("New playlist description").
                _public(false).build();

        // Collect the response and asset status code
        Response response = PlaylistApi.post(requestPlaylist);
        assertThat(response.getStatusCode(),equalTo(201));

        // De-serialization - Assertion using the java object
        Playlist responsePlaylist = response.as(Playlist.class);

        assertThat(responsePlaylist.getName(),equalTo(requestPlaylist.getName()));
        assertThat(responsePlaylist.getDescription(),equalTo(requestPlaylist.getDescription()));
        assertThat(responsePlaylist.get_public(),equalTo(requestPlaylist.get_public()));
    }

    @Test
    public void shouldBeAbleToGetAPlaylist(){
        // Create payload using POJO and Builder Pattern
        Playlist requestPlaylist = Playlist.builder().
                name("New Playlist").
                description("New playlist description").
                _public(false).build();

        // Collect the response and asset status code
        Response response = PlaylistApi.get(DataLoader.getInstance().getGetPlaylistId());
        assertThat(response.getStatusCode(),equalTo(200));

        // De-serialization - Assertion using the java object
        Playlist responsePlaylist = response.as(Playlist.class);

        assertThat(responsePlaylist.getName(),equalTo(requestPlaylist.getName()));
        assertThat(responsePlaylist.getDescription(),equalTo(requestPlaylist.getDescription()));
        assertThat(responsePlaylist.get_public(),equalTo(requestPlaylist.get_public()));
    }

    @Test
    public void shouldBeAbleToUpdateAPlaylist(){
        // Create payload using POJO
        Playlist requestPlaylist = Playlist.builder().
                name("Updated Playlist").
                description("Updated playlist description").
                _public(false).
                build();

        // Collect the response and asset status code
        Response response = PlaylistApi.update(requestPlaylist, DataLoader.getInstance().getUpdatePlaylistId());
        assertThat(response.getStatusCode(),equalTo(200));
    }

    @Test
    public void shouldNotBeAbleToCreateAPlaylistWithoutName(){
        // Create payload using POJO
        Playlist requestPlaylist = Playlist.builder().
                name(""). // Name field is null
                description("New playlist description").
                _public(false).
                build();

        // Collect the response and asset status code
        Response response = PlaylistApi.post(requestPlaylist);
        assertThat(response.getStatusCode(),equalTo(400));

        // De-serialization - Assertion using the java object
        Error error = response.as(Error.class);

        assertThat(error.getError().getStatus(), equalTo(400));
        assertThat(error.getError().getMessage(), equalTo("Missing required field: name"));
    }

    @Test
    public void shouldNotBeAbleToCreateAPlaylistWithExpiredToken(){
        String invalid_token = "12345678";

        // Create payload using POJO
        Playlist requestPlaylist = Playlist.builder().
                name("New Playlist").
                description("New playlist description").
                _public(false).
                build();

        // Collect the response and asset status code
        Response response = PlaylistApi.post(requestPlaylist, invalid_token);
        assertThat(response.getStatusCode(),equalTo(401));

        // De-serialization - Assertion using the java object
        Error error = response.as(Error.class);

        assertThat(error.getError().getStatus(), equalTo(401));
        assertThat(error.getError().getMessage(), equalTo("Invalid access token"));
    }
}
