package project.daihao18.panel.serviceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import project.daihao18.panel.entity.SsNodeInfo;
import project.daihao18.panel.mapper.SsNodeInfoMapper;
import project.daihao18.panel.service.SsNodeInfoService;

/**
 * @ClassName: SsNodeInfoServiceImpl
 * @Description:
 * @Author: code18
 * @Date: 2020-10-07 21:27
 */
@Service
@Slf4j
public class SsNodeInfoServiceImpl extends ServiceImpl<SsNodeInfoMapper, SsNodeInfo> implements SsNodeInfoService {
}