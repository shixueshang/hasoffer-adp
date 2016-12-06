package hasoffer.adp.core.models.po;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Date;

/**
 * Created by lihongde on 2016/12/2 10:33
 */
public class Equipment {

    @JsonIgnore
    private Long id;

    private String androidId;

    private String tags;

    private Date createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getAndroidId() {
        return androidId;
    }

    public void setAndroidId(String androidId) {
        this.androidId = androidId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
