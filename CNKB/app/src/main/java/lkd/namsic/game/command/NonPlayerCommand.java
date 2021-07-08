package lkd.namsic.game.command;

import android.app.Notification;

import androidx.annotation.NonNull;

public abstract class NonPlayerCommand implements CommandListener {

    @Override
    public void execute(@NonNull Object... params) {
        this.executeCommand((String) params[0], (String) params[1], (String) params[2],
                (String) params[3], (boolean) params[4], (Notification.Action) params[5]);
    }

    public abstract void executeCommand(@NonNull String sender, @NonNull String image,
                                        @NonNull String command, @NonNull String room,
                                        boolean isGroupChat, @NonNull Notification.Action session);

}
