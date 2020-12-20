package project.daihao18.panel.serviceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import project.daihao18.panel.entity.User;
import project.daihao18.panel.mapper.UserMapper;
import project.daihao18.panel.service.UserService;

/**
 * @ClassName: UserServiceImpl
 * @Description:
 * @Author: code18
 * @Date: 2020-10-07 21:29
 */
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}