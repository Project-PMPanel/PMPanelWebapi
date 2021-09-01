package project.daihao18.panel.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import project.daihao18.panel.common.aop.Auth;
import project.daihao18.panel.common.response.Result;
import project.daihao18.panel.service.*;

import java.util.*;

/**
 * @ClassName: APIController
 * @Description:
 * @Author: code18
 * @Date: 2020-12-09 21:31
 */
@Slf4j
@RestController
@RequestMapping("/api")
public class APIController {

    @Autowired
    private APIService apiService;

    @Auth
    @GetMapping("/ping")
    public Result ping() {
        return Result.success().data("pong");
    }

    @Auth
    @GetMapping("/node")
    public Result getNode(@RequestParam Map<String, Object> params) {
        return apiService.getNode(params);
    }

    @Auth
    @GetMapping("/users")
    public Result getUsers(@RequestParam Map<String, Object> params) {
        return apiService.getUsers(params);
    }

    @Auth
    @GetMapping("/rules")
    public Result getRules(@RequestParam Map<String, Object> params) {
        return apiService.getRules(params);
    }

    @Auth
    @PostMapping("/online")
    public Result postOnline(@RequestBody Map<String, Object> params) {
        return apiService.postOnline(params);
    }

    @Auth
    @PostMapping("/traffic")
    public Result postTraffic(@RequestBody Map<String, Object> params) {
        return apiService.postTraffic(params);
    }
}