package project.daihao18.panel.service.commonService;

import project.daihao18.panel.entity.pmpanel.PMPNode;

import java.util.Map;

public interface NodeService<T> {

    T getById(Integer id);

    boolean postTraffic(Map<String, Object> params);

    boolean updateHeartBeat(PMPNode node);
}
