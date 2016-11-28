package hasoffer.adp.core.core.test.db;

import hasoffer.adp.core.configuration.CoreConfiguration;
import hasoffer.adp.core.models.po.HotDealPo;
import hasoffer.adp.core.service.HotDealService;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by chevy on 16-11-19.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=CoreConfiguration.class)
public class TestDao {

    @Resource
    HotDealService hotDealService;

    @Test
    public void test_query() {
        System.out.println("=================test_query==================");
        HotDealPo hd = hotDealService.find(1000);
        if(hd != null){
            System.out.println(hd.toString());
        }
    }

    @Test
    public void test_create() throws Exception {
        System.out.println("=================test_create==================");
        HotDealPo hd = new HotDealPo(1022L, "https://item.jd.com/2342601.html", "Apple MacBook Air 13.3英寸笔记本电脑 银色(Core i5 处理器/8GB内存/128GB SSD闪存 MMGF2CH)", 6988);
        hotDealService.createWithId(hd);
        
        HotDealPo hdForSlave = new HotDealPo("https://item.jd.com/2342601.html", "Apple MacBook Air 13.3英寸笔记本电脑 银色(Core i5 处理器/8GB内存/128GB SSD闪存 MMGF2CH)", 6988);
        hotDealService.create(hdForSlave);
        
        System.out.println(hd.getId());

        HotDealPo hd2 = hotDealService.find(hd.getId());

        System.out.println(hd2.toString());
    }

    @Test
    public void test_update() {
        System.out.println("=================test_update==================");
        HotDealPo hd = hotDealService.find(1);
        System.out.println(hd.toString());

        hotDealService.updatePrice(1, 10000);

        hd = hotDealService.find(1);
        System.out.println(hd.toString());
    }

    @Test
    public void test_delete() {
        System.out.println("=================test_delete==================");
        HotDealPo hd = hotDealService.find(1022);
        System.out.println(hd.toString());

        hotDealService.delete(1022);

        hd = hotDealService.find(1022);
        System.out.println(hd == null ? "deleted" : "not null");
    }

    @Test
    public void test_transactional() {
        System.out.println("=================test_transactional==================");
        HotDealPo hd = hotDealService.find(1);
        System.out.println(hd.toString());
        
        try {
            hotDealService.updatePriceWithTr(1, 20000);
        } catch (Exception ex) {
            Logger.getLogger(TestDao.class.getName()).log(Level.SEVERE, "Exception!!", ex);
        }
        
        hd = hotDealService.find(1);
        System.out.println(hd.toString());
    }

}
