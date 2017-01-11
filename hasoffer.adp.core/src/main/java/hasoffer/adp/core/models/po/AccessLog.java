package hasoffer.adp.core.models.po;

import java.util.Date;

/**
 * Created by lihongde on 2017/1/11 15:27
 */
public class AccessLog {

    private Long id;

    private Date date;

    private int requests;

    private int pvCallback;

    private int pvClicks;

    private int imgRequests;

    private int clicks;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getRequests() {
        return requests;
    }

    public void setRequests(int requests) {
        this.requests = requests;
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
}
