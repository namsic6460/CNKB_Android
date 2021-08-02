package lkd.namsic.game.command.player.game;

import android.app.Notification;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import lkd.namsic.game.command.PlayerCommand;
import lkd.namsic.game.config.Config;
import lkd.namsic.game.exception.WeirdCommandException;
import lkd.namsic.game.object.GameMap;
import lkd.namsic.game.object.Player;
import lkd.namsic.game.manager.DisplayManager;

public class MapCommand extends PlayerCommand {

    @Override
    public void executeCommand(@NonNull Player player, @NonNull String command, @NonNull List<String> commands,
                               @Nullable String second, @Nullable String third, @Nullable String fourth,
                               @NonNull Notification.Action session) {
        if (second == null) {
            GameMap map = Config.getMapData(player.getLocation());
            player.replyPlayer(map.getInfo(), map.getInnerInfo());
        } else if (second.equals("목록") || second.equals("list")) {
            DisplayManager.getInstance().displayMapList(player);
        } else {
            throw new WeirdCommandException();
        }
    }

}
