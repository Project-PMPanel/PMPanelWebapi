package project.daihao18.panel.service.pmpanel;

import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.daihao18.panel.common.response.Result;
import project.daihao18.panel.entity.pmpanel.*;
import project.daihao18.panel.service.commonService.NodeService;
import project.daihao18.panel.service.commonService.PanelService;
import project.daihao18.panel.service.commonService.UserService;

import java.math.BigDecimal;
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
            Double tmpId = (Double) params.get("nodeId");
            Integer id = tmpId.intValue();
            PMPNode node = (PMPNode) getNodeService(type).getById(id);
            return ObjectUtil.isEmpty(node) ? Result.error().data("Can't query this node, please check nodeId") : Result.success().data(node);
        } catch (Exception e) {
            return Result.error();
        }
    }

    private static List<PMPUser> lastUserList = null;

    @Override
    public Result getUsers(Map<String, Object> params) {
        try {
            String type = params.get("type").toString();
            Double tmpId = (Double) params.get("nodeId");
            Integer id = tmpId.intValue();
            Integer nodeClass = 0;
            PMPNode node = (PMPNode) getNodeService(type).getById(id);
            nodeClass = node.getClazz();
            // 本次的userList
            List<PMPUser> userList = userService.getEnabledUsers(nodeClass);

            Boolean all = false;
            if (ObjectUtil.isNotEmpty(params.get("all"))) {
                all = (Boolean) params.get("all");
            }
            // 需要返回的users
            List<Map<String, Object>> addOrUpdateUser = new ArrayList<>();
            List<Map<String, Object>> delUser = new ArrayList<>();
            // 最后返回的map
            Map<String, Object> data = new HashMap<>();
            // 对比上次的可用用户列表
            if (all || ObjectUtil.isEmpty(lastUserList)) {
                // 请求所有用户
                Iterator<PMPUser> iterator = userList.iterator();
                while (iterator.hasNext()) {
                    PMPUser user = iterator.next();
                    // 去除超出流量的用户
                    if (user.getTransferEnable() >= (user.getU() + user.getD())) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("id", user.getId());
                        map.put("passwd", user.getPasswd());
                        map.put("speedlimit", new BigDecimal(user.getNodeSpeedlimit()));
                        addOrUpdateUser.add(map);
                    }
                }
                lastUserList = userList;
            } else {
                // 筛选更新列表和删除列表
                for (PMPUser lastUser : lastUserList) {
                    Boolean delFlag = true;
                    for (PMPUser thisUser : userList) {
                        // id相同
                        if (ObjectUtil.equals(lastUser.getId(), thisUser.getId())) {
                            // 有其他的不同,加入更新列表
                            if (ObjectUtil.notEqual(thisUser.getPasswd(), lastUser.getPasswd()) || ObjectUtil.notEqual(thisUser.getPasswd(), lastUser.getPasswd()) || ObjectUtil.notEqual(thisUser.getNodeSpeedlimit(), lastUser.getNodeSpeedlimit())) {
                                Map<String, Object> map = new HashMap<>();
                                map.put("id", thisUser.getId());
                                map.put("passwd", thisUser.getPasswd());
                                map.put("speedlimit", new BigDecimal(thisUser.getNodeSpeedlimit()));
                                addOrUpdateUser.add(map);
                            }
                            delFlag = false;
                            break;
                        }
                    }
                    if (delFlag) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("id", lastUser.getId());
                        delUser.add(map);
                    }
                }
                data.put("addOrUpdate", addOrUpdateUser);
                data.put("delete", delUser);
                // 筛选新增列表
                for (PMPUser thisUser : userList) {
                    Boolean addFlag = true;
                    for (PMPUser lastUser : lastUserList) {
                        if (ObjectUtil.equals(thisUser.getId(), lastUser.getId())) {
                            addFlag = false;
                            break;
                        }
                    }
                    if (addFlag) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("id", thisUser.getId());
                        map.put("passwd", thisUser.getPasswd());
                        map.put("speedlimit", new BigDecimal(thisUser.getNodeSpeedlimit()));
                        addOrUpdateUser.add(map);
                    }
                }
            }
            data.put("addOrUpdate", addOrUpdateUser);
            lastUserList = userList;
            return Result.success().data(data);
        } catch (Exception e) {
            return Result.error();
        }
    }

    @Override
    public Result getRules(Map<String, Object> params) {
        try {
            String type = params.get("type").toString();
            Double tmpId = (Double) params.get("nodeId");
            Integer id = tmpId.intValue();

            // 根据type和id查询指定审计规则,如果存在,则返回,如果不存在,则返回所有审计规则
            List<PMPNodeWithDetect> rules = nodeWithDetectService.getByTypeAndNodeId(type, id);
            if (ObjectUtil.isNotEmpty(rules)) {
                // 根据rules的detect_list_id查找具体的rules
                return Result.success().data(detectListService.getDetectsByTypeAndNodeId(type, id));
            } else {
                return Result.success().data(detectListService.list());
            }
        } catch (Exception e) {
            return Result.error();
        }
    }

    @Override
    public Result postTraffic(Map<String, Object> params) {
        try {
            String type = params.get("type").toString();
            // 同步节点流量
            return getNodeService(type).postTraffic(params) ? Result.success() : Result.error();
        } catch (Exception e) {
            return Result.error();
        }
    }
}