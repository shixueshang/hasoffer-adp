package hasoffer.adp.core.service;

import hasoffer.adp.core.dao.IHotDealDao;
import hasoffer.adp.core.models.po.HotDealPo;
import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Descripton of HotDealService
 * @since 1.0
 * @datetime 2016-11-23 23:20:19
 * @author Scarecroweib <weib_tion@126.com>
 */
@Component
public class HotDealService {
    @Resource
    IHotDealDao dao;
    
    public HotDealPo find(long id){
        return this.dao.find(id);
    }
    
    public void create(HotDealPo hd){
        this.dao.insert(hd);
    }
    
    public void createWithId(HotDealPo hd){
        this.dao.insertWithId(hd);
    }
    
    public void updatePrice(long id, float price){
        this.dao.updatePrice(id, price);
    }
    
    public void delete(long id){
        this.dao.delete(id);
    }
    
    @Transactional(transactionManager="txManager", rollbackFor=Exception.class)
    public void updatePriceWithTr(long id, float price) throws Exception{
        this.dao.updatePrice(id, price);
        throw new Exception();
    }
}
