package hasoffer.adp.core.service;

import hasoffer.adp.base.utils.page.Page;
import hasoffer.adp.core.dao.IEquipmentDao;
import hasoffer.adp.core.models.po.Equipment;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by lihongde on 2016/12/2 10:40
 */
@Service
public class EquipmentService {

    @Resource
    IEquipmentDao dao;

    @Resource
    private JdbcTemplate jdbcTemplate;

    public void insert(Equipment equipment){
        dao.insert(equipment);
    }

    public Equipment find(long id){
        return dao.find(id);
    }

    public Page<Equipment> findPage(int page, int size){
        int offset = (page - 1) * size;
        List<Equipment> list = dao.findPage(offset, size);
        int count  = dao.count(offset, size);
        return new Page<Equipment>(page, size, count, list);
    }

    public void update(Equipment equipment){
        dao.update(equipment);
    }

    public void truncate(){
        dao.truncate();
    }

    public void batchInsert(List<Equipment> equipments){
        String sql = "insert into t_equipment(androidId, tags, createTime) values(?,?,?)";
        jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter()
        {
            @Override
            public void setValues(PreparedStatement ps,int i)throws SQLException
            {
                ps.setString(1, equipments.get(i).getAndroidId());
                ps.setString(2, equipments.get(i).getTags());
                ps.setObject(3, equipments.get(i).getCreateTime());

                if (i % 2000 == 0) {
                    ps.executeBatch();
                }
            }
            @Override
            public int getBatchSize()
            {
                return equipments.size();
            }
        });

    }
}
