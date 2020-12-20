package project.daihao18.panel.controller;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import project.daihao18.panel.common.aop.Auth;
import project.daihao18.panel.common.response.Result;
import project.daihao18.panel.common.utils.FlowSizeConverterUtil;
import project.daihao18.panel.entity.*;
import project.daihao18.panel.service.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;

/**
 * @ClassName: WebAPIController
 * @Description:
 * @Author: code18
 * @Date: 2020-12-09 21:31
 */
@Slf4j
@RestController
@RequestMapping("/mod_mu")
public class WebAPIController {

    @Autowired
    private UserService userService;

    @Autowired
    private SsNodeService ssNodeService;

    @Autowired
    private DetectListService detectListService;

    @Autowired
    private UserTrafficLogService userTrafficLogService;

    @Autowired
    private SsNodeOnlineLogService ssNodeOnlineLogService;

    @Autowired
    private SsNodeInfoService ssNodeInfoService;

    @Autowired
    private AliveIpService aliveIpService;

    @Autowired
    private DetectLogService detectLogService;

    @Autowired
    private BlockipService blockipService;

    @Auth
    @GetMapping("/func/ping")
    public Result ping() {
        return Result.success().data("pong");
    }

    @Auth
    @GetMapping("/nodes/{id}/info")
    public Result getNodeInfoById(@PathVariable Integer id) {
        SsNode ssNode = ssNodeService.getById(id);
        if (ObjectUtil.isEmpty(ssNode)) {
            return Result.error();
        }
        Map<String, Object> map = new HashMap<>();
        map.put("node_group", ssNode.getNodeGroup());
        map.put("node_class", ssNode.getNodeClass());
        map.put("node_speedlimit", new BigDecimal(ssNode.getNodeSpeedlimit()));
        map.put("traffic_rate", new BigDecimal(ssNode.getTrafficRate()));
        map.put("mu_only", ssNode.getMuOnly());
        map.put("sort", ssNode.getSort());
        map.put("server", ssNode.getServer());
        if (ssNode.getSort() == 0) {
            // 如果是ss或ssr,在这里返回单端口的配置
            map.put("id", ssNode.getId());
            map.put("port", ssNode.getPort());
            map.put("passwd", ssNode.getPasswd());
            map.put("method", ssNode.getMethod());
            map.put("protocol", ssNode.getProtocol());
            map.put("obfs", ssNode.getObfs());
            map.put("is_multi_user", ssNode.getIsMultiUser());
        }
        return Result.success().data(map);
    }

