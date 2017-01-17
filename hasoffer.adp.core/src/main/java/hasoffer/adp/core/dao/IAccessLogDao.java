package hasoffer.adp.core.dao;

import hasoffer.adp.core.models.po.AccessLog;
import hasoffer.adp.core.models.po.AccessLogDetail;
import hasoffer.adp.core.models.vo.AccessLogDetailVo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created by lihongde on 2017/1/11 16:21
 */
public interface IAccessLogDao {

    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("insert into t_access_log(date, requests, pvCallback, pvClicks, imgRequests, clicks) values (#{date}, #{requests}, #{pvCallback}, #{pvClicks}, #{imgRequests}, #{clicks})")
    @Transactional
    void insert(AccessLog log);

    @Select("select * from t_access_log where date between #{dateTimeStart} and #{dateTimeEnd} order by date limit #{offset}, #{size} ")
    List<AccessLog> findPage(@Param("offset") int offset, @Param("size") int size, @Param("dateTimeStart") Date dateTimeStart, @Param("dateTimeEnd") Date dateTimeEnd);

    @Select("select count(*) from t_access_log where date between #{dateTimeStart} and #{dateTimeEnd} limit #{offset}, #{size}")
    int count(@Param("offset") int offset, @Param("size") int size, @Param("dateTimeStart") Date dateTimeStart, @Param("dateTimeEnd") Date dateTimeEnd);

    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("insert into t_access_log_detail(date, mid, pvCallback, pvClicks, imgRequests, clicks) values (#{date}, #{mid}, #{pvCallback}, #{pvClicks}, #{imgRequests}, #{clicks})")
    @Transactional
    void insertLogDetail(AccessLogDetail logDetail);

    @Select("SELECT t.*, m.title FROM t_access_log_detail as t left join t_material as m on t.mid = m.id where date = #{date}")
    List<AccessLogDetailVo> findLogDetail(@Param("date") String dateStr);
}
