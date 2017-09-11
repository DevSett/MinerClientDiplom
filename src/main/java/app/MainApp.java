package app;

import app.classes.configurations.MainConfig;
import lombok.Getter;
import lombok.extern.log4j.Log4j;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

@Log4j
@Component("mainApp")
public class MainApp {

    @Getter
    private static AnnotationConfigApplicationContext ctx;


    public static void main(String[] args) {
        log.info("Start bot");
        ctx = new AnnotationConfigApplicationContext(MainConfig.class);

        MainApp mainApp = (MainApp) ctx.getBean("mainApp");

        while (true) ;

    }
}
