package project.daihao18.panel.common.aop;

import cn.hutool.core.util.ObjectUtil;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: AuthAspect
 * @Description:
 * @Author: code18
 * @Date: 2020-12-09 22:00
 */
@Aspect
@Slf4j
@Component
public class AuthAspect {

    @Value("${key}")
    private String muKey;

    @Autowired
    private Gson gson;

    @Pointcut(value = "@annotation(project.daihao18.panel.common.aop.Auth)")
    public void pointCut() {
    }

    //环绕通知
    @Around("@annotation(auth)")
    public Object around(ProceedingJoinPoint pj, Auth auth) {

        Object result = null;
        Object classMethod = pj.getSignature();

        try {
            //前置通知,方法执行前执行
            //获取request
            HttpServletRequest request =
                    ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            //获取response
            HttpServletResponse response =
                    ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
            // 服务器端的key
            if (ObjectUtil.isEmpty(muKey)) {
                // webapi的key为null
                Map<String, Object> map = new HashMap<>();
                map.put("ret", 400);
                map.put("data", "Server Key is null.");
                response.setContentType("application/json;charset=UTF-8");
                PrintWriter out = response.getWriter();
                out.write(gson.toJson(map));
                return null;
            }
            // 请求带的muKey
            String key = request.getHeader("key");
            if (ObjectUtil.isEmpty(key)) {
                // key为null
                Map<String, Object> map = new HashMap<>();
                map.put("ret", 400);
                map.put("data", "Your key is null.");
                response.setContentType("application/json;charset=UTF-8");
                PrintWriter out = response.getWriter();
                out.write(gson.toJson(map));
                return null;
            }
            if (ObjectUtil.notEqual(muKey, key)) {
                // key非法
                Map<String, Object> map = new HashMap<>();
                map.put("ret", 400);
                map.put("data", "APIKey is invalid.");
                response.setContentType("application/json;charset=UTF-8");
                PrintWriter out = response.getWriter();
                out.write(gson.toJson(map));
                out.close();
                return null;
            }
            //执行注解方法
            result = pj.proceed();

            //返回通知,方法正确执行后执行
            //TODO
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            //异常通知,方法出现异常时执行
            log.error(classMethod + "执行出现异常:" + throwable.getMessage());
            //TODO
        }
        //后置通知,方法执行后不管方法出不出异常,都执行
        //TODO
        return result;
    }
}