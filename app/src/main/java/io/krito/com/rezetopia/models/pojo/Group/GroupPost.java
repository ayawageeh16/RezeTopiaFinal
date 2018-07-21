package io.krito.com.rezetopia.models.pojo.Group;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

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
private List<Admin> admins = null;
@SerializedName("members")
@Expose
private List<Member> members = null;
@SerializedName("picture")
@Expose
private String picture;
@SerializedName("cover")
@Expose
private String cover;
@SerializedName("posts")
@Expose
private Posts posts;


private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public GroupPost() {
}

public GroupPost(Boolean error, String groupName, String privacy, Integer membersNumber, String username, String groupDecription, List<Admin> admins, List<Member> members, String picture, String cover, Posts posts) {
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

public List<Admin> getAdmins() {
return admins;
}

public void setAdmins(List<Admin> admins) {
this.admins = admins;
}

public List<Member> getMembers() {
return members;
}

public void setMembers(List<Member> members) {
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

public Posts getPosts() {
return posts;
}

public void setPosts(Posts posts) {
this.posts = posts;
}

}