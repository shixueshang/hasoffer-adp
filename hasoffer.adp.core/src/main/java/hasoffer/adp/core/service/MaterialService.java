package hasoffer.adp.core.service;

import hasoffer.adp.base.utils.page.Page;
import hasoffer.adp.core.dao.IMaterialDao;
import hasoffer.adp.core.models.po.Material;
import hasoffer.adp.core.models.po.MaterialCreative;
import hasoffer.adp.core.models.vo.MaterialCreativeVo;
import hasoffer.adp.core.models.vo.MaterialVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by lihongde on 2016/11/29 16:16
 */
@Service
public class MaterialService {

    @Resource
    IMaterialDao dao;

    public void insert(Material material){
        dao.insert(material);
    }

    public Material find(long id){
        return dao.find(id);
    }

    public Page<Material> findPage(int page, int size){
        int offset = (page - 1) * size;
        List<Material> list = dao.findPage(offset, size);
        int count  = dao.count(offset, size);
        return new Page<Material>(page, size, count, list);
    }

    public void update(Material material){
        dao.update(material);
    }

    public List<MaterialCreativeVo> findCreatives(Long materialId){
        return dao.findCreativesByMaterialId(materialId);
    }

    public void insertCreatives(MaterialCreative mc){
        dao.insertCreative(mc);
    }
}