    @Auth
    @GetMapping("/users")
    public Result getUserListByNodeId(HttpServletRequest request) {
        String nodeId = request.getParameter("node_id");
        if (ObjectUtil.isEmpty(nodeId)) {
            return Result.error();
        }
        Integer nodeid = Integer.parseInt(nodeId);
        SsNode ssNode = ssNodeService.getById(nodeid);
        Long now = new Date().getTime() / 1000;
        ssNode.setNodeHeartbeat(now.intValue());
        // 更新心跳
        ssNodeService.updateById(ssNode);
        // 需要返回的userList
        List<Map<String, Object>> data = new ArrayList<>();
        // 节点流量耗尽则返回 null
        if (ssNode.getNodeBandwidthLimit() != 0 && ssNode.getNodeBandwidthLimit() < ssNode.getNodeBandwidth()) {
            return Result.success().data(data);
        }
        // 查用户列表
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        if (ObjectUtil.equal(ssNode.getPort(), 0) && ssNode.getMuOnly() == 1) {
            userQueryWrapper
                    .ge("class", ssNode.getNodeClass())
                    .eq("node_group", ssNode.getNodeGroup())
                    .eq("is_multi_user", 0);
        } else {
            userQueryWrapper
                    .ge("class", ssNode.getNodeClass())
                    .eq("node_group", ssNode.getNodeGroup());
        }
        userQueryWrapper.or().eq("is_admin", 1);
        userQueryWrapper
                .eq("enable", 1)
                .gt("expire_in", new Date());
        List<User> userList = userService.list(userQueryWrapper);
        List<Map<String, Object>> users = new ArrayList<>();
        Iterator<User> iterator = userList.iterator();
        while (iterator.hasNext()) {
            User user = iterator.next();
            // 去除超出流量的用户
            if (user.getTransferEnable() >= (user.getU() + user.getD())) {
                Map<String, Object> map = new HashMap<>();
                map.put("id", user.getId());
                map.put("email", user.getEmail());
                map.put("passwd", user.getPasswd());
                map.put("u", user.getU());
                map.put("d", user.getD());
                map.put("port", user.getPort());
                map.put("method", user.getMethod());
                map.put("node_speedlimit", new BigDecimal(user.getNodeSpeedlimit()));
                map.put("node_connector", user.getNodeConnector());
                map.put("protocol", user.getProtocol());
                map.put("protocol_param", user.getProtocolParam());
                map.put("obfs", user.getObfs());
                map.put("obfs_param", user.getObfsParam());
                if (ObjectUtil.isNotEmpty(user.getForbiddenIp())) {
                    map.put("forbidden_ip", user.getForbiddenIp());
                } else {
                    map.put("forbidden_ip", "");
                }
                if (ObjectUtil.isNotEmpty(user.getForbiddenPort())) {
                    map.put("forbidden_port", user.getForbiddenPort());
                } else {
                    map.put("forbidden_port", "");
                }
                if (ObjectUtil.isNotEmpty(user.getDisconnectIp())) {
                    map.put("disconnect_ip", user.getDisconnectIp());
                } else {
                    map.put("disconnect_ip", "");
                }
                map.put("is_multi_user", user.getIsMultiUser());
                map.put("uuid", user.getUuid());
                users.add(map);
            }
        }
        return Result.success().data(users);
    }

    @Auth
    @GetMapping("/nodes")
    public Result getAllNodes() {
        QueryWrapper<SsNode> ssNodeQueryWrapper = new QueryWrapper<>();
        ssNodeQueryWrapper
                .ne("node_ip", null)
                .or()
                .ne("node_ip", "");
        ssNodeQueryWrapper
                .eq("sort", 0)
                .or()
                .eq("sort", 10)
                .or()
                .eq("sort", 12)
                .or()
                .eq("sort", 13);
        return Result.success().data(ssNodeService.list(ssNodeQueryWrapper));
    }

    @Auth
    @GetMapping("/func/detect_rules")
    public Result getDetectsByNodeId(HttpServletRequest request) {
        String nodeId = request.getParameter("node_id");
        if (ObjectUtil.isEmpty(nodeId)) {
            return Result.success().data(detectListService.list());
        }
        Integer nodeid = Integer.parseInt(nodeId);
        List<DetectList> data = detectListService.getDetectsByNodeId(nodeid);
        if (ObjectUtil.isEmpty(data)) {
            return Result.success().data(detectListService.list());
        }
        return Result.success().data(data);
    }

