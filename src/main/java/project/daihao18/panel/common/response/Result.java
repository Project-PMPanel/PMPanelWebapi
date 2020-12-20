package project.daihao18.panel.common.response;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Data
public class Result implements Serializable {
    private Integer ret;

    private Object data = new Object();

    // 构造器私有
    private Result() {
    }

    // 通用返回成功
    public static Result success() {
        Result r = new Result();
        r.setRet(1);
        return r;
    }

    // 通用返回失败，未知错误
    public static Result error() {
        Result r = new Result();
        r.setRet(0);
        return r;
    }

    /**
     * ------------使用链式编程，返回类本身-----------
     **/

    // 自定义返回数据
    public Result data(Map<String, Object> map) {
        this.setData(map);
        return this;
    }

    // 通用设置data
    public Result data(String key, Object value) {
        Map<String, Object> map = new HashMap<>();
        map.put(key, value);
        return this.data(map);
    }

    // 通用设置data
    public Result data(Object value) {
        this.data = value;
        return this;
    }

    // 自定义状态码
    public Result code(Integer code) {
        this.setRet(code);
        return this;
    }
}
