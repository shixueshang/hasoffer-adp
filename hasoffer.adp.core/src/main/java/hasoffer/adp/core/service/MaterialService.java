package hasoffer.adp.core.service;

import hasoffer.adp.base.utils.page.Page;
import hasoffer.adp.core.dao.IMaterialDao;
import hasoffer.adp.core.models.po.Material;
import hasoffer.adp.core.models.po.MaterialCreative;
import hasoffer.adp.core.models.vo.MaterialCreativeVo;
import hasoffer.adp.core.models.vo.MaterialVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
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
        Material m = dao.find(id);
        List<MaterialCreative> mcv = dao.findCreativesByMaterialId(id);
        m.setCreatives(mcv);
        return m;
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

    public List<MaterialCreative> findCreatives(Long materialId){
        return dao.findCreativesByMaterialId(materialId);
    }

    public void insertCreatives(MaterialCreative mc){
        dao.insertCreative(mc);
    }

    public List<Material> findMaterials(int width, int height){
        List<Material> list = new ArrayList<>();
        List<MaterialCreative> mcs = dao.findCreativesByWidthAndHeight(width, height);
        for(MaterialCreative mc : mcs){
            Material m = this.find(mc.getMaterialId());
            list.add(m);
        }

        return list;
    }

    public List<Material> findLikeByTag(String tag){
        List<Material> list = new ArrayList<>();
        List<Material> ms =  dao.findLikeByTag(tag);
        for(Material mc : ms){
            Material m = this.find(mc.getId());
            list.add(m);
        }
        return list;
    }


}
