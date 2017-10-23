package app;

import app.classes.client.Client;
import app.classes.configurations.MainConfig;
import app.classes.models.Property;
import lombok.Getter;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

@Log4j
@Component("mainApp")
public class MainApp {

    @Getter
    private static AnnotationConfigApplicationContext ctx;

    @Autowired
    Client client;

    @Autowired
    Property property;

    public static void main(String[] args) {
        log.info("Start bot");
        ctx = new AnnotationConfigApplicationContext(MainConfig.class);
        MainApp mainApp = (MainApp) ctx.getBean("mainApp");
        ctx.getAutowireCapableBeanFactory().autowireBean(mainApp);

        Thread thread = new Thread(() -> {
            mainApp.client.start();
        });
        thread.start();
        Thread thread1 = new Thread(() -> {
            while (true) ;
        });
        thread1.start();


    }
}
