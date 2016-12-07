package hasoffer.adp.core.models.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import hasoffer.adp.core.enums.Platform;
import hasoffer.adp.core.enums.SettlementWay;

import java.util.List;

/**
 * Created by lihongde on 2016/12/6 18:07
 */
public class MaterialVo {

    @JsonIgnore
    private  Long id;

    private String title;

    private String subTitle;

    private String description;

    private String btnText;

    private String openWay;

    private float price;

    private String url;

    private String putCountry;

    private String icon;

    private Platform putPlatform;

    private String minVersion;

    private String maxVersion;

    private String appType;

    private SettlementWay settlementWay;

    private String dailyRunning;

    private String pvRequestUrl;

    private List<MaterialCreativeVo> creatives;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBtnText() {
        return btnText;
    }

    public void setBtnText(String btnText) {
        this.btnText = btnText;
    }

    public String getOpenWay() {
        return openWay;
    }

    public void setOpenWay(String openWay) {
        this.openWay = openWay;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPutCountry() {
        return putCountry;
    }

    public void setPutCountry(String putCountry) {
        this.putCountry = putCountry;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Platform getPutPlatform() {
        return putPlatform;
    }

    public void setPutPlatform(Platform putPlatform) {
        this.putPlatform = putPlatform;
    }

    public String getMinVersion() {
        return minVersion;
    }

    public void setMinVersion(String minVersion) {
        this.minVersion = minVersion;
    }

    public String getMaxVersion() {
        return maxVersion;
    }

    public void setMaxVersion(String maxVersion) {
        this.maxVersion = maxVersion;
    }

    public String getAppType() {
        return appType;
    }

    public void setAppType(String appType) {
        this.appType = appType;
    }

    public SettlementWay getSettlementWay() {
        return settlementWay;
    }

    public void setSettlementWay(SettlementWay settlementWay) {
        this.settlementWay = settlementWay;
    }

    public String getDailyRunning() {
        return dailyRunning;
    }

    public void setDailyRunning(String dailyRunning) {
        this.dailyRunning = dailyRunning;
    }

    public String getPvRequestUrl() {
        return pvRequestUrl;
    }

    public void setPvRequestUrl(String pvRequestUrl) {
        this.pvRequestUrl = pvRequestUrl;
    }

    public List<MaterialCreativeVo> getCreatives() {
        return creatives;
    }

    public void setCreatives(List<MaterialCreativeVo> creatives) {
        this.creatives = creatives;
    }
}
