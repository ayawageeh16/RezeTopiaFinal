package io.krito.com.rezetopia.models.pojo.Group;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Posts implements Parcelable {

    @SerializedName("posts")
    @Expose
    private List<Post> posts = null;
    @SerializedName("now")
    @Expose
    private Integer now;
    @SerializedName("next_cursor")
    @Expose
    private Integer nextCursor;
    @SerializedName("error")
    @Expose
    private Boolean error;


    public Posts() {
    }

    public Posts(List<Post> posts, Integer now, Integer nextCursor, Boolean error) {
        super();
        this.posts = posts;
        this.now = now;
        this.nextCursor = nextCursor;
        this.error = error;
    }

    protected Posts(Parcel in) {
        if (in.readByte() == 0) {
            now = null;
        } else {
            now = in.readInt();
        }
        if (in.readByte() == 0) {
            nextCursor = null;
        } else {
            nextCursor = in.readInt();
        }
        byte tmpError = in.readByte();
        error = tmpError == 0 ? null : tmpError == 1;
    }

    public static final Creator<Posts> CREATOR = new Creator<Posts>() {
        @Override
        public Posts createFromParcel(Parcel in) {
            return new Posts(in);
        }

        @Override
        public Posts[] newArray(int size) {
            return new Posts[size];
        }
    };

    public List<Post> getPost() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public Integer getNow() {
        return now;
    }

    public void setNow(Integer now) {
        this.now = now;
    }

    public Integer getNextCursor() {
        return nextCursor;
    }

    public void setNextCursor(Integer nextCursor) {
        this.nextCursor = nextCursor;
    }

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (now == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(now);
        }
        if (nextCursor == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(nextCursor);
        }
        dest.writeByte((byte) (error == null ? 0 : error ? 1 : 2));
    }
}

