package project.daihao18.panel.entity;

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
 * @ClassName: User
 * @Description:
 * @Author: code18
 * @Date: 2020-10-07 21:07
 */
@Data
@ToString
@NoArgsConstructor
@TableName(value = "user")
public class User implements Serializable {

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 登陆密码
     */
    @TableField("`password`")
    private String password;

    /**
     * 余额
     */
    private BigDecimal money;

    /**
     * 邀请码
     */
    private String inviteCode;

    /**
     * 邀请数
     */
    private Integer inviteCount;

    /**
     * 是否循环返利
     */
    private Boolean inviteCycleEnable;

    /**
     * 循环返利百分比
     */
    private BigDecimal inviteCycleRate;

    /**
     * 邀请人id
     */
    private Integer parentId;

    /**
     * 订阅标志
     */
    private String link;

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
     * v2ray流量上报需要的无用字段
     */
    private Integer expireTime;

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
     * 端口
     */
    @TableField("`port`")
    private Integer port;

    /**
     * 节点链接密码
     */
    private String passwd;

    /**
     * 加密方式
     */
    private String method;

    /**
     * 协议
     */
    private String protocol;

    /**
     * 协议参数
     */
    private String protocolParam;

    /**
     * 混淆
     */
    private String obfs;

    /**
     * 混淆参数
     */
    private String obfsParam;

    /**
     * v2ray的uuid
     */
    private String uuid;

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

    private String forbiddenIp;

    private String forbiddenPort;

    private String disconnectIp;

    private Integer isMultiUser;

    // 以下字段非数据库字段

    /**
     * 所有已用流量 u+d  gb
     *
     * @TableField(exist = false)
     * private Double allUsedGb;
     */

    private static final long serialVersionUID = 1L;
}