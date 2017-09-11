package app.classes.client;

import app.classes.models.Property;
import app.classes.models.ReciveCommand;
import app.classes.models.SendInformation;
import app.classes.services.ServiceGpu;
import app.classes.services.ShutdownServices;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.websocket.*;
import java.io.IOException;
import java.net.URI;

@ClientEndpoint
@Component("client")
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
    Property property;

    @PostConstruct
    public void init() {
        WebSocketContainer container = ContainerProvider
                .getWebSocketContainer();
        try {
            session = container.connectToServer(Client.class,
                    URI.create(property.getUrl()));
            log.info("connect");
        } catch (DeploymentException e) {
            log.error(e.getMessage(), e);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
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
    public void onMessage(String message) {
        JsonElement jelement = new JsonParser().parse(message);
        JsonObject jobject = jelement.getAsJsonObject();

        Gson json = new Gson();
        ReciveCommand minerStatistic = json.fromJson(jobject.getAsString(), ReciveCommand.class);

        SendInformation sendInformation = new SendInformation();
        sendInformation.setId(minerStatistic.getId());


        if (minerStatistic.isStatus()) {
            sendInformation.setInformation(serviceGpu.getInformation());
            session.getAsyncRemote().sendText(json.toJson(sendInformation));
        }

        if (minerStatistic.isStatus_debug()) {
            sendInformation.setInformation(serviceGpu.getLastTen());
            session.getAsyncRemote().sendText(json.toJson(sendInformation));
        }

        if (minerStatistic.isService_reboot_rig()) {
            shutdownServices.reboot();
        }

        if (minerStatistic.isService_shutdown_rig()) {
            shutdownServices.shutdown();
        }
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        log.info("[close] " + session);

    }

    @OnError
    public void onError(Session session, Throwable thr) {
    }
}
