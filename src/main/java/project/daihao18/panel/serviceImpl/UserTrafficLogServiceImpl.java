package project.daihao18.panel.serviceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import project.daihao18.panel.entity.UserTrafficLog;
import project.daihao18.panel.mapper.UserTrafficLogMapper;
import project.daihao18.panel.service.UserTrafficLogService;

/**
 * @ClassName: UserTrafficLogServiceImpl
 * @Description:
 * @Author: code18
 * @Date: 2020-10-07 21:30
 */
@Service
@Slf4j
public class UserTrafficLogServiceImpl extends ServiceImpl<UserTrafficLogMapper, UserTrafficLog> implements UserTrafficLogService {
}