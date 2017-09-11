package app.classes.models;

import lombok.Cleanup;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.Properties;

/**
 * Created by killsett on 13.06.17.
 */
@Log4j
@Component("property")
public class Property {

    @Getter
    @Setter
    private String url;

    @Setter
    @Getter
    private String pathToLog;

    //    private String gpuLogName;
    public Property() {
        //do not use
    }

    @PostConstruct
    public void init() {
        @Cleanup Properties prop = new Properties();
        @Cleanup InputStream input = null;
        try {
            input = new FileInputStream(new File(getClass().getProtectionDomain().getCodeSource().getLocation().getPath()).getParent() + "/configCl.properties");
            prop.load(input);
            // load a properties file
            setUrl("ws://" + prop.getProperty("Ip") + ":" + prop.getProperty("Port") + "/ws/miner");
            setPathToLog(prop.getProperty("PathToLog"));
        } catch (IOException ex) {
            log.error("input", ex);
        }
    }


//    public String getGpuLogName() {
//        return gpuLogName;
//    }


}
