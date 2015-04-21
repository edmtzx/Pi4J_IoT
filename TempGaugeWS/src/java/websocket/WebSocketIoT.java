package websocket;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.websocket.OnClose;

import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import rpisensor.BMP085Device;

@ApplicationScoped
@ServerEndpoint(value = "/iotWebSocket")
public class WebSocketIoT {

    private static Set<Session> sessions = Collections.synchronizedSet(new HashSet<Session>());

    @Inject
    private BMP085Device dev;

    private static double data[];

    @OnMessage
    public String onMessage(String message) {
        return null;
    }

    @OnOpen
    public void onOpen(Session id) {

        sessions.add(id);
        try {
            //BMP085Device dev = new BMP085Device(false);
            data = dev.getReading();
            System.out.println("temperature=" + data[1] + " pressure=" + (data[0] * 0.01) + " hpa");
            sendMessage(String.format("%.2f", data[1]));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClose
    public void onClose(Session id) {
        sessions.remove(id);
    }

    public void sendMessage(String message) {
        for (Iterator<Session> iter = sessions.iterator(); iter.hasNext();) {
            Session recipient = iter.next();
            try {
                recipient.getBasicRemote().sendText(message);
            } catch (Exception e) {
                iter.remove();
            }
        }
    }
}
