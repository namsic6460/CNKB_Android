package lkd.namsic.game.command.player.game;

import android.app.Notification;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import lkd.namsic.game.command.PlayerCommand;
import lkd.namsic.game.enums.Doing;
import lkd.namsic.game.exception.DoingFilterException;
import lkd.namsic.game.exception.WeirdCommandException;
import lkd.namsic.game.gameObject.Player;
import lkd.namsic.game.manager.FishManager;

public class FishCommand extends PlayerCommand {

    @Override
    public void executeCommand(@NonNull final Player player, @NonNull String command, @NonNull List<String> commands,
                               @Nullable String second, @Nullable String third, @Nullable String fourth,
                               @NonNull Notification.Action session) {
        if(player.getDoing().equals(Doing.NONE)) {
            if(second != null) {
                throw new WeirdCommandException();
            }

            FishManager.getInstance().tryFish(player);
        } else if(player.getDoing().equals(Doing.FISH)) {
            if(second == null) {
                throw new WeirdCommandException();
            }

            FishManager.getInstance().fishCommand(player, second);
        } else {
            throw new DoingFilterException();
        }
    }

}
