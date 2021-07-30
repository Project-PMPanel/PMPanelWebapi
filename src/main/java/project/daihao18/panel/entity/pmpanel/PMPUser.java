package project.daihao18.panel.entity.pmpanel;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @ClassName: PMPUser
 * @Description:
 * @Author: code18
 * @Date: 2020-10-07 21:07
 */
@Data
@ToString
@NoArgsConstructor
@TableName(value = "user")
public class PMPUser implements Serializable {

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 用户等级
     */
    @TableField("`class`")
    private Integer clazz;

    /**
     * 是否启用
     */
    @TableField("`enable`")
    private Boolean enable;

    /**
     * 过期时间
     */
    private Date expireIn;

    /**
     * 上次使用时间
     */
    private Long t;

    /**
     * 上传流量
     */
    private Long u;

    /**
     * 下载流量
     */
    private Long d;

    /**
     * 过去已用流量
     */
    private Long p;

    /**
     * 全部可用流量
     */
    private Long transferEnable;

    /**
     * 节点链接密码
     */
    private String passwd;

    /**
     * 节点限速 mbps
     */
    private Double nodeSpeedlimit;

    /**
     * 链接ip数
     */
    private Integer nodeConnector;

    /**
     * 节点组
     */
    private Integer nodeGroup;

    /**
     * 上次使用时间
     */
    private Date lastUsedDate;

    /**
     * 是否是管理员
     */
    private Integer isAdmin;

    // 以下字段非数据库字段


    private static final long serialVersionUID = 1L;
}