package io.krito.com.rezetopia.models.pojo.post;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Attachment {

    @SerializedName("videos")
    @Expose
    private Media[] videos;

    @SerializedName("images")
    @Expose
    private Media[] images;


    public Media[] getVideos() {
        return videos;
    }

    public void setVideos(Media[] videos) {
        this.videos = videos;
    }

    public Media[] getImages() {
        return images;
    }

    public void setImages(Media[] images) {
        this.images = images;
    }
}
