package project.daihao18.panel.entity.pmpanel;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @ClassName: PMPV2ray
 * @Description:
 * @Author: code18 
 * @Date: 2020-10-07 21:06
 */
@Data
@ToString
@TableName(value = "v2ray")
public class PMPV2ray extends PMPNode implements Serializable {

    private Integer alterId;

    private String network;

    private String security;

    private String host;

    private String path;

    private String sni;

    private static final long serialVersionUID = 1L;
}