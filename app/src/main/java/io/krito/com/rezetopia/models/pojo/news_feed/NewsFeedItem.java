package io.krito.com.rezetopia.models.pojo.news_feed;

import io.krito.com.rezetopia.models.pojo.post.Attachment;

public class NewsFeedItem {

    public static final int PRODUCT_TYPE = 0;
    public static final int POST_TYPE = 1;
    public static final int VENDOR_POST_TYPE = 2;
    public static final int EVENT_TYPE = 3;
    public static final int GROUP_POSTS_TYPE = 4;

    private int type;
    private int id;
    private String ownerId;
    private String postText;
    private Attachment postAttachment;
    private String createdAt;
    private int[] likes;
    private String ownerName;
    private String productTitle;
    private float productPrice;
    private int productAmount;
    private String productImageUrl;
    private String itemImage;
    private String description;
    private int productSale;
    private int productSoldAmount;
    private String occurDate;
    private String itemName;
    private int groupId;
    private int vendorId;
    private String postId;
    private int commentSize;
    private int storeId;
    private int nextCursor;

    public int getNextCursor() {
        return nextCursor;
    }

    public void setNextCursor(int nextCursor) {
        this.nextCursor = nextCursor;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public String getItemImage() {
        return itemImage;
    }

    public void setItemImage(String itemImage) {
        this.itemImage = itemImage;
    }

    public int getCommentSize() {
        return commentSize;
    }

    public void setCommentSize(int commentSize) {
        this.commentSize = commentSize;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public int getVendorId() {
        return vendorId;
    }

    public void setVendorId(int vendorId) {
        this.vendorId = vendorId;
    }

    public String getOccurDate() {
        return occurDate;
    }

    public void setOccurDate(String occurDate) {
        this.occurDate = occurDate;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getPostText() {
        return postText;
    }

    public void setPostText(String postText) {
        this.postText = postText;
    }

    public Attachment getPostAttachment() {
        return postAttachment;
    }

    public void setPostAttachment(Attachment postAttachment) {
        this.postAttachment = postAttachment;
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

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public float getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(float productPrice) {
        this.productPrice = productPrice;
    }

    public int getProductAmount() {
        return productAmount;
    }

    public void setProductAmount(int productAmount) {
        this.productAmount = productAmount;
    }

    public String getProductImageUrl() {
        return productImageUrl;
    }

    public void setProductImageUrl(String productImageUrl) {
        this.productImageUrl = productImageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getProductSale() {
        return productSale;
    }

    public void setProductSale(int productSale) {
        this.productSale = productSale;
    }

    public int getProductSoldAmount() {
        return productSoldAmount;
    }

    public void setProductSoldAmount(int productSoldAmount) {
        this.productSoldAmount = productSoldAmount;
    }
}

