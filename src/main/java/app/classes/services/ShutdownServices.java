package app.classes.services;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component("shutdownServices")
@Value
@AllArgsConstructor
@NoArgsConstructor
@Log4j
public class ShutdownServices {
    private String shutdownCmd = "shutdown -s";
    private String rebootCmd = "shutdown -r";

    public void reboot(){
        try {
            Process child = Runtime.getRuntime().exec(rebootCmd);
        } catch (IOException e) {
            log.error(e.getMessage(),e);
        }
    }
    public void shutdown(){
        try {
            Process child = Runtime.getRuntime().exec(shutdownCmd);
        } catch (IOException e) {
            log.error(e.getMessage(),e);
        }
    }
}
