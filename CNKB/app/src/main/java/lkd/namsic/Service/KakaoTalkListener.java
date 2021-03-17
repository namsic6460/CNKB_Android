package lkd.namsic.Service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.RemoteInput;
import android.content.Intent;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lkd.namsic.Setting.Logger;
import lkd.namsic.MainActivity;

public class KakaoTalkListener extends NotificationListenerService {

    Map<String, Notification.Action> sessions = new ConcurrentHashMap<>();

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        super.onNotificationPosted(sbn);

        if(!MainActivity.isOn) {
            return;
        }

        if (sbn.getPackageName().equals("com.kakao.talk")) {
            try {
                Notification.WearableExtender extender = new Notification.WearableExtender(sbn.getNotification());

                for (Notification.Action action : extender.getActions()) {
                    if (action.getRemoteInputs() != null && action.getRemoteInputs().length > 0 &&
                            action.title.toString().toLowerCase().contains("reply") ||
                            action.title.toString().toLowerCase().contains("답장")) {
                        Bundle data = sbn.getNotification().extras;
                        String room, sender, msg;
                        boolean isGroupChat = false;

                        room = data.getString("android.summaryText");
                        sender = data.getString("android.title");
                        msg = String.valueOf(data.get("android.text"));

                        if(room == null) {
                            room = sender;
                        } else {
                            isGroupChat = true;
                        }

                        if (msg.equals("null"))
                            msg = "";

                        onChat(sender, msg.trim(), room, isGroupChat, action);
                    }
                }
            } catch(Exception e) {
                Logger.e("KakaoTalkListener", e);
            }
        }
    }


    private void onChat(final String sender, final String msg, final String room, final boolean isGroupChat, final Notification.Action session) {
        sessions.put(room, session);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Logger.i("chatLog", "sender : " + sender + "\nmsg : " + msg + "\nroom : " + room + "\nisGroupChat : " + isGroupChat);

                    if (msg.equals("namsicTest")) {
                        reply(session, "reply");
                    }
                } catch (Exception e) {
                    Logger.e("onChat", e);
                }
            }
        });

        MainActivity.startThread(thread);
    }

    private void reply(Notification.Action session, String msg){
        Intent intent = new Intent();
        Bundle bundle = new Bundle();

        for (RemoteInput input : session.getRemoteInputs()) {
            bundle.putCharSequence(input.getResultKey(), msg);
        }

        RemoteInput.addResultsToIntent(session.getRemoteInputs(), intent, bundle);

        try {
            session.actionIntent.send(this, 0, intent);
        } catch (PendingIntent.CanceledException e) {
            Logger.e("KakaoTalkListener", e);
        }
    }

    private void replyAll(String msg) {
        for(Notification.Action session : sessions.values()) {
            reply(session, msg);
        }
    }

}