package com.pm.player.entity;

import java.io.Serializable;

/**
 * Created by WeiSir on 2018/6/7.
 */

public class VideoInfo implements Serializable{
    private static final long serialVersionUID=-7920222595800367956L;
    private int id;
    /**视频标题**/
    private String title;
    /**视频标题**/
    private String path;
    private String artist;
    private String displayName;
    private String album;
    private String mimeType;
    private long size;
    private long duration;

    public VideoInfo(int id, String title, String path, String artist, String displayName, String album, String mimeType, long size, long duration) {
        this.id = id;
        this.title = title;
        this.path = path;
        this.artist = artist;
        this.displayName = displayName;
        this.album = album;
        this.mimeType = mimeType;
        this.size = size;
        this.duration = duration;
    }

    public VideoInfo() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "VideoInfo{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", path='" + path + '\'' +
                ", artist='" + artist + '\'' +
                ", displayName='" + displayName + '\'' +
                ", album='" + album + '\'' +
                ", mimeType='" + mimeType + '\'' +
                ", size=" + size +
                ", duration=" + duration +
                '}';
    }
}
