package project.daihao18.panel.serviceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.daihao18.panel.entity.DetectList;
import project.daihao18.panel.mapper.DetectListMapper;
import project.daihao18.panel.service.DetectListService;

import java.util.List;

/**
 * @ClassName: DetectListServiceImpl
 * @Description:
 * @Author: code18
 * @Date: 2020-10-07 21:25
 */
@Service
public class DetectListServiceImpl extends ServiceImpl<DetectListMapper, DetectList> implements DetectListService {

    @Autowired
    private DetectListMapper detectListMapper;

    @Override
    public List<DetectList> getDetectsByNodeId(Integer nodeid) {
        return detectListMapper.getDetectsByNodeId(nodeid);
    }
}