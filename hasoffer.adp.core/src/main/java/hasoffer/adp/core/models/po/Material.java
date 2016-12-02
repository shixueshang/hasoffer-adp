package hasoffer.adp.core.models.po;

import hasoffer.adp.core.enums.Platform;
import hasoffer.adp.core.enums.SettlementWay;

/**
 * Created by lihongde on 2016/11/29 15:42
 */
public class Material {

    private Long id;

    private String title;

    private String subTitle;

    private String description;

    private String btnText;

    private boolean isCPI;

    private String openWay;

    private float price;

    private String url;

    private String putCountry;

    private String icon;

    private String otherIcon;

    private Platform putPlatform;

    private Integer platformVersion;

    private String appType;

    private SettlementWay settlementWay;

    private String dailyRunning;

    private String pvRequestUrl;

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

    public boolean getIsCPI() {
        return isCPI;
    }

    public void setIsCPI(boolean isCPI) {
        this.isCPI = isCPI;
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

    public String getOtherIcon() {
        return otherIcon;
    }

    public void setOtherIcon(String otherIcon) {
        this.otherIcon = otherIcon;
    }

    public Platform getPutPlatform() {
        return putPlatform;
    }

    public void setPutPlatform(Platform putPlatform) {
        this.putPlatform = putPlatform;
    }

    public Integer getPlatformVersion() {
        return platformVersion;
    }

    public void setPlatformVersion(Integer platformVersion) {
        this.platformVersion = platformVersion;
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
}
