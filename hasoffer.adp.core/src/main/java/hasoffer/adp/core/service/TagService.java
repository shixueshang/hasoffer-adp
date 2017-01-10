package hasoffer.adp.core.service;

import hasoffer.adp.core.dao.ITagStatisticalDao;
import hasoffer.adp.core.models.po.Tag;
import hasoffer.adp.core.models.po.Tag2;
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
            @Override
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
            @Override
            public int getBatchSize()
            {
                return tags.size();
            }
        });

    }

    public List<TagStatistical> findAllTags(){
        return dao.findAllTags();
    }

    public List<Tag2> findAllTag2s() {
        return dao.findAllTag2s();
    }


    public void batchInsertTag(List<Tag> tags) {
        String sql = "insert into t_tag(aid, ymd, samsung, xiaomi, redmi, moto, leeco, lenovo) values(?,?,?,?,?,?,?,?)";
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setString(1, tags.get(i).getAid());
                ps.setString(2, tags.get(i).getYmd());
                ps.setInt(3, tags.get(i).getSamsung());
                ps.setInt(4, tags.get(i).getXiaomi());
                ps.setInt(5, tags.get(i).getRedmi());
                ps.setInt(6, tags.get(i).getMoto());
                ps.setInt(7, tags.get(i).getLeeco());
                ps.setInt(8, tags.get(i).getLenovo());

                if (i % 2000 == 0) {
                    System.out.println("batchsize == " + i);
                    ps.executeBatch();
                }
            }

            @Override
            public int getBatchSize() {
                return tags.size();
            }
        });

    }

    public List<Tag> findTagsGroupByAid() {
        return dao.findTagsGroupByAid();
    }


    public void truncate2() {
        dao.truncateTag2();
    }


    public void batchInsertTag2(List<Tag2> tags) {
        String sql = "insert into t_tag2(aid, OnePlus_3T, Moto_G_Plus_4th_Gen, Lenovo_Vibe_K5_Note, LeEco_Le_1s_Eco, Moto_M, Lenovo_Phab_2, Panasonic_Eluga_Note, SAMSUNG_Galaxy_On8, SAMSUNG_Galaxy_On_Nxt, Yu_Yureka_Plus) values(?,?,?,?,?,?,?,?,?,?,?)";
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setString(1, tags.get(i).getAid());
                ps.setInt(2, tags.get(i).getOnePlus_3T());
                ps.setInt(3, tags.get(i).getMoto_G_Plus_4th_Gen());
                ps.setInt(4, tags.get(i).getLenovo_Vibe_K5_Note());
                ps.setInt(5, tags.get(i).getLeEco_Le_1s_Eco());
                ps.setInt(6, tags.get(i).getMoto_M());
                ps.setInt(7, tags.get(i).getLenovo_Phab_2());
                ps.setInt(8, tags.get(i).getPanasonic_Eluga_Note());
                ps.setInt(9, tags.get(i).getSAMSUNG_Galaxy_On8());
                ps.setInt(10, tags.get(i).getSAMSUNG_Galaxy_On_Nxt());
                ps.setInt(11, tags.get(i).getYu_Yureka_Plus());

                if (i % 2000 == 0) {
                    System.out.println("batchsize == " + i);
                    ps.executeBatch();
                }
            }

            @Override
            public int getBatchSize() {
                return tags.size();
            }
        });

    }
}
