package project.daihao18.panel.service.pmpanel;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.daihao18.panel.common.response.Result;
import project.daihao18.panel.entity.pmpanel.PMPNode;
import project.daihao18.panel.entity.pmpanel.PMPNodeWithDetect;
import project.daihao18.panel.entity.pmpanel.PMPOnline;
import project.daihao18.panel.entity.pmpanel.PMPUser;
import project.daihao18.panel.service.commonService.NodeService;
import project.daihao18.panel.service.commonService.PanelService;
import project.daihao18.panel.service.commonService.UserService;

import java.util.*;

@Slf4j
@Service
public class PMPanelService implements PanelService {

    @Autowired
    private PMPSsService ssService;

    @Autowired
    private PMPV2rayService v2rayService;

    @Autowired
    private PMPTrojanService trojanService;

    @Autowired
    private UserService<PMPUser> userService;

    @Autowired
    private PMPNodeWithDetectService nodeWithDetectService;

    @Autowired
    private PMPDetectListService detectListService;

    @Autowired
    private PMPOnlineService pmpOnlineService;

    private NodeService getNodeService(String type) {
        switch (type) {
            case "ss":
                return ssService;
            case "v2ray":
                return v2rayService;
            case "trojan":
                return trojanService;
        }
        return null;
    }

    @Override
    public Result getNode(Map<String, Object> params) {
        try {
            String type = params.get("type").toString();
            Integer id = Integer.parseInt(params.get("nodeId").toString());
            PMPNode node = (PMPNode) getNodeService(type).getById(id);
            if (ObjectUtil.isEmpty(node)) {
                return Result.error().data("Can't query this node, please check nodeId");
            } else {
                log.info("query node info successfully");
                return Result.success().data(node);
            }
        } catch (Exception e) {
            log.error(e.toString());
            return Result.error();
        }
    }

    private static List<PMPUser> lastUserList = null;

    @Override
    public Result getUsers(Map<String, Object> params) {
        try {
            String type = params.get("type").toString();
            Integer id = Integer.parseInt(params.get("nodeId").toString());
            Integer nodeClass = 0;
            PMPNode node = (PMPNode) getNodeService(type).getById(id);
            // 更新节点心跳
            getNodeService(type).updateHeartBeat(node);
            nodeClass = node.getClazz();
            // 本次的userList
            List<PMPUser> userList = userService.getEnabledUsers(nodeClass);
            return Result.success().data(userList);
        } catch (Exception e) {
            log.error(e.toString());
            return Result.error();
        }
    }

    @Override
    public Result getRules(Map<String, Object> params) {
        try {
            String type = params.get("type").toString();
            Integer id = Integer.parseInt(params.get("nodeId").toString());

            // 根据type和id查询指定审计规则,如果存在,则返回,如果不存在,则返回所有审计规则
            List<PMPNodeWithDetect> rules = nodeWithDetectService.getByTypeAndNodeId(type, id);
            if (ObjectUtil.isNotEmpty(rules)) {
                // 根据rules的detect_list_id查找具体的rules
                log.info("query rule list successfully");
                return Result.success().data(detectListService.getDetectsByTypeAndNodeId(type, id));
            } else {
                log.info("query rule list successfully");
                return Result.success().data(detectListService.list());
            }
        } catch (Exception e) {
            log.error(e.toString());
            return Result.error();
        }
    }

    @Override
    public Result postOnline(Map<String, Object> params) {
        try {
            Double tmpId = Double.valueOf(params.get("nodeId").toString());
            Integer id = tmpId.intValue();
            String type = params.get("type").toString();

            // 先删除该节点之前的所有记录
            pmpOnlineService.remove(new QueryWrapper<PMPOnline>().eq("node_id", id).eq("type", type));

            List<PMPOnline> onlineList = new ArrayList<>();
            List<Map<String, Object>> onlines = (List<Map<String, Object>>) params.get("onlines");
            Date now = new Date();
            if (ObjectUtil.isNotEmpty(onlines) && onlines.size() > 0) {
                Iterator<Map<String, Object>> iterator = onlines.iterator();
                while (iterator.hasNext()) {
                    Map<String, Object> map = iterator.next();
                    PMPOnline online = new PMPOnline();
                    // 兼容 field id
                    Object tmpUserId = map.get("user_id");
                    Double tmpUId;
                    if (ObjectUtil.isEmpty(tmpUserId)) {
                        tmpUId = Double.valueOf(map.get("id").toString());
                    } else {
                        tmpUId = Double.valueOf(tmpUserId.toString());
                    }
                    online.setUserId(tmpUId.intValue());
                    online.setTime(now);
                    online.setIp(map.get("ip").toString());
                    online.setType(type);
                    online.setNodeId(id);
                    onlineList.add(online);
                }
                if (pmpOnlineService.saveBatch(onlineList)) {
                    log.info("post user online successfully for {} node, id {}", type, id);
                    return Result.success();
                }
            }
        } catch (Exception e) {
            log.error(e.toString());
            return Result.error();
        }
        return Result.error();
    }

    @Override
    public Result postTraffic(Map<String, Object> params) {
        try {
            String type = params.get("type").toString();
            Double tmpId = Double.valueOf(params.get("nodeId").toString());
            Integer id = tmpId.intValue();
            // 同步节点流量
            if (getNodeService(type).postTraffic(params)) {
                log.info("post user traffic successfully for {} node, id {}", type, id);
                return Result.success();
            } else {
                return Result.error();
            }
        } catch (Exception e) {
            log.error(e.toString());
            return Result.error();
        }
    }
}
