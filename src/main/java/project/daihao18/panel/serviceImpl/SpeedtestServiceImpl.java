package project.daihao18.panel.serviceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import project.daihao18.panel.entity.Speedtest;
import project.daihao18.panel.mapper.SpeedtestMapper;
import project.daihao18.panel.service.SpeedtestService;

/**
 * @ClassName: SpeedtestServiceImpl
 * @Description:
 * @Author: code18
 * @Date: 2020-10-07 21:27
 */
@Service
@Slf4j
public class SpeedtestServiceImpl extends ServiceImpl<SpeedtestMapper, Speedtest> implements SpeedtestService {
}