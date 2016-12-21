package hasoffer.adp.core.dao;

import hasoffer.adp.core.dao.sqlprovider.OrderSqlProvider;
import hasoffer.adp.core.models.po.Order;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

/**
 * Created by lihongde on 2016/12/19 16:46
 */
public interface IOrderDao {

    @SelectProvider(type = OrderSqlProvider.class, method = "getSqlByOrderTime")
    List<Order> find(@Param("orderTime") String submitDate);
}
