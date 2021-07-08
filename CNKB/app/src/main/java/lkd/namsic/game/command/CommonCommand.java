package lkd.namsic.game.command;

import android.app.Notification;

import androidx.annotation.NonNull;

public abstract class CommonCommand implements CommandListener {

    @Override
    public void execute(@NonNull Object... params) {
        this.executeCommand((String) params[0], (Notification.Action) params[1]);
    }

    public abstract void executeCommand(@NonNull String command, @NonNull Notification.Action session);

}
