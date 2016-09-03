package com.tsengvn.myfeed.pojo;

/**
 * @author : hienngo
 * @since : Sep 01, 2016.
 */
public class Post {
    private String text;
    private String imgUrl;
    private float imgRatio;
    private long created;

    public Post() {
    }

    public Post(String text, String imgUrl, float imgRatio, long created) {
        this.text = text;
        this.imgUrl = imgUrl;
        this.created = created;
        this.imgRatio = imgRatio;
    }

    public float getImgRatio() {
        return imgRatio;
    }

    public void setImgRatio(float imgRatio) {
        this.imgRatio = imgRatio;
    }

    public String getText() {
        return text;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public long getCreated() {
        return created;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public void setCreated(long created) {
        this.created = created;
    }
}
