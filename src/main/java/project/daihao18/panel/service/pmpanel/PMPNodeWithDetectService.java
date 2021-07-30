package project.daihao18.panel.service.pmpanel;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import project.daihao18.panel.entity.pmpanel.PMPNodeWithDetect;
import project.daihao18.panel.mapper.pmpanel.PMPNodeWithDetectMapper;

import java.util.List;

/**
 * @ClassName: PMPNodeWithDetectService
 * @Description:
 * @Author: code18
 * @Date: 2020-10-07 21:22
 */
@Service
public class PMPNodeWithDetectService extends ServiceImpl<PMPNodeWithDetectMapper, PMPNodeWithDetect> {

    public List<PMPNodeWithDetect> getByTypeAndNodeId(String type, Integer id) {
        QueryWrapper<PMPNodeWithDetect> nodeWithDetectQueryWrapper = new QueryWrapper<>();
        nodeWithDetectQueryWrapper
                .eq("type", type)
                .eq("node_id", id);
        return this.list(nodeWithDetectQueryWrapper);
    }
}