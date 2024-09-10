package lkd.namsic.noti.service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import lkd.namsic.noti.MainActivity;
import lkd.namsic.noti.Sessions;
import lkd.namsic.noti.dto.MessageRequest;
import lkd.namsic.noti.utils.CollectionUtils;
import lkd.namsic.noti.websocket.Connector;
import lombok.Getter;

public class NotificationListener extends NotificationListenerService {

    private Connector connector = null;

    @Getter
    @SuppressLint("StaticFieldLeak")
    private static NotificationListener instance = null;

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
        this.refreshConnector();
    }

    private void refreshConnector() {
        if (this.connector == null || !this.connector.isOpen()) {
            try {
                this.connector = new Connector();
                this.connector.connectBlocking(5000, TimeUnit.MILLISECONDS);

                MainActivity.toast("Reconnected websocket");
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }
    }

    @Nullable
    @Override
    public ComponentName startService(Intent service) {
        MainActivity.toast("Noti Service Started");
        return super.startService(service);
    }

    @Override
    public boolean stopService(Intent intent) {
        if (this.connector != null && this.connector.isOpen()) {
            try {
                this.connector.close();
                this.connector = null;
            } catch (Exception e) {
                Log.e("Connector.close", "Failed to close connector", e);
            }
        }

        MainActivity.toast("Noti Service Stopped");
        return super.stopService(intent);
    }

    @Override
    public void onDestroy() {
        MainActivity.toast("Noti Service Stopped");

        if (this.connector != null && this.connector.isOpen()) {
            try {
                this.connector.close();
                this.connector = null;
            } catch (Exception e) {
                Log.e("Connector.close", "Failed to close connector", e);
            }
        }

        this.stopSelf();
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        super.onNotificationPosted(sbn);
        this.refreshConnector();

        if(!MainActivity.isOn.get()) {
            return;
        }

        if (sbn.getPackageName().equals("com.kakao.talk")) {
            try {
                List<Notification.Action> actions = new ArrayList<>();
                Notification.Action[] immutableList = sbn.getNotification().actions;
                if(immutableList != null) {
                    actions.addAll(Arrays.asList(immutableList));
                }

                Notification.WearableExtender extender = new Notification.WearableExtender(sbn.getNotification());
                Optional.ofNullable(extender.getActions()).ifPresent(actions::addAll);

                if(actions.isEmpty()) {
                    return;
                }

                for (Notification.Action action : actions) {
                    if (!CollectionUtils.isEmpty(action.getRemoteInputs()) &&
                        (action.title.toString().toLowerCase().contains("reply") ||
                        action.title.toString().toLowerCase().contains("답장"))
                    ) {
                        if(sbn.getNotification().getLargeIcon() == null) {
                            return;
                        }

                        Bundle data = sbn.getNotification().extras;
                        String message = String.valueOf(data.get("android.text"));
                        String sender = data.getString("android.title");
                        String room = data.getString("android.subText");

                        MainActivity.mainActivity.textView.append("\n[" + (room == null ? "CNKB" : room) + "] " + sender + ": " + message);
                        action.actionIntent.send(this, 0, new Intent());
                        ((NotificationManager) this.getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE)).cancel(sbn.getId());

                        String key = room + "|" + sender;
                        if (!Sessions.getKnownRoomNames().contains(key)) {
                            Sessions.getTempSessionMap().put(key, action);
                            List<MessageRequest> pendingRequests = this.connector.getPendingRequests().remove(sender);
                            if (pendingRequests == null || pendingRequests.isEmpty()) {
                                return;
                            }

                            for (MessageRequest request : pendingRequests) {
                                this.connector.processRequest(request);
                            }
                        }

                        return;
                    }
                }
            } catch(Exception e) {
                Log.e("KakaoTalkListener", "Failed to process notification", e);
            }
        }
    }
}
