package lkd.namsic.game.command.player.game;

import android.app.Notification;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Arrays;
import java.util.List;

import lkd.namsic.game.command.PlayerCommand;
import lkd.namsic.game.exception.WeirdCommandException;
import lkd.namsic.game.manager.RankingManager;
import lkd.namsic.game.object.Player;

public class RankingCommand extends PlayerCommand {

    @Override
    public void executeCommand(@NonNull final Player player, @NonNull String command, @NonNull List<String> commands,
                               @Nullable String second, @Nullable String third, @Nullable String fourth,
                               @NonNull Notification.Action session) {
        if(second == null) {
            RankingManager.getInstance().displayLvRanking(player);
        } else if(second.equals("레벨") || second.equals("lv")) {
            RankingManager.getInstance().displayLvRanking(player);
        } else if(Arrays.asList("탑", "타워", "tower").contains(second)) {
            RankingManager.getInstance().displayTowerRanking(player);
        } else {
            throw new WeirdCommandException();
        }
    }

}
