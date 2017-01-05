package hasoffer.adp.core.models.po;

/**
 * Created by lihongde on 2017/1/3 16:50
 */
public class Device {

    private String deviceId;

    private String brand;

    private String deviceName;

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}
