package lkd.namsic.noti;

import android.app.Notification;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import lombok.Getter;

public class Sessions {

    @Getter
    private static final Set<String> knownRoomNames = new HashSet<>();

    @Getter
    private static final Map<String, Notification.Action> tempSessionMap = new ConcurrentHashMap<>();

    @Getter
    private static final Map<String, Notification.Action> sessionMap = new ConcurrentHashMap<>();
}
