package project.daihao18.panel.service.commonService;

import java.util.List;

public interface UserService<T> {

    List<T> getEnabledUsers(Integer nodeClass);
}
