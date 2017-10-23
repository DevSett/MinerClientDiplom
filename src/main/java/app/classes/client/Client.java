package app.classes.client;

import app.classes.models.Property;
import app.classes.models.ReciveCommand;
import app.classes.models.SendInformation;
import app.classes.services.ServiceGpu;
import app.classes.services.ShutdownServices;
import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.Base64;

@ClientEndpoint
@Log4j
public class Client {

    @Autowired
    ServiceGpu serviceGpu;

    @Autowired
    ShutdownServices shutdownServices;

    @Getter
    @Setter
    private Session session;

    @Autowired
    public Property property;

    public void start() {
        connect();
    }

    public void stop() {
        try {
            session.close();
        } catch (IOException e) {
            log.error("stop IO", e);
            e.printStackTrace();
        }
    }

    @OnOpen
    public void onOpen(Session session) {
        log.info("[open] " + session);
    }

    @OnMessage
    public void onMessage(String message) throws IOException {
        log.info("Получено: " + message);


        Gson json = new Gson();
        ReciveCommand minerStatistic = json.fromJson(message, ReciveCommand.class);

        SendInformation sendInformation = new SendInformation();
        sendInformation.setId(minerStatistic.getId());


        if (minerStatistic.isStatus()) {
            sendInformation.setNameRig(property.getName());
            sendInformation.setInformation(serviceGpu.getInformation());
            session.getAsyncRemote().sendText(json.toJson(sendInformation));
            System.out.println(json.toJson(sendInformation));
        }


        if (minerStatistic.isStatus_debug()) {
            sendInformation.setNameRig(property.getName());
            sendInformation.setInformation(serviceGpu.getLastTen());
            session.getAsyncRemote().sendText(json.toJson(sendInformation));
        }

        if (minerStatistic.isService_reboot_rig()) {
            shutdownServices.reboot();
        }

        if (minerStatistic.isService_shutdown_rig()) {
            shutdownServices.shutdown();
        }

        if (minerStatistic.isGetName()) {
            sendInformation.setNameRig(property.getName());
            sendInformation.setInformation(property.getName());
            session.getAsyncRemote().sendText(json.toJson(sendInformation));

        }
        if (minerStatistic.isScreenshot()) {
            sendInformation.setNameRig(property.getName());
            sendInformation.setInformation(new String(Base64.getEncoder().encode(IOUtils.toByteArray(serviceGpu.getScreenshot()))));
            session.getAsyncRemote().sendText(json.toJson(sendInformation));
        }

    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) throws InterruptedException {
        log.info("[close] " + session);
        connect();
//        while (!session.isOpen()) {
//            connect();
//        }
    }

    private void connect() {

        WebSocketContainer container = ContainerProvider
                .getWebSocketContainer();
        try {
            session = container.connectToServer(this,
                    URI.create(property.getUrl()));
            log.info("connect");
        } catch (DeploymentException e) {
            log.error(e.getMessage(), e);
            connect();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    @OnError
    public void onError(Session session, Throwable thr) {
    }
}
