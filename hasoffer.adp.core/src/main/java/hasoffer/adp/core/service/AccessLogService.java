package hasoffer.adp.core.service;

import hasoffer.adp.base.utils.page.Page;
import hasoffer.adp.core.dao.IAccessLogDao;
import hasoffer.adp.core.models.po.AccessLog;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Created by lihongde on 2017/1/11 16:20
 */
@Service
public class AccessLogService {

    @Resource
    IAccessLogDao dao;

    public void insert(AccessLog log) {
        dao.insert(log);
    }

    public Page<AccessLog> findPage(int page, int size, Date dateTimeStart, Date dateTimeEnd) {
        int offset = (page - 1) * size;
        List<AccessLog> list = dao.findPage(offset, size, dateTimeStart, dateTimeEnd);
        int count = dao.count(offset, size, dateTimeStart, dateTimeEnd);
        return new Page<AccessLog>(page, size, count, list);
    }
}
