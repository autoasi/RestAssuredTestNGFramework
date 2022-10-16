package com.spotify.oauth2.api.applicationApi;

import com.spotify.oauth2.api.RestResource;
import com.spotify.oauth2.pojo.Playlist;
import com.spotify.oauth2.utils.ConfigLoader;
import io.restassured.response.Response;

import static com.spotify.oauth2.api.Route.*;
import static com.spotify.oauth2.api.TokenManager.getToken;

// Reusable methods for Playlist API
public class PlaylistApi {
    // static String access_token = "BQBjKqe3Y9c2xSgZ0qcaX04Ni8o9xQMZOV16f9_oV7H3ZSMkE2dFSdM7YpU5GRrzl1Im8VHOpulAHXoLCjTCqGwo2iDAgMsNGCGx__wVe0zXpa3EtNdRwmKmY89eHbZFQF7JtZoQXExN8mDlW5NJZsIDVsCsvUcGozP9XaLbQxaI-K-eKQDQGVfpZFHnabCAXrTeVV2hVX3qbMCU1-TQdC4k7zrX-kdfXDrlQYhtWp1PiDOYfxELf5zP6BPV91vHm__zVFN1Au3HxZaZ";

    public static Response post(Playlist requestPlaylist) {
        return RestResource.post(requestPlaylist,USERS + "/" + ConfigLoader.getInstance().getUser() + PLAYLISTS, getToken());
    }

    // Overload with token argument for negative testing
    public static Response post(Playlist requestPlaylist, String token) {
        return RestResource.post(requestPlaylist,USERS + "/" + ConfigLoader.getInstance().getUser() + PLAYLISTS, token);
    }

    public static Response get(String playlistId){
        return RestResource.get(PLAYLISTS + "/" + playlistId, getToken());
    }

    public static Response update(Playlist requestPlaylist, String playlistId){
        return RestResource.update(requestPlaylist,PLAYLISTS + "/" + playlistId, getToken());
    }
}
