package hasoffer.adp.core.models.vo;

import java.util.Date;

/**
 * Created by lihongde on 2017/1/17 16:09
 */
public class AccessLogDetailVo {

    private long id;

    private Date date;

    private long mid;

    private int pvCallback;

    private int pvClicks;

    private int imgRequests;

    private int clicks;

    private String title;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public long getMid() {
        return mid;
    }

    public void setMid(long mid) {
        this.mid = mid;
    }

    public int getPvCallback() {
        return pvCallback;
    }

    public void setPvCallback(int pvCallback) {
        this.pvCallback = pvCallback;
    }

    public int getPvClicks() {
        return pvClicks;
    }

    public void setPvClicks(int pvClicks) {
        this.pvClicks = pvClicks;
    }

    public int getImgRequests() {
        return imgRequests;
    }

    public void setImgRequests(int imgRequests) {
        this.imgRequests = imgRequests;
    }

    public int getClicks() {
        return clicks;
    }

    public void setClicks(int clicks) {
        this.clicks = clicks;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
