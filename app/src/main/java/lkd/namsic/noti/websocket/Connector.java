package lkd.namsic.noti.websocket;

import android.app.Notification;
import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lkd.namsic.noti.KakaoTalk;
import lkd.namsic.noti.MainActivity;
import lkd.namsic.noti.Sessions;
import lkd.namsic.noti.dto.MessageRequest;
import lombok.Getter;

public class Connector extends WebSocketClient {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Getter
    private final Map<String, List<MessageRequest>> pendingRequests = new ConcurrentHashMap<>();

    public Connector() {
        super(URI.create("ws://192.168.0.99:8080/handle"));
    }

    @Override
    public void onOpen(ServerHandshake handshake) {
        Log.i("Connector.onOpen", "connected");
    }

    @Override
    public void onMessage(String message) {
        if (!MainActivity.isOn.get()) {
            return;
        }

        try {
            MessageRequest request = this.objectMapper.readValue(message, MessageRequest.class);
            this.processRequest(request);
        } catch (Exception e) {
            Log.e("Connector.onText", "Failed to process request", e);
        }
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        Log.i("Connector.onClose", "Closed - " + code + ": " + reason + "(" + remote + ")");
    }

    @Override
    public void onError(Exception e) {
        Log.e("Connector.onException", "Error", e);
    }

    public void processRequest(MessageRequest request) {
        String message = request.getMessage();
        String innerMessage = request.getInnerMessage();
        String sender = request.getSender();
        String room = request.getRoom();
        if ("CNKB".equals(room)) {
            room = null;
        }

        String key = room + "|" + sender;

        Notification.Action action = Sessions.getSessionMap().get(key);
        if (action != null) {
            KakaoTalk.send(action, message, innerMessage);
            return;
        }

        action = Sessions.getTempSessionMap().remove(key);
        if (action != null) {
            Sessions.getKnownRoomNames().add(key);
            Sessions.getSessionMap().put(key, action);

            KakaoTalk.send(action, message, innerMessage);
            return;
        }

        List<MessageRequest> list = this.pendingRequests.computeIfAbsent(sender, k -> new ArrayList<>());
        list.add(request);
    }
}
