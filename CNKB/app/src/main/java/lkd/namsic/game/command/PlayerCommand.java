package lkd.namsic.game.command;

import android.app.Notification;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import lkd.namsic.game.object.Player;

public abstract class PlayerCommand implements CommandListener {

    @Override
    @SuppressWarnings("unchecked")
    public void execute(@NonNull Object... params) {
        this.executeCommand((Player) params[0], (String) params[1], (List<String>) params[2],
                (String) params[3], (String) params[4], (String) params[5], (Notification.Action) params[6]);
    }

    public abstract void executeCommand(@NonNull Player player, @NonNull String command, @NonNull List<String> commands,
                                        @Nullable String second, @Nullable String third, @Nullable String fourth,
                                        @NonNull Notification.Action session);

}
