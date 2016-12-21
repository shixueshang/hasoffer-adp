package hasoffer.adp.core.dao.sqlprovider;

import org.apache.ibatis.jdbc.SQL;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * Created by lihongde on 2016/12/19 17:37
 */
public class OrderSqlProvider {

    private final static String TABLE_NAME = "t_order";

    public String getSqlByOrderTime(Map<String, Object> parameter){
        String orderTime = (String) parameter.get("orderTime");

       return new SQL()
        {
            {
                SELECT("*");
                FROM(TABLE_NAME);
                if(!StringUtils.isEmpty(orderTime)){
                    WHERE("date_format(orderTime, '%Y-%m-%d')='" + orderTime + "'");
                }
            }
        } .toString();
    }


}
