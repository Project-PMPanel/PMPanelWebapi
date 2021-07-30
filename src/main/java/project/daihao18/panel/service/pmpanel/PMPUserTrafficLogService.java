package project.daihao18.panel.service.pmpanel;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import project.daihao18.panel.entity.pmpanel.PMPUserTrafficLog;
import project.daihao18.panel.mapper.pmpanel.PMPUserTrafficLogMapper;

/**
 * @ClassName: PMPUserTrafficLogService
 * @Description:
 * @Author: code18
 * @Date: 2020-10-07 21:30
 */
@Service
@Slf4j
public class PMPUserTrafficLogService extends ServiceImpl<PMPUserTrafficLogMapper, PMPUserTrafficLog> implements IService<PMPUserTrafficLog> {
}