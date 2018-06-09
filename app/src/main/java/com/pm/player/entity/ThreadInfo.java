package com.pm.player.entity;

/**
 * Created by WeiSir on 2018/6/9.
 */

public class ThreadInfo {
    private int id;
    private String url;
    private int start;
    private int end;
    private int finished;

    public ThreadInfo() {
        super();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public int getFinished() {
        return finished;
    }

    public void setFinished(int finished) {
        this.finished = finished;
    }

    /**
     * Instantiates a new Thread info.
     *
     * @param id       the id
     * @param url      the url
     * @param start    the start
     * @param end      the end
     * @param finished the finished
     */
    public ThreadInfo(int id, String url, int start, int end, int finished) {

        this.id = id;
        this.url = url;
        this.start = start;
        this.end = end;
        this.finished = finished;
    }

    @Override
    public String toString() {
        return "ThreadInfo{" +
                "id=" + id +
                ", url='" + url + '\'' +
                ", start=" + start +
                ", end=" + end +
                ", finished=" + finished +
                '}';
    }
}
