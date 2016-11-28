package hasoffer.adp.core.models.po;

import hasoffer.adp.base.utils.TimeUtils;

import java.util.Date;

/**
 * Created by chevy on 16-11-19.
 */
public class HotDealPo {

    /**

     create table t_hotdeal (
        id bigint(20)  not null auto_increment primary key,
        createTime datetime,
        sourceUrl varchar(255),
        title varchar(255),
        refprice float
     ) DEFAULT CHARSET=utf8;


     */

    private long id;

    private Date createTime;

    private String sourceUrl;

    private String title;

    private float refPrice;

    public HotDealPo() {
        this.createTime = TimeUtils.now();
    }
    
    public HotDealPo(long id, String sourceUrl, String title, float refPrice){
        this(sourceUrl, title, refPrice);
        this.id = id;
    }

    public HotDealPo(String sourceUrl, String title, float refPrice) {
        this();
        this.sourceUrl = sourceUrl;
        this.title = title;
        this.refPrice = refPrice;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public float getRefPrice() {
        return refPrice;
    }

    public void setRefPrice(float refPrice) {
        this.refPrice = refPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HotDealPo hotDealPo = (HotDealPo) o;

        if (id != hotDealPo.id) return false;
        if (Float.compare(hotDealPo.refPrice, refPrice) != 0) return false;
        if (createTime != null ? !createTime.equals(hotDealPo.createTime) : hotDealPo.createTime != null) return false;
        if (sourceUrl != null ? !sourceUrl.equals(hotDealPo.sourceUrl) : hotDealPo.sourceUrl != null) return false;
        return !(title != null ? !title.equals(hotDealPo.title) : hotDealPo.title != null);

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (createTime != null ? createTime.hashCode() : 0);
        result = 31 * result + (sourceUrl != null ? sourceUrl.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (refPrice != +0.0f ? Float.floatToIntBits(refPrice) : 0);
        return result;
    }

    @Override
    public String toString() {
        return "HotDealPo{" +
                "id=" + id +
                ", createTime=" + createTime +
                ", sourceUrl='" + sourceUrl + '\'' +
                ", title='" + title + '\'' +
                ", refPrice=" + refPrice +
                '}';
    }

    /**
     *
     * createTime | datetime     | YES  |     | NULL    |                |
     | refPrice   | float        | NO   |     | NULL    |                |
     | stdProId   | bigint(20)   | NO   |     | NULL    |                |
     | title      | varchar(255) | YES  |     | NULL    |                |
     | color      | varchar(255) | YES  |     | NULL    |                |
     | size       | varchar(255) | YES  |     | NULL    |                |
     | sourceid   | varchar(255) | YES  |     | NULL    |                |
     | sourceUrl  | varchar(255) | YES  |     | NULL    |                |
     | brand      | varchar(255) | YES  |     | NULL    |                |
     | categoryId | bigint(20)   | NO   |     | NULL    |                |
     | model      | varchar(255) | YES  |     | NULL    |                |
     +------------+--------------+------+-----+---------+----------------+

     */

}
