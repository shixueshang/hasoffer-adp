package hasoffer.adp.core.models.po;

/**
 * Created by lihongde on 2016/12/3 14:46
 */
public class MaterialCreative {

    private Long id;

    private Long materialId;

    private String url;

    private String width;

    private String height;

    public MaterialCreative(Long materialId, String url, String width, String height){
        this.materialId = materialId;
        this.url = url;
        this.width = width;
        this.height = height;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMaterialId() {
        return materialId;
    }

    public void setMaterialId(Long materialId) {
        this.materialId = materialId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }
}
