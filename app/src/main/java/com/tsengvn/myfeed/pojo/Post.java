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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Post)) return false;

        Post post = (Post) o;

        if (Float.compare(post.getImgRatio(), getImgRatio()) != 0) return false;
        if (getCreated() != post.getCreated()) return false;
        if (getText() != null ? !getText().equals(post.getText()) : post.getText() != null)
            return false;
        return getImgUrl() != null ? getImgUrl().equals(post.getImgUrl()) : post.getImgUrl() == null;

    }

    @Override
    public int hashCode() {
        int result = getText() != null ? getText().hashCode() : 0;
        result = 31 * result + (getImgUrl() != null ? getImgUrl().hashCode() : 0);
        result = 31 * result + (getImgRatio() != +0.0f ? Float.floatToIntBits(getImgRatio()) : 0);
        result = 31 * result + (int) (getCreated() ^ (getCreated() >>> 32));
        return result;
    }
}
