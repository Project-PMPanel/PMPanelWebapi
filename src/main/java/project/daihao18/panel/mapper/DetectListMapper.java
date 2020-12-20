package project.daihao18.panel.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import project.daihao18.panel.entity.DetectList;

import java.util.List;

@Mapper
public interface DetectListMapper extends BaseMapper<DetectList> {

    @Select("select id, regex, type FROM detect_list a, (SELECT detect_list_id FROM node_with_detect WHERE node_id = #{nodeid}) b WHERE a.id = b.detect_list_id")
    List<DetectList> getDetectsByNodeId(@Param("nodeid") Integer nodeid);
}