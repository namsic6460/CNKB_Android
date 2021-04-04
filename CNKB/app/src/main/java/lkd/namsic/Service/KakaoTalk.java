package lkd.namsic.Service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.RemoteInput;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import lkd.namsic.MainActivity;
import lkd.namsic.Setting.Logger;

public class KakaoTalk {

    private final static Map<String, Notification.Action> groupSessions = new ConcurrentHashMap<>();
    private final static Map<String, Notification.Action> soloSessions = new ConcurrentHashMap<>();

    public static Notification.Action getGroupSession(String room) {
        return Objects.requireNonNull(groupSessions.get(room));
    }

    public static Notification.Action getSoloSession(String room) {
        return Objects.requireNonNull(soloSessions.get(room));
    }

    public static void onChat(@NonNull final String sender, @NonNull final String image,
                              @NonNull final String msg, @NonNull final String room, final boolean isGroupChat,
                              @NonNull final Notification.Action session) {
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

    public static void reply(@NonNull Notification.Action session, @NonNull String msg){
        reply(session, msg, null);
    }

    public static void reply(@NonNull Notification.Action session, @NonNull String msg, @Nullable String innerMsg) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();

        if(innerMsg != null) {
            StringBuilder builder = new StringBuilder(msg);
            builder.append("\n(자세한 내용은 전체보기 확인)\n");

            for(int i = 0; i < 500; i++) {
                builder.append('\u200b');
            }
            builder.append('\n');
            builder.append(innerMsg);

            msg = builder.toString();
        }

        for (RemoteInput input : session.getRemoteInputs()) {
            bundle.putCharSequence(input.getResultKey(), msg);
        }

        RemoteInput.addResultsToIntent(session.getRemoteInputs(), intent, bundle);

        try {
            session.actionIntent.send(KakaoTalkListener.context, 0, intent);
        } catch (PendingIntent.CanceledException e) {
            Logger.e("KakaoTalkListener", e);
        }
    }

    private void replyGroup(@NonNull String msg) {
        for(Notification.Action session : groupSessions.values()) {
            reply(session, msg);
        }
    }

    private void replyAll(@NonNull String msg) {
        replyGroup(msg);

        for(Notification.Action session : soloSessions.values()) {
            reply(session, msg);
        }
    }

}
