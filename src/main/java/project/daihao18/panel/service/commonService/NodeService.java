package project.daihao18.panel.service.commonService;

import java.util.Map;

public interface NodeService<T> {

    T getById(Integer id);

    boolean postTraffic(Map<String, Object> params);
}
