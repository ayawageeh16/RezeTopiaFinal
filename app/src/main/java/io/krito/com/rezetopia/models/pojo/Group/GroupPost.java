package io.krito.com.rezetopia.models.pojo.Group;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class GroupPost {

    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("group_name")
    @Expose
    private String groupName;
    @SerializedName("privacy")
    @Expose
    private String privacy;
    @SerializedName("members_number")
    @Expose
    private Integer membersNumber;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("group_decription")
    @Expose
    private String groupDecription;
    @SerializedName("admins")
    @Expose
    private ArrayList<Admin> admins = null;
    @SerializedName("members")
    @Expose
    private ArrayList<Member> members = null;
    @SerializedName("picture")
    @Expose
    private String picture;
    @SerializedName("cover")
    @Expose
    private String cover;
    @SerializedName("posts")
    @Expose
    private Posts posts;
    @SerializedName("add_member")
    @Expose
    private String addMember;
    @SerializedName("add_post")
    @Expose
    private String addPost;
    @SerializedName("add_event")
    @Expose
    private String addEvent;
    @SerializedName("now")
    @Expose
    private String now;
    @SerializedName("next_cursor")
    @Expose
    private String nextCursor;

    public GroupPost() {
    }

    public GroupPost(Boolean error, String groupName, String privacy, Integer membersNumber, String username, String groupDecription, ArrayList<Admin> admins, ArrayList<Member> members, String picture, String cover,  Posts posts, String addMember, String addPost, String addEvent, String now, String nextCursor) {
        super();
        this.error = error;
        this.groupName = groupName;
        this.privacy = privacy;
        this.membersNumber = membersNumber;
        this.username = username;
        this.groupDecription = groupDecription;
        this.admins = admins;
        this.members = members;
        this.picture = picture;
        this.cover = cover;
        this.posts = posts;
        this.addMember = addMember;
        this.addPost = addPost;
        this.addEvent = addEvent;
        this.now = now;
        this.nextCursor = nextCursor;
    }

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getPrivacy() {
        return privacy;
    }

    public void setPrivacy(String privacy) {
        this.privacy = privacy;
    }

    public Integer getMembersNumber() {
        return membersNumber;
    }

    public void setMembersNumber(Integer membersNumber) {
        this.membersNumber = membersNumber;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getGroupDecription() {
        return groupDecription;
    }

    public void setGroupDecription(String groupDecription) {
        this.groupDecription = groupDecription;
    }

    public ArrayList<Admin> getAdmins() {
        return admins;
    }

    public void setAdmins(ArrayList<Admin> admins) {
        this.admins = admins;
    }

    public ArrayList<Member> getMembers() {
        return members;
    }

    public void setMembers(ArrayList<Member> members) {
        this.members = members;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public  Posts getPosts() {
        return posts;
    }

    public void setPosts( Posts posts) {
        this.posts = posts;
    }

    public String getAddMember() {
        return addMember;
    }

    public void setAddMember(String addMember) {
        this.addMember = addMember;
    }

    public String getAddPost() {
        return addPost;
    }

    public void setAddPost(String addPost) {
        this.addPost = addPost;
    }

    public String getAddEvent() {
        return addEvent;
    }

    public void setAddEvent(String addEvent) {
        this.addEvent = addEvent;
    }

    public String getNow() {
        return now;
    }

    public void setNow(String now) {
        this.now = now;
    }

    public String getNextCursor() {
        return nextCursor;
    }

    public void setNextCursor(String nextCursor) {
        this.nextCursor = nextCursor;
    }
}