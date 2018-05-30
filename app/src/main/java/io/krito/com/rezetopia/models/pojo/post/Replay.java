package io.krito.com.rezetopia.models.pojo.post;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Replay implements Serializable{
    @SerializedName("replayId")
    @Expose
    private int replayId;

    @SerializedName("replierId")
    @Expose
    private int replierId;

    @SerializedName("replayText")
    @Expose
    private String replayText;

    @SerializedName("username")
    @Expose
    private String username;

    @SerializedName("likes")
    @Expose
    private int[] likes;

    @SerializedName("createdAt")
    @Expose
    private String createdAt;

    @SerializedName("pending")
    @Expose
    private boolean pending;

    public boolean isPending() {
        return pending;
    }

    public void setPending(boolean pending) {
        this.pending = pending;
    }

    public int[] getLikes() {
        return likes;
    }

    public void setLikes(int[] likes) {
        this.likes = likes;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getReplayId() {
        return replayId;
    }

    public void setReplayId(int replayId) {
        this.replayId = replayId;
    }

    public int getReplierId() {
        return replierId;
    }

    public void setReplierId(int replierId) {
        this.replierId = replierId;
    }

    public String getReplayText() {
        return replayText;
    }

    public void setReplayText(String replayText) {
        this.replayText = replayText;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
