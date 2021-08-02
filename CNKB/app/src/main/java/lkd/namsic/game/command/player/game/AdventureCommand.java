package lkd.namsic.game.command.player.game;

import android.app.Notification;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import lkd.namsic.game.command.PlayerCommand;
import lkd.namsic.game.enums.Doing;
import lkd.namsic.game.exception.DoingFilterException;
import lkd.namsic.game.exception.WeirdCommandException;
import lkd.namsic.game.object.Player;
import lkd.namsic.game.manager.AdventureManager;

public class AdventureCommand extends PlayerCommand {

    @Override
    public void executeCommand(@NonNull final Player player, @NonNull String command, @NonNull List<String> commands,
                               @Nullable String second, @Nullable String third, @Nullable String fourth,
                               @NonNull Notification.Action session) {
        if(player.getDoing().equals(Doing.NONE)) {
            if(second != null) {
                throw new WeirdCommandException();
            }

            AdventureManager.getInstance().tryAdventure(player);
        } else if(player.getDoing().equals(Doing.ADVENTURE)) {
            if(second == null) {
                throw new WeirdCommandException();
            }

            AdventureManager.getInstance().exploreCommand(player, second);
        } else {
            throw new DoingFilterException(player.getDoing());
        }
    }

}
