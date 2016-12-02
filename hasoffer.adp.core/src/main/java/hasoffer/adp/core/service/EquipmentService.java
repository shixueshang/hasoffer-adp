package hasoffer.adp.core.service;

import hasoffer.adp.base.utils.page.Page;
import hasoffer.adp.core.dao.IEquipmentDao;
import hasoffer.adp.core.models.po.Equipment;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by lihongde on 2016/12/2 10:40
 */
@Service
public class EquipmentService {

    @Resource
    IEquipmentDao dao;

    public void insert(Equipment equipment){
        dao.insert(equipment);
    }

    public Equipment find(long id){
        return dao.find(id);
    }

    public Page<Equipment> findPage(int page, int size){
        int offset = (page - 1) * size;
        List<Equipment> list = dao.findPage(offset, size);
        int count  = dao.count(offset, size);
        return new Page<Equipment>(page, size, count, list);
    }

    public void update(Equipment equipment){
        dao.update(equipment);
    }
}
