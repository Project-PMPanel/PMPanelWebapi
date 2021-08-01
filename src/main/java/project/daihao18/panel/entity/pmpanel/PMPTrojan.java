package project.daihao18.panel.entity.pmpanel;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @ClassName: PMPTrojan
 * @Description:
 * @Author: code18 
 * @Date: 2020-10-07 21:06
 */
@Data
@ToString
@TableName(value = "trojan")
public class PMPTrojan extends PMPNode implements Serializable {

    private Boolean grpc;

    private String sni;

    private static final long serialVersionUID = 1L;
}