    @Auth
    @PostMapping("/users/traffic")
    public Result postUserTraffic(HttpServletRequest request, @RequestBody Map<String, Object> map) {
        String nodeId = request.getParameter("node_id");
        List<Map<String, Object>> listData = (List<Map<String, Object>>) map.get("data");
        // log.info("/users/traffic\nnode_id:{}\ndata:{}", nodeId, listData);
        Long thisTimeBandwidth = 0L;
        if (ObjectUtil.isEmpty(nodeId)) {
            return Result.error();
        }
        // 查该节点
        SsNode ssNode = ssNodeService.getById(Integer.parseInt(nodeId));
        if (ObjectUtil.isEmpty(ssNode)) {
            return Result.error();
        }
        List<User> userList = new ArrayList<>();
        List<UserTrafficLog> userTrafficLogList = new ArrayList<>();
        Long now = new Date().getTime() / 1000;
        if (ObjectUtil.isNotEmpty(listData) && listData.size() > 0) {
            Iterator<Map<String, Object>> iterator = listData.iterator();
            while (iterator.hasNext()) {
                Map<String, Object> traffic = iterator.next();
                Double uid = Double.parseDouble(traffic.get("user_id").toString());
                User user = userService.getById(uid.intValue());
                user.setT(now);
                String u = FlowSizeConverterUtil.convertNumber(traffic.get("u").toString());
                String d = FlowSizeConverterUtil.convertNumber(traffic.get("d").toString());
                user.setU(user.getU() + Long.parseLong(u) * ssNode.getTrafficRate().longValue());
                user.setD(user.getD() + Long.parseLong(d) * ssNode.getTrafficRate().longValue());
                thisTimeBandwidth += Long.parseLong(u) * ssNode.getTrafficRate().longValue() + Long.parseLong(d) * ssNode.getTrafficRate().longValue();
                userList.add(user);
                // 新增TrafficLog
                UserTrafficLog userTrafficLog = new UserTrafficLog();
                userTrafficLog.setUserId(user.getId());
                userTrafficLog.setU(Integer.parseInt(String.valueOf(Long.parseLong(u))));
                userTrafficLog.setD(Integer.parseInt(String.valueOf(Long.parseLong(d))));
                userTrafficLog.setNodeId(ssNode.getId());
                userTrafficLog.setRate(ssNode.getTrafficRate());
                userTrafficLog.setTraffic(FlowSizeConverterUtil.BytesConverter(Long.parseLong(String.valueOf(Long.parseLong(u) + Long.parseLong(d)))));
                userTrafficLog.setLogTime(now.intValue());
                userTrafficLogList.add(userTrafficLog);
            }
            // 批量保存
            userService.updateBatchById(userList);
            userTrafficLogService.saveBatch(userTrafficLogList);
            // 给节点增加流量使用
            ssNode.setNodeBandwidth(ssNode.getNodeBandwidth() + thisTimeBandwidth);
            ssNodeService.updateById(ssNode);
            SsNodeOnlineLog ssNodeOnlineLog = new SsNodeOnlineLog();
            ssNodeOnlineLog.setNodeId(ssNode.getId());
            ssNodeOnlineLog.setOnlineUser(listData.size());
            ssNodeOnlineLog.setLogTime(now.intValue());
            ssNodeOnlineLogService.save(ssNodeOnlineLog);
        }
        return Result.success().data("ok");
    }

    @Auth
    @PostMapping("/nodes/{id}/info")
    public Result postNodeInfo(HttpServletRequest request, @RequestBody Map<String, Object> data) {
        String nodeId = request.getParameter("node_id");
        if (ObjectUtil.isEmpty(nodeId)) {
            return Result.error();
        }
        // log.info("/nodes/{}/info\nnode_id:{}\ndata:{}", nodeId, nodeId, data);
        SsNodeInfo ssNodeInfo = new SsNodeInfo();
        Long now = new Date().getTime() / 1000;
        if (ObjectUtil.isNotEmpty(data)) {
            String load = data.get("load").toString();
            String uptime = data.get("uptime").toString();
            ssNodeInfo.setNodeId(Integer.parseInt(nodeId));
            ssNodeInfo.setLoad(load);
            ssNodeInfo.setUptime(Double.parseDouble(uptime));
            ssNodeInfo.setLogTime(now.intValue());
        }

        return ssNodeInfoService.save(ssNodeInfo) ? Result.success().data("ok") : Result.error();
    }

