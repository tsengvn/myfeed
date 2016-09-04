package com.tsengvn.myfeed.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author : hienngo
 * @since : Sep 02, 2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Image implements Parcelable{
    private String id;
    private String title;
    private String description;
    private boolean animated;
    private String link;
    private String score;
    private boolean isAlbum;
    private int width;
    private int height;
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public float getRatio() {
        return (float) getWidth()/(float) getHeight();
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isAnimated() {
        return animated;
    }

    public void setAnimated(boolean animated) {
        this.animated = animated;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public boolean isAlbum() {
        return isAlbum;
    }

    public void setAlbum(boolean album) {
        isAlbum = album;
    }

    public String getThumbnail() {
        int i = getLink().lastIndexOf(".");
        return new StringBuilder(getLink()).insert(i, "t").toString();
    }

    public static class List {
        private java.util.List<Image> data;
        private boolean success;
        private int status;

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public java.util.List<Image> getData() {
            return data;
        }

        public void setData(java.util.List<Image> data) {
            this.data = data;
        }
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.title);
        dest.writeString(this.description);
        dest.writeByte(this.animated ? (byte) 1 : (byte) 0);
        dest.writeString(this.link);
        dest.writeString(this.score);
        dest.writeByte(this.isAlbum ? (byte) 1 : (byte) 0);
        dest.writeInt(this.width);
        dest.writeInt(this.height);
        dest.writeString(this.type);
    }

    public Image() {
    }

    protected Image(Parcel in) {
        this.id = in.readString();
        this.title = in.readString();
        this.description = in.readString();
        this.animated = in.readByte() != 0;
        this.link = in.readString();
        this.score = in.readString();
        this.isAlbum = in.readByte() != 0;
        this.width = in.readInt();
        this.height = in.readInt();
        this.type = in.readString();
    }

    public static final Creator<Image> CREATOR = new Creator<Image>() {
        @Override
        public Image createFromParcel(Parcel source) {
            return new Image(source);
        }

        @Override
        public Image[] newArray(int size) {
            return new Image[size];
        }
    };
}
