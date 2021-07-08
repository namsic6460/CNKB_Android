package lkd.namsic.game.command.player.game;

import android.app.Notification;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import lkd.namsic.game.command.PlayerCommand;
import lkd.namsic.game.config.Config;
import lkd.namsic.game.enums.Id;
import lkd.namsic.game.exception.WeirdCommandException;
import lkd.namsic.game.gameObject.Player;
import lkd.namsic.game.manager.DisplayManager;

public class InfoCommand extends PlayerCommand {

    @Override
    public void executeCommand(@NonNull final Player player, @NonNull String command, @NonNull List<String> commands,
                               @Nullable String second, @Nullable String third, @Nullable String fourth,
                               @NonNull Notification.Action session) {
        if(second == null || command.equals(player.getNickName())) {
            DisplayManager.getInstance().displayItemInfo(player, player);
        } else if(second.equals("아이템") || second.equals("item")) {
            DisplayManager.getInstance().displayItemInfo(player, command.replace(second, "").trim());
        } else {
            Long playerId = Config.PLAYER_ID.get(command);

            if(playerId == null) {
                throw new WeirdCommandException("알 수 없는 대상입니다");
            }

            Player target = Config.getData(Id.PLAYER, playerId);
            DisplayManager.getInstance().displayItemInfo(player, target);
        }
    }

}
