package lkd.namsic.Service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.RemoteInput;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lkd.namsic.Setting.Logger;
import lkd.namsic.MainActivity;

public class KakaoTalkListener extends NotificationListenerService {

    Map<String, Notification.Action> groupSessions = new ConcurrentHashMap<>();
    Map<String, Notification.Action> soloSessions = new ConcurrentHashMap<>();

    @SuppressLint("StaticFieldLeak")
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();

        context = getApplicationContext();
    }

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
                        String room, sender, msg, image;
                        Bitmap bitmap;
                        boolean isGroupChat = false;

                        room = data.getString("android.summaryText");
                        sender = data.getString("android.title");
                        msg = String.valueOf(data.get("android.text"));
                        bitmap = ((BitmapDrawable) sbn.getNotification().getLargeIcon().loadDrawable(context)).getBitmap();

                        if(room == null) {
                            room = sender;
                        } else {
                            isGroupChat = true;
                        }

                        if (msg.equals("null")) {
                            msg = "";
                        }

                        if(bitmap == null) {
                            image = "이미지를 가져올 수 없습니다";
                        } else {
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);

                            image = Base64.encodeToString(baos.toByteArray(), 0);
                            int imageLength = image.length();
                            image = image.substring(200, 250);
                            image = image + "+" + image.hashCode() + "+" + imageLength;
                        }

                        onChat(sender, image, msg.trim(), room, isGroupChat, action);
                    }
                }
            } catch(Exception e) {
                Logger.e("KakaoTalkListener", e);
            }
        }
    }


    private void onChat(final String sender, final String image, final String msg, final String room,
                        final boolean isGroupChat, final Notification.Action session) {
        if(isGroupChat) {
            groupSessions.put(room, session);
        } else {
            soloSessions.put(room, session);
        }

        Thread thread = new Thread(() -> {
            try {
                Logger.i("chatLog", "sender: " + sender + "\nimage: " + image +
                        "\nmsg: " + msg + "\nroom: " + room + "\nisGroupChat: " + isGroupChat);

                if (msg.equals("namsicTest")) {
                    reply(session, "reply");
                }
            } catch (Exception e) {
                Logger.e("onChat", e);
            }
        });

        MainActivity.startThread(thread);
    }

    public static void reply(Notification.Action session, String msg){
        Intent intent = new Intent();
        Bundle bundle = new Bundle();

        for (RemoteInput input : session.getRemoteInputs()) {
            bundle.putCharSequence(input.getResultKey(), msg);
        }

        RemoteInput.addResultsToIntent(session.getRemoteInputs(), intent, bundle);

        try {
            session.actionIntent.send(context, 0, intent);
        } catch (PendingIntent.CanceledException e) {
            Logger.e("KakaoTalkListener", e);
        }
    }

    private void replyGroup(String msg) {
        for(Notification.Action session : groupSessions.values()) {
            reply(session, msg);
        }
    }

    private void replyAll(String msg) {
        replyGroup(msg);

        for(Notification.Action session : soloSessions.values()) {
            reply(session, msg);
        }
    }

}