package io.krito.com.reze.helper;

/**
 * Created by Mona Abdallh on 4/26/2018.
 */


public class MenuCustomItem {
    private String title;
    private int imageRes;

    public MenuCustomItem(String title, int imageRes) {
        this.title = title;
        this.imageRes = imageRes;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImageRes() {
        return imageRes;
    }

    public void setImageRes(int imageRes) {
        this.imageRes = imageRes;
    }
}
