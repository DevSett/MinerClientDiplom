package app.classes.models;

import lombok.*;
import lombok.extern.log4j.Log4j;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.Properties;

/**
 * Created by killsett on 13.06.17.
 */
@Log4j
@Data
@NoArgsConstructor
public class Property {


    private String url;


    private String pathToLog;

    private String name;

    private String temperatureArg;
    private String totalArg;
    private String mhsArg;
    private String charset;
    private String type;

    @PostConstruct
    public void init() {
        Properties prop = new Properties();
        try {
            @Cleanup InputStream input = new FileInputStream(new File(getClass().getProtectionDomain().getCodeSource().getLocation().getPath()).getParent() + "/configCl.properties");
            prop.load(input);
            // load a properties file
            setUrl("ws://" + prop.getProperty("Ip") + ":" + prop.getProperty("Port") + "/ws/miner");
            setPathToLog(prop.getProperty("PathToLog"));
            setName(prop.getProperty("RigName"));
            setTemperatureArg(prop.getProperty("TemperatureArg"));
            setTotalArg(prop.getProperty("TotalArg"));
            setMhsArg(prop.getProperty("MhsArg"));
            setCharset(prop.getProperty("Charset"));
            setType(prop.getProperty("TYPE"));
        } catch (IOException ex) {
            log.error("input", ex);
        }
    }


//    public String getGpuLogName() {
//        return gpuLogName;
//    }


}
