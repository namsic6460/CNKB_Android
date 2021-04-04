package lkd.namsic.Service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.util.Objects;

import lkd.namsic.Setting.Logger;
import lkd.namsic.MainActivity;

public class KakaoTalkListener extends NotificationListenerService {

    @SuppressLint("StaticFieldLeak")
    public static Context context;

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
                        sender = Objects.requireNonNull(data.getString("android.title"));
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

                        KakaoTalk.onChat(sender, image, msg.trim(), room, isGroupChat, action);
                    }
                }
            } catch(Exception e) {
                Logger.e("KakaoTalkListener", e);
            }
        }
    }

}