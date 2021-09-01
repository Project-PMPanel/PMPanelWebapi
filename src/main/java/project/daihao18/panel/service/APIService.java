package project.daihao18.panel.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.daihao18.panel.common.response.Result;
import project.daihao18.panel.service.commonService.PanelService;

import java.util.*;

@Service
public class APIService {

    @Autowired
    private PanelService panelService;

    public Result getNode(Map<String, Object> params) {
        return panelService.getNode(params);
    }

    public Result getUsers(Map<String, Object> params) {
        return panelService.getUsers(params);
    }

    public Result getRules(Map<String, Object> params) {
        return panelService.getRules(params);
    }

    public Result postOnline(Map<String, Object> params) {
        return panelService.postOnline(params);
    }

    public Result postTraffic(Map<String, Object> params) {
        return panelService.postTraffic(params);
    }
}
