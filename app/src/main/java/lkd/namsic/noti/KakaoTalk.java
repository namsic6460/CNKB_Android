package lkd.namsic.noti;

import android.app.Notification;
import android.app.RemoteInput;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import lkd.namsic.noti.service.NotificationListener;
import lombok.experimental.UtilityClass;

@UtilityClass
public class KakaoTalk {

    public static void send(Notification.Action session, String message, String innerMessage) {
        if(session == null) {
            return;
        }

        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        StringBuilder builder = new StringBuilder(message);

        if(innerMessage != null) {
            for(int i = 0; i < 500; i++) {
                builder.append('\u200b');
            }

            builder.append("\n\n").append(innerMessage);
        }

        for (RemoteInput input : session.getRemoteInputs()) {
            bundle.putCharSequence(input.getResultKey(), builder.toString());
        }

        try {
            RemoteInput.addResultsToIntent(session.getRemoteInputs(), intent, bundle);
            session.actionIntent.send(NotificationListener.getInstance(), 0, intent);
            Thread.sleep(200);
        } catch(Exception e) {
            Log.e("KakaoTalk", "Failed to send", e);
        }
    }
}
