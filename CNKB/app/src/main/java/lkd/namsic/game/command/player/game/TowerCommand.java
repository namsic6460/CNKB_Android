package lkd.namsic.game.command.player.game;

import android.app.Notification;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import lkd.namsic.game.command.PlayerCommand;
import lkd.namsic.game.manager.TowerManager;
import lkd.namsic.game.object.Player;

public class TowerCommand extends PlayerCommand {

    @Override
    public void executeCommand(@NonNull final Player player, @NonNull String command, @NonNull List<String> commands,
                               @Nullable String second, @Nullable String third, @Nullable String fourth,
                               @NonNull Notification.Action session) {
//        if(second == null) {
//            TowerManager.getInstance().displayTowerExplanation(player);
//        } else if(second.equals("입장") || second.equals("enter")) {
//            TowerManager.getInstance().enterTower(player);
//        } else if(second.equals(""))
    }

}
