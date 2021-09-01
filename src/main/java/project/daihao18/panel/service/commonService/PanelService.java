package project.daihao18.panel.service.commonService;

import project.daihao18.panel.common.response.Result;

import java.util.Map;

public interface PanelService {
    Result getNode(Map<String, Object> params);

    Result getUsers(Map<String, Object> params);

    Result getRules(Map<String, Object> params);

    Result postOnline(Map<String, Object> params);

    Result postTraffic(Map<String, Object> params);
}
