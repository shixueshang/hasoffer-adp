package hasoffer.adp.api.controller;

import hasoffer.adp.base.utils.AjaxJson;
import hasoffer.adp.base.utils.Constants;
import hasoffer.adp.core.models.po.Order;
import hasoffer.adp.core.service.OrderService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by lihongde on 2016/12/19 16:30
 */
@RestController
@RequestMapping("/order")
public class OrderController extends BaseController {

    @Resource
    private OrderService orderService;

    @RequestMapping(value = "/count", method = RequestMethod.GET)
    public AjaxJson count(@RequestParam(value = "submitDate", required = false) String submitDate){
        List<Order> orders = orderService.findOrders(submitDate);
        Map<String, Object> result = new ConcurrentHashMap<>();
        result.put("orderNum", orders.size());
        double amount = 0;
        double commission = 0;

        for(Order order : orders){
            amount += order.getSaleAmount();
            commission += order.getCommission();
        }
        result.put("totalAmount", amount);
        result.put("totalCommission", commission);
        result.put("clicks", 0);
        return new AjaxJson(Constants.HttpStatus.OK, result);
    }
}
