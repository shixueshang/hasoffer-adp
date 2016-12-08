package hasoffer.adp.core.models.po;

/**
 * Created by lihongde on 2016/12/2 17:25
 */
public class TagStatistical {

    private String androidid;

    private int xiaomi;

    private int lenovo;

    private int redmi;

    private int huawei;

    private int honor;

    private int samsung;

    private int meizu;

    public TagStatistical(){

    }

    public TagStatistical(String androidid){
        this.androidid = androidid;
    }

    public String getAndroidid() {
        return androidid;
    }

    public void setAndroidid(String androidid) {
        this.androidid = androidid;
    }

    public int getXiaomi() {
        return xiaomi;
    }

    public void setXiaomi(int xiaomi) {
        this.xiaomi = xiaomi;
    }

    public int getLenovo() {
        return lenovo;
    }

    public void setLenovo(int lenovo) {
        this.lenovo = lenovo;
    }

    public int getRedmi() {
        return redmi;
    }

    public void setRedmi(int redmi) {
        this.redmi = redmi;
    }

    public int getHuawei() {
        return huawei;
    }

    public void setHuawei(int huawei) {
        this.huawei = huawei;
    }

    public int getHonor() {
        return honor;
    }

    public void setHonor(int honor) {
        this.honor = honor;
    }

    public int getSamsung() {
        return samsung;
    }

    public void setSamsung(int samsung) {
        this.samsung = samsung;
    }

    public int getMeizu() {
        return meizu;
    }

    public void setMeizu(int meizu) {
        this.meizu = meizu;
    }
}
