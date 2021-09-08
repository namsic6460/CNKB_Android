package lkd.namsic.game.command.player.game;

import android.app.Notification;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Arrays;
import java.util.List;

import lkd.namsic.game.command.PlayerCommand;
import lkd.namsic.game.enums.Doing;
import lkd.namsic.game.exception.DoingFilterException;
import lkd.namsic.game.exception.WeirdCommandException;
import lkd.namsic.game.manager.AdventureManager;
import lkd.namsic.game.manager.DisplayManager;
import lkd.namsic.game.object.Player;

public class PercentCommand extends PlayerCommand {

    @Override
    public void executeCommand(@NonNull final Player player, @NonNull String command, @NonNull List<String> commands,
                               @Nullable String second, @Nullable String third, @Nullable String fourth,
                               @NonNull Notification.Action session) {
        if(second == null) {
            throw new WeirdCommandException();
        }

        if(second.equals("광질") || second.equals("mine")) {
            DisplayManager.getInstance().displayMinePercent(player);
        } else if(second.equals("낚시") || second.equals("fish")) {
            DisplayManager.getInstance().displayFishPercent(player);
        } else if(Arrays.asList("모험", "adventure", "adv").contains(second)) {
            DisplayManager.getInstance().displayAdvPercent(player);
        } else {
            throw new WeirdCommandException();
        }
    }

}
