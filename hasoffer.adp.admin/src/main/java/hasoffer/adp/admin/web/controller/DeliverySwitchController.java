package hasoffer.adp.admin.web.controller;

import hasoffer.adp.base.utils.Constants;
import hasoffer.data.redis.IRedisMapService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Created by lihongde on 2016/12/28 17:03
 */
@Controller
@RequestMapping(value = "/switch")
public class DeliverySwitchController extends BaseController {

    @Resource
    IRedisMapService redisMapService;

    @RequestMapping(value = "/listServers", method = RequestMethod.GET)
    public String listServers(Model model) {

        Map<String, Object> map = redisMapService.getMap(Constants.REDIS_MAP_KEY.DELIVERYSWITCH);
        model.addAttribute("servers", map);
        return "setting/listServers";
    }

    @RequestMapping(value = "/status/{mac}")
    public String status(@PathVariable(value = "mac") String mac) {
        String status = (String) redisMapService.getValue(Constants.REDIS_MAP_KEY.DELIVERYSWITCH, mac);
        boolean flag = Boolean.valueOf(status);
        redisMapService.putMap(Constants.REDIS_MAP_KEY.DELIVERYSWITCH, mac, String.valueOf(!flag));

        return "redirect:/switch/listServers";
    }
}
