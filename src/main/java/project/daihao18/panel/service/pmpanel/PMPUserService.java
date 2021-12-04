package project.daihao18.panel.service.pmpanel;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import project.daihao18.panel.entity.pmpanel.PMPUser;
import project.daihao18.panel.mapper.pmpanel.PMPUserMapper;
import project.daihao18.panel.service.commonService.UserService;

import java.util.Date;
import java.util.List;

/**
 * @ClassName: PMPUserService
 * @Description:
 * @Author: code18
 * @Date: 2020-10-07 21:29
 */
@Slf4j
@Service
public class PMPUserService extends ServiceImpl<PMPUserMapper, PMPUser> implements IService<PMPUser>, UserService<PMPUser> {

    @Override
    public List<PMPUser> getEnabledUsers(Integer nodeClass) {
        // 查可用用户列表
        QueryWrapper<PMPUser> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper
                .ge("class", nodeClass)
                .eq("enable", 1)
                .gt("expire_in", new Date())
                .gtSql("transfer_enable", "u+d")
                .or()
                .eq("is_admin", 1);
        return this.list(userQueryWrapper);
    }
}