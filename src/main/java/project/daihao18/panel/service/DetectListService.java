package project.daihao18.panel.service;

import com.baomidou.mybatisplus.extension.service.IService;
import project.daihao18.panel.entity.DetectList;

import java.util.List;

/**
 * @InterfaceName: DetectListService
 * @Description:
 * @Author: code18
 * @Date: 2020-10-07 21:16
 */
public interface DetectListService extends IService<DetectList> {
    List<DetectList> getDetectsByNodeId(Integer nodeid);
}