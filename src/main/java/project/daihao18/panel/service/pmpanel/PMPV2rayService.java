package project.daihao18.panel.service.pmpanel;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.daihao18.panel.common.utils.FlowSizeConverterUtil;
import project.daihao18.panel.entity.pmpanel.*;
import project.daihao18.panel.mapper.pmpanel.PMPV2rayMapper;
import project.daihao18.panel.service.commonService.NodeService;

import java.util.*;

/**
 * @ClassName: PMPV2rayService
 * @Description:
 * @Author: code18
 * @Date: 2020-10-07 21:22
 */
@Service
public class PMPV2rayService extends ServiceImpl<PMPV2rayMapper, PMPV2ray> implements IService<PMPV2ray>, NodeService<PMPV2ray> {

    @Autowired
    private PMPUserService userService;

    @Autowired
    private PMPUserTrafficLogService PMPUserTrafficLogService;

    @Override
    public PMPV2ray getById(Integer id) {
        return super.getById(id);
    }

    @Override
    public boolean postTraffic(Map<String, Object> params) {
        try {
            Double tmpId = Double.valueOf(params.get("nodeId").toString());
            Integer id = tmpId.intValue();

            // 查该节点
            PMPV2ray PMPV2Ray = this.getById(id);
            if (ObjectUtil.isEmpty(PMPV2Ray)) {
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
                    u.setU(u.getU() + Long.parseLong(up) * PMPV2Ray.getTrafficRate().longValue());
                    u.setD(u.getD() + Long.parseLong(down) * PMPV2Ray.getTrafficRate().longValue());
                    thisTimeUpload += Long.parseLong(up) * PMPV2Ray.getTrafficRate().longValue();
                    thisTimeDownload += Long.parseLong(down) * PMPV2Ray.getTrafficRate().longValue();
                    u.setLastUsedDate(dateNow);
                    userList.add(u);
                    // 新增TrafficLog
                    PMPUserTrafficLog userTrafficLog = new PMPUserTrafficLog();
                    userTrafficLog.setUserId(u.getId());
                    userTrafficLog.setU(Integer.parseInt(String.valueOf(Long.parseLong(up))));
                    userTrafficLog.setD(Integer.parseInt(String.valueOf(Long.parseLong(down))));
                    userTrafficLog.setType("v2ray");
                    userTrafficLog.setNodeId(PMPV2Ray.getId());
                    userTrafficLog.setRate(PMPV2Ray.getTrafficRate());
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
        return this.updateById((PMPV2ray) node);
    }
}