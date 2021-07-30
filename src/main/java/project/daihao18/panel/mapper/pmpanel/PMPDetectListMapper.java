package project.daihao18.panel.mapper.pmpanel;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import project.daihao18.panel.entity.pmpanel.PMPDetectList;

import java.util.List;

@Mapper
public interface PMPDetectListMapper extends BaseMapper<PMPDetectList> {

    @Select("select id, regex, type FROM detect_list a, (SELECT detect_list_id FROM node_with_detect WHERE type = #{type} AND node_id = #{id}) b WHERE a.id = b.detect_list_id")
    List<PMPDetectList> getDetectsByTypeAndNodeId(@Param("type") String type, @Param("id") Integer id);
}