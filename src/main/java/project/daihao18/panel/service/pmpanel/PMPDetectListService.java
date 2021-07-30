package project.daihao18.panel.service.pmpanel;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.daihao18.panel.entity.pmpanel.PMPDetectList;
import project.daihao18.panel.mapper.pmpanel.PMPDetectListMapper;

import java.util.List;

/**
 * @ClassName: PMPDetectListService
 * @Description:
 * @Author: code18
 * @Date: 2020-10-07 21:25
 */
@Service
public class PMPDetectListService extends ServiceImpl<PMPDetectListMapper, PMPDetectList> {

    @Autowired
    private PMPDetectListMapper PMPDetectListMapper;

    public List<PMPDetectList> getDetectsByTypeAndNodeId(String type, Integer id) {
        return PMPDetectListMapper.getDetectsByTypeAndNodeId(type, id);
    }
}