package hasoffer.adp.core.dao;

import hasoffer.adp.core.models.po.Equipment;
import org.apache.ibatis.annotations.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by lihongde on 2016/12/2 10:35
 */
public interface IEquipmentDao {

    @Select("select * from t_equipment where id = #{id}")
    Equipment find(long id);

    @Select("select * from t_equipment limit #{offset}, #{size}")
    List<Equipment> findPage(@Param("offset") int offset, @Param("size") int size);

    @Select("select count(*) from t_equipment limit #{offset}, #{size}")
    int count(@Param("offset") int offset, @Param("size") int size);

    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("insert into t_equipment(androidId, tags, createTime) values (#{androidId}, #{tags}, #{createTime})")
    @Transactional
    void insert(Equipment equipment);

    @Update("update t_equipment set androidId=#{androidId}, tags=#{tags} where id=#{id}")
    @Transactional
    void update(Equipment equipment);

    @Delete("delete from t_equipment")
    void truncate();

    @Select("select * from t_equipment where androidId = #{androidId}")
    Equipment findByAndroidid(@Param("androidId") String androidid);
}
