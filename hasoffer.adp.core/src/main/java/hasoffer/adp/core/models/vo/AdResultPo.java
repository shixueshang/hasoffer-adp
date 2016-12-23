package hasoffer.adp.core.models.vo;

import java.io.Serializable;

/**
 * Created by lihongde on 2016/12/22 17:49
 */
public class AdResultPo implements Serializable {

    private String title;

    private String desc;

    private String img;

    private String imgw;

    private String imgh;

    private String icon;

    private String clk_url;

    private String btn_text;

    private String[] imp_tks;

    private String[] clk_tks;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getImgw() {
        return imgw;
    }

    public void setImgw(String imgw) {
        this.imgw = imgw;
    }

    public String getImgh() {
        return imgh;
    }

    public void setImgh(String imgh) {
        this.imgh = imgh;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getClk_url() {
        return clk_url;
    }

    public void setClk_url(String clk_url) {
        this.clk_url = clk_url;
    }

    public String getBtn_text() {
        return btn_text;
    }

    public void setBtn_text(String btn_text) {
        this.btn_text = btn_text;
    }

    public String[] getImp_tks() {
        return imp_tks;
    }

    public void setImp_tks(String[] imp_tks) {
        this.imp_tks = imp_tks;
    }

    public String[] getClk_tks() {
        return clk_tks;
    }

    public void setClk_tks(String[] clk_tks) {
        this.clk_tks = clk_tks;
    }
}
