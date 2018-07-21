package io.krito.com.rezetopia.models.pojo.Group;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

 public class Post {

    @SerializedName("post_id")
    @Expose
    private Integer postId;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("image_url")
    @Expose
    private String imageUrl;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("comment_size")
    @Expose
    private Integer commentSize;
    @SerializedName("likes")
    @Expose
    private List<Integer> likes = null;
    @SerializedName("attachment")
    @Expose
    private GroupPostAttachment attachment;

    public Post() {
    }


    public Post(Integer postId, Integer userId, String username, String text, String imageUrl, String createdAt, Integer commentSize, List<Integer> likes, GroupPostAttachment attachment) {
        super();
        this.postId = postId;
        this.userId = userId;
        this.username = username;
        this.text = text;
        this.imageUrl = imageUrl;
        this.createdAt = createdAt;
        this.commentSize = commentSize;
        this.likes = likes;
        this.attachment = attachment;
    }

    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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

    public Integer getCommentSize() {
        return commentSize;
    }

    public void setCommentSize(Integer commentSize) {
        this.commentSize = commentSize;
    }

    public List<Integer> getLikes() {
        return likes;
    }

    public void setLikes(List<Integer> likes) {
        this.likes = likes;
    }

    public GroupPostAttachment getAttachment() {
        return attachment;
    }

    public void setAttachment(GroupPostAttachment attachment) {
        this.attachment = attachment;
    }

}

