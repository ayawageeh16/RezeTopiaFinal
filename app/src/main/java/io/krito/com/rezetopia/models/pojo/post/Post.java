package io.krito.com.rezetopia.models.pojo.post;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Mona Abdallh on 4/11/2018.
 */

public class Post implements Serializable {

    @SerializedName("post_id")
    @Expose
    private String postId;

    @SerializedName("text")
    @Expose
    private String text;

    @SerializedName("attachment")
    @Expose
    private Attachment attachment;

    @SerializedName("comment_size")
    @Expose
    private int commentSize;

    @SerializedName("created_at")
    @Expose
    private String createdAt;

    @SerializedName("likes")
    @Expose
    private int[] likes;

    @SerializedName("privacy_id")
    @Expose
    private int privacyId;

    @SerializedName("username")
    @Expose
    private String username;

    @SerializedName("user_id")
    @Expose
    private String userId;

    @SerializedName("image_url")
    @Expose
    private String imageUrl;

    @SerializedName("liker_id")
    @Expose
    private int likerId;

    @SerializedName("liker_name")
    @Expose
    private String likerName;

    @SerializedName("s_timestamp")
    @Expose
    private String shareTimestamp;

    @SerializedName("sharer_id")
    @Expose
    private int sharerId;

    @SerializedName("s_username")
    @Expose
    private String sharerUsername;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("pp_url")
    @Expose
    private String ppUrl;

    @SerializedName("cover_url")
    @Expose
    private String coverUrl;

    public String getPpUrl() {
        return ppUrl;
    }

    public void setPpUrl(String ppUrl) {
        this.ppUrl = ppUrl;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public int getPrivacyId() {
        return privacyId;
    }

    public void setPrivacyId(int privacyId) {
        this.privacyId = privacyId;
    }

    public String getShareTimestamp() {
        return shareTimestamp;
    }

    public void setShareTimestamp(String shareTimestamp) {
        this.shareTimestamp = shareTimestamp;
    }

    public int getSharerId() {
        return sharerId;
    }

    public void setSharerId(int sharerId) {
        this.sharerId = sharerId;
    }

    public String getSharerUsername() {
        return sharerUsername;
    }

    public void setSharerUsername(String sharerUsername) {
        this.sharerUsername = sharerUsername;
    }

    public int getLikerId() {
        return likerId;
    }

    public void setLikerId(int likerId) {
        this.likerId = likerId;
    }

    public String getLikerName() {
        return likerName;
    }

    public void setLikerName(String likerName) {
        this.likerName = likerName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getCommentSize() {
        return commentSize;
    }

    public void setCommentSize(int commentSize) {
        this.commentSize = commentSize;
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

    public int[] getLikes() {
        return likes;
    }

    public void setLikes(int[] likes) {
        this.likes = likes;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Attachment getAttachment() {
        return attachment;
    }

    public void setAttachment(Attachment attachment) {
        this.attachment = attachment;
    }
    
    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
