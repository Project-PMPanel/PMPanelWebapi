package project.daihao18.panel.service.pmpanel;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.daihao18.panel.common.utils.FlowSizeConverterUtil;
import project.daihao18.panel.entity.pmpanel.PMPNode;
import project.daihao18.panel.entity.pmpanel.PMPSs;
import project.daihao18.panel.entity.pmpanel.PMPUser;
import project.daihao18.panel.entity.pmpanel.PMPUserTrafficLog;
import project.daihao18.panel.mapper.pmpanel.PMPSsMapper;
import project.daihao18.panel.service.commonService.NodeService;

import java.util.*;

/**
 * @ClassName: PMPSsService
 * @Description:
 * @Author: code18
 * @Date: 2020-10-07 21:22
 */
@Service
public class PMPSsService extends ServiceImpl<PMPSsMapper, PMPSs> implements IService<PMPSs>, NodeService<PMPSs> {

    @Autowired
    private PMPUserService userService;

    @Autowired
    private PMPUserTrafficLogService PMPUserTrafficLogService;

    @Override
    public PMPSs getById(Integer id) {
        return super.getById(id);
    }

    @Override
    public boolean postTraffic(Map<String, Object> params) {
        try {
            Double tmpId = Double.valueOf(params.get("nodeId").toString());
            Integer id = tmpId.intValue();

            // 查该节点
            PMPSs PMPSs = this.getById(id);
            if (ObjectUtil.isEmpty(PMPSs)) {
                return false;
            }

            List<PMPUser> userList = new ArrayList<>();
            List<PMPUserTrafficLog> userTrafficLogList = new ArrayList<>();
            Date dateNow = new Date();
            Long now = dateNow.getTime() / 1000;

            List<Map<String, Object>> users = (List<Map<String, Object>>) params.get("users");
            Long thisTimeUpload = 0L;
            Long thisTimeDownload = 0L;

            if (ObjectUtil.isNotEmpty(users) && users.size() > 0) {
                Iterator<Map<String, Object>> iterator = users.iterator();
                while (iterator.hasNext()) {
                    Map<String, Object> user = iterator.next();
                    Double uid = Double.parseDouble(user.get("id").toString());
                    PMPUser u = userService.getById(uid.intValue());
                    u.setT(now);
                    String up = FlowSizeConverterUtil.convertNumber(user.get("up").toString());
                    String down = FlowSizeConverterUtil.convertNumber(user.get("down").toString());
                    u.setU(u.getU() + Long.parseLong(up) * PMPSs.getTrafficRate().longValue());
                    u.setD(u.getD() + Long.parseLong(down) * PMPSs.getTrafficRate().longValue());
                    thisTimeUpload += Long.parseLong(up) * PMPSs.getTrafficRate().longValue();
                    thisTimeDownload += Long.parseLong(down) * PMPSs.getTrafficRate().longValue();
                    u.setLastUsedDate(dateNow);
                    userList.add(u);
                    // 新增TrafficLog
                    PMPUserTrafficLog userTrafficLog = new PMPUserTrafficLog();
                    userTrafficLog.setUserId(u.getId());
                    userTrafficLog.setU(Integer.parseInt(String.valueOf(Long.parseLong(up))));
                    userTrafficLog.setD(Integer.parseInt(String.valueOf(Long.parseLong(down))));
                    userTrafficLog.setType("ss");
                    userTrafficLog.setNodeId(PMPSs.getId());
                    userTrafficLog.setRate(PMPSs.getTrafficRate());
                    userTrafficLog.setTraffic(FlowSizeConverterUtil.BytesConverter(Long.parseLong(String.valueOf(Long.parseLong(up) + Long.parseLong(down)))));
                    userTrafficLog.setIp(user.get("ip").toString());
                    userTrafficLog.setLogTime(now.intValue());
                    userTrafficLogList.add(userTrafficLog);
                }
                // 批量保存
                userService.updateBatchById(userList);
                PMPUserTrafficLogService.saveBatch(userTrafficLogList);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean updateHeartBeat(PMPNode node) {
        node.setHeartbeat(new Date());
        return this.updateById((PMPSs) node);
    }
}