    @Auth
    @PostMapping("/users/aliveip")
    public Result postAliveIP(HttpServletRequest request, @RequestBody Map<String, Object> map) {
        String nodeId = request.getParameter("node_id");
        List<Map<String, Object>> listData = (List<Map<String, Object>>) map.get("data");
        // log.info("/users/aliveip\nnode_id:{}\ndata:{}", nodeId, listData);
        if (ObjectUtil.isEmpty(nodeId)) {
            return Result.error();
        }
        // 查该节点
        SsNode ssNode = ssNodeService.getById(Integer.parseInt(nodeId));
        if (ObjectUtil.isEmpty(ssNode)) {
            return Result.error();
        }
        List<AliveIp> list = new ArrayList<>();
        Long now = new Date().getTime() / 1000;
        if (ObjectUtil.isNotEmpty(listData) && listData.size() > 0) {
            Iterator<Map<String, Object>> iterator = listData.iterator();
            while (iterator.hasNext()) {
                Map<String, Object> item = iterator.next();
                AliveIp aliveIp = new AliveIp();
                aliveIp.setNodeid(ssNode.getId());
                Double tmpId = Double.parseDouble(item.get("user_id").toString());
                aliveIp.setUserid(tmpId.intValue());
                aliveIp.setIp(item.get("ip").toString());
                aliveIp.setDatetime(now.intValue());
                list.add(aliveIp);
            }
        }
        // 批量保存
        if (ObjectUtil.isNotEmpty(list) && list.size() > 0) {
            // 先删除之前的
            UpdateWrapper<AliveIp> deleteLastRecord = new UpdateWrapper<>();
            deleteLastRecord.eq("nodeid", ssNode.getId());
            aliveIpService.remove(deleteLastRecord);
            return aliveIpService.saveBatch(list) ? Result.success().data("ok") : Result.error();
        } else {
            return Result.success().data("ok");
        }
    }

    @Auth
    @PostMapping("/users/detectlog")
    public Result postDetectLog(HttpServletRequest request, @RequestBody Map<String, Object> map) {
        String nodeId = request.getParameter("node_id");
        List<Map<String, Object>> listData = (List<Map<String, Object>>) map.get("data");
        // log.info("/users/detectlog\nnode_id:{}\ndata:{}", nodeId, listData);
        if (ObjectUtil.isEmpty(nodeId)) {
            return Result.error();
        }
        // 查该节点
        SsNode ssNode = ssNodeService.getById(Integer.parseInt(nodeId));
        if (ObjectUtil.isEmpty(ssNode)) {
            return Result.error();
        }
        List<DetectLog> list = new ArrayList<>();
        Long now = new Date().getTime() / 1000;
        if (ObjectUtil.isNotEmpty(listData) && listData.size() > 0) {
            Iterator<Map<String, Object>> iterator = listData.iterator();
            while (iterator.hasNext()) {
                Map<String, Object> item = iterator.next();
                DetectLog detectLog = new DetectLog();
                detectLog.setNodeId(ssNode.getId());
                Double tmpUserId = Double.parseDouble(item.get("user_id").toString());
                detectLog.setUserId(tmpUserId.intValue());
                Double tmpListId = Double.parseDouble(item.get("list_id").toString());
                detectLog.setListId(tmpListId.intValue());
                detectLog.setDatetime(now.intValue());
                list.add(detectLog);
            }
        }
        // 批量保存
        if (ObjectUtil.isNotEmpty(list) && list.size() > 0) {
            return detectLogService.saveBatch(list) ? Result.success().data("ok") : Result.error();
        } else {
            return Result.success().data("ok");
        }
    }

    @Auth
    @PostMapping("/func/block_ip")
    public Result blockIp(HttpServletRequest request, @RequestBody Map<String, Object> map) {
        String nodeId = request.getParameter("node_id");
        List<Map<String, String>> listData = (List<Map<String, String>>) map.get("data");
        // log.info("/users/block_ips\nnode_id:{}\ndata:{}", nodeId, listData);
        if (ObjectUtil.isEmpty(nodeId)) {
            return Result.error();
        }
        Integer nodeid = Integer.parseInt(nodeId);
        List<Blockip> list = new ArrayList<>();
        if (ObjectUtil.isNotEmpty(listData) && listData.size() > 0) {
            Iterator<Map<String, String>> iterator = listData.iterator();
            while (iterator.hasNext()) {
                Map<String, String> blockIp = iterator.next();
                Blockip blockip = new Blockip();
                blockip.setNodeid(nodeid);
                blockip.setIp(blockIp.get("ip"));
                list.add(blockip);
            }
        }
        if (ObjectUtil.isNotEmpty(list) && list.size() > 0) {
            return blockipService.saveBatch(list) ? Result.success().data("ok") : Result.error();
        } else {
            return Result.success().data("ok");
        }
    }
}