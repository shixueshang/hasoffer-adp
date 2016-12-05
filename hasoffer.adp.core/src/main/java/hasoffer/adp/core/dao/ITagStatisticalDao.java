package hasoffer.adp.core.dao;

import hasoffer.adp.core.models.po.TagStatistical;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by lihongde on 2016/12/5 14:04
 */
public interface ITagStatisticalDao {

    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("insert into t_tag_statistical(androidid, xiaomi, lenovo, redmi, huawei, honor, samsung, meizu)" +
            " values (#{androidid}, #{xiaomi}, #{lenovo}, #{redmi}, #{huawei}, #{honor}, #{samsung}, #{meizu})")
    @Transactional
    void insert(TagStatistical tag);

    @Delete(" delete from t_tag_statistical")
    void delete();

}
