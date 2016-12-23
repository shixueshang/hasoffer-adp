package hasoffer.adp.core.dao;

import hasoffer.adp.core.models.po.Tag;
import hasoffer.adp.core.models.po.TagStatistical;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by lihongde on 2016/12/5 14:04
 */
public interface ITagStatisticalDao {

    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("insert into t_tag_statistical(androidid, xiaomi, lenovo, redmi, huawei, honor, samsung, meizu)" +
            " values (#{androidid}, #{xiaomi}, #{lenovo}, #{redmi}, #{huawei}, #{honor}, #{samsung}, #{meizu})")
    @Transactional
    void insert(TagStatistical tag);

    @Delete(" truncate table t_tag_statistical")
    void delete();

    @Select("select * from t_tag_statistical")
    List<TagStatistical> findAllTags();

    @Select("select aid,sum(samsung) as samsung, sum(xiaomi) as xiaomi, sum(redmi) as redmi, sum(moto) as moto, sum(leeco) as leeco, sum(lenovo) as lenovo " +
            "from t_tag group by aid")
    List<Tag> findTagsGroupByAid();

}
