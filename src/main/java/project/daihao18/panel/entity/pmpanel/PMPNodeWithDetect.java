package project.daihao18.panel.entity.pmpanel;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @ClassName: PMPNodeWithDetect
 * @Description:
 * @Author: code18
 * @Date: 2020-10-07 21:06
 */
@Data
@ToString
@TableName(value = "node_with_detect")
public class PMPNodeWithDetect implements Serializable {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String type;

    private Integer nodeId;

    private Integer detectListId;

    private static final long serialVersionUID = 1L;
}