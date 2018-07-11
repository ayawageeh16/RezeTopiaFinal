package io.krito.com.rezetopia.models.pojo.post;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ApiResponse {

    @SerializedName("next_cursor")
    @Expose
    private String nextCursor;

    @SerializedName("error")
    @Expose
    private boolean error;

    @SerializedName("posts")
    @Expose
    private Post[] posts;

    @SerializedName("pps")
    @Expose
    private Pp[] pps;

    @SerializedName("now")
    @Expose
    private long now;

    @SerializedName("message")
    @Expose
    private String message;

    public Pp[] getPps() {
        return pps;
    }

    public void setPps(Pp[] pps) {
        this.pps = pps;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Post[] getPosts() {
        return posts;
    }

    public void setPosts(Post[] posts) {
        this.posts = posts;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getNextCursor() {
        return nextCursor;
    }

    public void setNextCursor(String nextCursor) {
        this.nextCursor = nextCursor;
    }

    public long getNow() {
        return now;
    }

    public void setNow(long now) {
        this.now = now;
    }
}
