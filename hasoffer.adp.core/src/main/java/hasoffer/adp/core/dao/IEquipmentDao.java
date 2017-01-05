package hasoffer.adp.core.dao;

import hasoffer.adp.core.models.po.Device;
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

    @Delete("truncate table t_equipment")
    void truncate();

    @Select("select * from t_equipment where androidId = #{androidId}")
    Equipment findByAndroidid(@Param("androidId") String androidid);

    @Select("select * from t_equipment")
    List<Equipment> findAllEquips();

    @Select("SELECT deviceId, brand, deviceName FROM urmdevice where deviceName in('Xiaomi Redmi Note 3 kenzo', 'samsung SM-J200G j2ltedd', 'samsung SM-J700F j7eltexx', 'LWNOVO Lenovo A6000 Kraft-A6000', 'Xiaomi 2014818 2014818','LENOVO Lenovo K50a40 aio_otfp', 'samsung SM-G7102 ms013gxx')")
    List<Device> findDevices();
}
