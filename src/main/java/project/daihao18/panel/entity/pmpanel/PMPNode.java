package project.daihao18.panel.entity.pmpanel;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName: PMPNode
 * @Description:
 * @Author: code18 
 * @Date: 2020-10-07 21:06
 */
@Data
@ToString
public class PMPNode implements Serializable {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String name;

    private String outServer;

    private Integer outPort;

    private String subServer;

    private Integer subPort;

    private Double trafficRate;

    @TableField("`class`")
    private Integer clazz;

    private Integer speedlimit;

    private Date heartbeat;

    private boolean flag;

    private static final long serialVersionUID = 1L;
}