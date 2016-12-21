package hasoffer.adp.core.dao;

import hasoffer.adp.core.models.po.Material;
import hasoffer.adp.core.models.po.MaterialCreative;
import org.apache.ibatis.annotations.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by lihongde on 2016/11/29 16:17
 */
public interface IMaterialDao {

    @Select("select * from t_material where id = #{id}")
    Material find(long id);

    @Select("select * from t_material limit #{offset}, #{size}")
    List<Material> findPage(@Param("offset") int offset, @Param("size") int size);

    @Select("select count(*) from t_material limit #{offset}, #{size}")
    int count(@Param("offset") int offset, @Param("size") int size);

    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("insert into t_material(title, subTitle, description, btnText, openWay, price, url, putCountry, icon, putPlatform, minVersion, maxVersion, appType, settlementWay, dailyRunning, pvRequestUrl, tags)" +
            " values (#{title}, #{subTitle}, #{description}, #{btnText}, #{openWay}, #{price}, #{url}, #{putCountry}, #{icon}, #{putPlatform}, #{minVersion}, #{maxVersion}, #{appType}, #{settlementWay}, #{dailyRunning}, #{pvRequestUrl}, #{tags})")
    @Transactional
    void insert(Material material);

    @Update("update t_material set title=#{title}, subTitle=#{subTitle}, description=#{description}, btnText=#{btnText}, openWay=#{openWay}, price=#{price}, url=#{url}, putCountry=#{putCountry}, icon=#{icon}, " +
            "putPlatform=#{putPlatform}, minVersion=#{minVersion}, maxVersion=#{maxVersion}, appType=#{appType}, settlementWay=#{settlementWay}, dailyRunning=#{dailyRunning}, tags=#{tags} where id=#{id}")
    @Transactional
    void update(Material material);

    @Select("select id, materialId, url, width, height from t_material_creative where materialId = #{materialId}")
    List<MaterialCreative> findCreativesByMaterialId(@Param("materialId") long materialId);

    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("insert into t_material_creative(materialId, url, width, height) values (#{materialId}, #{url}, #{width}, #{height})")
    @Transactional
    void insertCreative(MaterialCreative mc);

    @Select("select * from t_material_creative where width = #{width} and height = #{height}")
    List<MaterialCreative> findCreativesByWidthAndHeight(@Param("width") int width, @Param("height") int height);

    @Select("select * from t_material where tags like CONCAT('%','${tags}','%')")
    List<Material> findLikeByTag(@Param("tags") String tag);

}
