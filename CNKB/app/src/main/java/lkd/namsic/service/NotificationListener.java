package lkd.namsic.service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import lkd.namsic.MainActivity;
import lkd.namsic.game.KakaoTalk;
import lkd.namsic.game.config.Config;
import lkd.namsic.setting.Logger;

public class NotificationListener extends NotificationListenerService {
    
    @SuppressLint("StaticFieldLeak")
    private static NotificationListener instance = null;
    
    public static NotificationListener getInstance() {
        return instance;
    }
    
    @SuppressLint("StaticFieldLeak")
    public static Context context;
    
    private static boolean isCreated = false;
    
    @Override
    public void onCreate() {
        if(isCreated) {
            this.onDestroy();

            MainActivity.toast("Noti Service Already Started");
            return;
        } else {
            instance = this;
            super.onCreate();

            isCreated = true;
            MainActivity.toast("Noti Service Started");
        }

        context = getApplicationContext();
    }

    @Override
    public void onDestroy() {
        MainActivity.toast("Noti Service Stopped");
        this.stopSelf();
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        super.onNotificationPosted(sbn);

        if(!MainActivity.isOn) {
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
                actions.addAll(Optional.ofNullable(extender.getActions()).orElse(Collections.emptyList()));

                if(actions.isEmpty()) {
                    return;
                }
                
                for (Notification.Action action : actions) {
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

                        if(sbn.getNotification().getLargeIcon() == null) {
                            return;
                        }

                        bitmap = ((BitmapDrawable) sbn.getNotification().getLargeIcon().loadDrawable(context)).getBitmap();

                        if(room == null) {
                            room = sender;
                        } else {
                            isGroupChat = true;
                        }

                        if (msg.equals("null")) {
                            msg = "";
                        }

                        int length = msg.length();
                        if(!msg.startsWith("n eval") && length > 100) {
                            Logger.i("KakaoTalkListener", "Returned by length(" + length + ")");
                            return;
                        }

                        if(bitmap == null) {
                            image = "이미지를 가져올 수 없습니다";
                        } else {
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);

                            image = Base64.encodeToString(baos.toByteArray(), 0);
                            int imageLength = image.length();
                            image = image.substring(200, 250);
                            image = Config.getRegex(image, "-");
                            image = image + "+" + image.hashCode() + "+" + imageLength;
                        }

                        KakaoTalk.onChat(sender, image, msg.trim(), room, isGroupChat, action);
                        ((NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE)).cancel(sbn.getId());

                        return;
                    }
                }
            } catch(Exception e) {
                Logger.e("KakaoTalkListener", e);
            }
        }
    }


}