package io.krito.com.rezetopia.models.pojo.post;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Comment implements Serializable {

    @SerializedName("commentId")
    @Expose
    private int commentId;

    @SerializedName("commenterId")
    @Expose
    private int commenterId;

    @SerializedName("commenterText")
    @Expose
    private String commentText;

    @SerializedName("replay_size")
    @Expose
    private int replaySize;

    @SerializedName("commenterName")
    @Expose
    private String commenterName;

    @SerializedName("createdAt")
    @Expose
    private String createdAt;

    @SerializedName("image_url")
    @Expose
    private String imgUrl;

    @SerializedName("likes")
    @Expose
    private int[] likes;

    @SerializedName("pending")
    @Expose
    private boolean pending;

    public boolean isPending() {
        return pending;
    }

    public void setPending(boolean pending) {
        this.pending = pending;
    }

    public int getReplaySize() {
        return replaySize;
    }

    public void setReplaySize(int replaySize) {
        this.replaySize = replaySize;
    }

    public int[] getLikes() {
        return likes;
    }

    public void setLikes(int[] likes) {
        this.likes = likes;
    }

    public String getCommenterName() {
        return commenterName;
    }

    public void setCommenterName(String commenterName) {
        this.commenterName = commenterName;
    }

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public int getCommenterId() {
        return commenterId;
    }

    public void setCommenterId(int commenterId) {
        this.commenterId = commenterId;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}

