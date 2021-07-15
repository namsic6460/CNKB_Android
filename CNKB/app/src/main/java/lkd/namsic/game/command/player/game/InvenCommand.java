package lkd.namsic.game.command.player.game;

import android.app.Notification;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import lkd.namsic.game.command.PlayerCommand;
import lkd.namsic.game.gameObject.Player;
import lkd.namsic.game.manager.ItemDisplayManager;

public class InvenCommand extends PlayerCommand {

    @Override
    public void executeCommand(@NonNull Player player, @NonNull String command, @NonNull List<String> commands,
                               @Nullable String second, @Nullable String third, @Nullable String fourth,
                               @NonNull Notification.Action session) {
        int page = second == null ? 1 : Integer.parseInt(second);
        ItemDisplayManager.getInstance().displayInventory(player, page);
    }

}
