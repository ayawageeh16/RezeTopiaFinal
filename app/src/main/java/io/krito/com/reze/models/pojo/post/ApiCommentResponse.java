package io.krito.com.reze.models.pojo.post;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ApiCommentResponse implements Serializable {

    @SerializedName("error")
    @Expose
    private boolean error;

    @SerializedName("comments")
    @Expose
    private Comment[] comments;

    @SerializedName("nextCursor")
    @Expose
    private String nextCursor;


    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public Comment[] getComments() {
        return comments;
    }

    public void setComments(Comment[] comments) {
        this.comments = comments;
    }

    public String getNextCursor() {
        return nextCursor;
    }

    public void setNextCursor(String nextCursor) {
        this.nextCursor = nextCursor;
    }
}
