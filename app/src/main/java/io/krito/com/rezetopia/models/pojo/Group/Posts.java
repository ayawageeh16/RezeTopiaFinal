package io.krito.com.rezetopia.models.pojo.Group;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Posts {

    @SerializedName("posts")
    @Expose
    private List<Post> posts = null;
    @SerializedName("now")
    @Expose
    private Integer now;
    @SerializedName("next_cursor")
    @Expose
    private Integer nextCursor;
    @SerializedName("error")
    @Expose
    private Boolean error;


    public Posts() {
    }

    public Posts(List<Post> posts, Integer now, Integer nextCursor, Boolean error) {
        super();
        this.posts = posts;
        this.now = now;
        this.nextCursor = nextCursor;
        this.error = error;
    }

    public List<Post> getPost() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public Integer getNow() {
        return now;
    }

    public void setNow(Integer now) {
        this.now = now;
    }

    public Integer getNextCursor() {
        return nextCursor;
    }

    public void setNextCursor(Integer nextCursor) {
        this.nextCursor = nextCursor;
    }

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

}

