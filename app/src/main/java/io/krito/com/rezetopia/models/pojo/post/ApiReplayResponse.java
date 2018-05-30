package io.krito.com.rezetopia.models.pojo.post;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ApiReplayResponse {
    @SerializedName("error")
    @Expose
    private boolean error;

    @SerializedName("replies")
    @Expose
    private Replay[] replies;


    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public Replay[] getReplies() {
        return replies;
    }

    public void setReplies(Replay[] replies) {
        this.replies = replies;
    }
}
