package hasoffer.adp.core.service;

import hasoffer.adp.core.dao.IOrderDao;
import hasoffer.adp.core.models.po.Order;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by lihongde on 2016/12/19 16:46
 */
@Service
public class OrderService {

    @Resource
    IOrderDao dao;

    public List<Order> findOrders(String submitDate){
        return dao.find(submitDate);
    }
}
