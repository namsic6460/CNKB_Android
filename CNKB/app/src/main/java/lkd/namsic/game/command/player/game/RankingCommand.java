package lkd.namsic.game.command.player.game;

import android.app.Notification;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import lkd.namsic.game.command.PlayerCommand;
import lkd.namsic.game.exception.WeirdCommandException;
import lkd.namsic.game.object.Player;
import lkd.namsic.game.manager.DisplayManager;

public class RankingCommand extends PlayerCommand {

    @Override
    public void executeCommand(@NonNull final Player player, @NonNull String command, @NonNull List<String> commands,
                               @Nullable String second, @Nullable String third, @Nullable String fourth,
                               @NonNull Notification.Action session) {
        if(second == null) {
            DisplayManager.getInstance().displayLvRanking(player);
        } else if(second.equals("레벨") || second.equals("lv")) {
            DisplayManager.getInstance().displayLvRanking(player);
        } else {
            throw new WeirdCommandException();
        }
    }

}
