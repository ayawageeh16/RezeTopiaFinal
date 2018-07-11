package io.krito.com.rezetopia.models.pojo.post;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Mona Abdallh on 6/24/2018.
 */

public class Pp {
    @SerializedName("privacy_id")
    @Expose
    private int privacyId;

    @SerializedName("pp_url")
    @Expose
    private String ppUrl;

    @SerializedName("post_id")
    @Expose
    private String postId;

    @SerializedName("user_id")
    @Expose
    private String userId;

    @SerializedName("username")
    @Expose
    private String username;

    @SerializedName("image_url")
    @Expose
    private String imageUrl;

    @SerializedName("created_at")
    @Expose
    private String createdAt;

    @SerializedName("likes")
    @Expose
    private int[] likes;

    @SerializedName("shares")
    @Expose
    private int[] shares;

    @SerializedName("commentSize")
    @Expose
    private int commentSize;

    @SerializedName("text")
    @Expose
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getPrivacyId() {
        return privacyId;
    }

    public void setPrivacyId(int privacyId) {
        this.privacyId = privacyId;
    }

    public String getPpUrl() {
        return ppUrl;
    }

    public void setPpUrl(String ppUrl) {
        this.ppUrl = ppUrl;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public int[] getLikes() {
        return likes;
    }

    public void setLikes(int[] likes) {
        this.likes = likes;
    }

    public int[] getShares() {
        return shares;
    }

    public void setShares(int[] shares) {
        this.shares = shares;
    }

    public int getCommentSize() {
        return commentSize;
    }

    public void setCommentSize(int commentSize) {
        this.commentSize = commentSize;
    }
}
