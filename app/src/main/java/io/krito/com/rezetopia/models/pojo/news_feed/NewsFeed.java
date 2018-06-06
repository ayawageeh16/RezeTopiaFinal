package io.krito.com.rezetopia.models.pojo.news_feed;

import java.util.ArrayList;

public class NewsFeed {

    ArrayList<NewsFeedItem> items;
    String nextCursor;
    long now;

    public ArrayList<NewsFeedItem> getItems() {
        return items;
    }

    public void addAllItems(ArrayList<NewsFeedItem> s){
        if (items != null){
            items.addAll(s);
        } else {
            items = s;
        }
    }

    public void setItems(ArrayList<NewsFeedItem> items) {
        this.items = items;
    }

    public String getNextCursor() {
        return nextCursor;
    }

    public void setNextCursor(String nextCursor) {
        this.nextCursor = nextCursor;
    }

    public long getNow() {
        return now;
    }

    public void setNow(long now) {
        this.now = now;
    }
}
