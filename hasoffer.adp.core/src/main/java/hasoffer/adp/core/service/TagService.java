package hasoffer.adp.core.service;

import hasoffer.adp.core.dao.ITagStatisticalDao;
import hasoffer.adp.core.models.po.TagStatistical;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by lihongde on 2016/12/5 14:00
 */
@Service
public class TagService {

    @Resource
    ITagStatisticalDao dao;

    @Resource
    private JdbcTemplate jdbcTemplate;

    public void insert(TagStatistical tag){
        dao.insert(tag);
    }

    public void truncate(){
        dao.delete();
    }

    public void batchInsert(List<TagStatistical> tags){
        String sql = "insert into t_tag_statistical(androidid, xiaomi, lenovo, redmi, huawei, honor, samsung, meizu) values(?,?,?,?,?,?,?,?)";
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter()
        {
            public void setValues(PreparedStatement ps,int i)throws SQLException
            {
                ps.setString(1, tags.get(i).getAndroidid());
                ps.setInt(2, tags.get(i).getXiaomi());
                ps.setInt(3, tags.get(i).getLenovo());
                ps.setInt(4, tags.get(i).getRedmi());
                ps.setInt(5, tags.get(i).getHuawei());
                ps.setInt(6, tags.get(i).getHonor());
                ps.setInt(7, tags.get(i).getSamsung());
                ps.setInt(8, tags.get(i).getMeizu());

                if (i % 2000 == 0) {
                    ps.executeBatch();
                }
            }
            public int getBatchSize()
            {
                return tags.size();
            }
        });

    }

    public List<TagStatistical> findAllTags(){
        return dao.findAllTags();
    }
}
