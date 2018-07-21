package io.krito.com.rezetopia.models.pojo.Group;

import android.media.Image;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GroupPostAttachment {

    @SerializedName("images")
    @Expose
    private List<Media> images = null;
    @SerializedName("videos")
    @Expose
    private List<Media> videos = null;

    public GroupPostAttachment() {
    }

    public GroupPostAttachment(List<Media> images, List<Media> videos) {
        super();
        this.images = images;
        this.videos = videos;
    }

    public List<Media> getImages() {
        return images;
    }

    public void setImages(List<Media> images) {
        this.images = images;
    }

    public List<Media> getVideos() {
        return videos;
    }

    public void setVideos(List<Media> videos) {
        this.videos = videos;
    }

}
