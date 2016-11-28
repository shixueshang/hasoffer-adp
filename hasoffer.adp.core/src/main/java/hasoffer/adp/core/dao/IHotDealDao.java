package hasoffer.adp.core.dao;

import hasoffer.adp.core.models.po.HotDealPo;
import org.apache.ibatis.annotations.*;

/**
 * Created by chevy on 16-11-19.
 */

public interface IHotDealDao {

    @Select("select * from t_hotdeal where id = #{id}")
    HotDealPo find(long id);

    @Options(useGeneratedKeys = true, keyProperty = "id") // 该注解的作用是，插入成功后，数据库生成的主键被更新到传入的对象中
    @Insert("insert into t_hotdeal(id, createtime, sourceurl, title, refprice) values (#{id}, #{createTime},#{sourceUrl}, #{title}, #{refPrice})")
    void insertWithId(HotDealPo hd);

    @Update("update t_hotdeal set refprice=#{price} where id=#{id}")
    void updatePrice(@Param(value = "id") long id, @Param(value = "price") float price);

    @Delete("delete from t_hotdeal where id=#{id}")
    void delete(long id);

//    @InsertProvider(type = HotDealPo.class, method = "insertSql")
//    void create(HotDealPo hd);

    @Options(useGeneratedKeys = true, keyProperty = "id") // 该注解的作用是，插入成功后，数据库生成的主键被更新到传入的对象中
    @Insert("insert into t_hotdeal(createtime, sourceurl, title, refprice) values (#{createTime},#{sourceUrl}, #{title}, #{refPrice})")
    void insert(HotDealPo hd);

}
