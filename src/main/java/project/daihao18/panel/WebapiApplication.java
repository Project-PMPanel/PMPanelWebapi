package project.daihao18.panel;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import project.daihao18.panel.service.commonService.PanelService;
import project.daihao18.panel.service.pmpanel.PMPanelService;

@Slf4j
@MapperScan(basePackages = {"project.daihao18.panel.mapper"})
@EnableTransactionManagement
@SpringBootApplication(exclude = {JacksonAutoConfiguration.class})
public class WebapiApplication {

    @Getter
    public static String panel;

    public static PanelService panelService;

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(WebapiApplication.class, args);
        Environment environment = context.getBean(Environment.class);
        panel = environment.getProperty("panel");
        log.info("Start webapi for " + panel);
        switch (panel) {
            case "pmpanel":
                panelService = new PMPanelService();
                break;
            default:
                log.info("Wrong panel type: " + panel);
                SpringApplication.exit(context);
        }
    }

}
