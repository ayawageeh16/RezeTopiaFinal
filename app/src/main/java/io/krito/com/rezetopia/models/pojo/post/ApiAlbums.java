package io.krito.com.rezetopia.models.pojo.post;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Mona Abdallh on 6/13/2018.
 */

public class ApiAlbums {

    @SerializedName("error")
    @Expose
    private boolean error;

    @SerializedName("albums")
    @Expose
    private Album[] albums;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public Album[] getAlbums() {
        return albums;
    }

    public void setAlbums(Album[] albums) {
        this.albums = albums;
    }
}
