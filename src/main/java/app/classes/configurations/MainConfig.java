package app.classes.configurations;

import app.MainApp;
import app.classes.client.Client;
import app.classes.models.Property;
import app.classes.services.ServiceGpu;
import app.classes.services.ShutdownServices;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Сергей on 05.07.2017.
 */
@Configuration
public class MainConfig {

    @Bean
    public Property property() {
        return new Property();
    }

    @Bean
    public MainApp mainApp() {
        return new MainApp();
    }

    @Bean
    public Client client(){return new Client();}

    @Bean
    public ServiceGpu serviceGpu() {return new ServiceGpu();}

    @Bean
    public ShutdownServices shutdownServices(){return new ShutdownServices();}
}
