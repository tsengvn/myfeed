package com.tsengvn.myfeed.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author : hienngo
 * @since : Sep 01, 2016.
 */
public class Post {
    public enum Status {
        Add, Remove;
    }

    private String text;
    private String imgUrl;
    private float imgRatio;
    private long created;

    @JsonIgnore
    private String key;

    @JsonIgnore
    private Status status;

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

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Post)) return false;

        Post post = (Post) o;

        if (!getKey().equals(post.getKey())) return false;
        return getStatus() == post.getStatus();

    }

    @Override
    public int hashCode() {
        int result = getKey().hashCode();
        result = 31 * result + getStatus().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Post{" +
                "text='" + text + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", imgRatio=" + imgRatio +
                ", created=" + created +
                '}';
    }
}
