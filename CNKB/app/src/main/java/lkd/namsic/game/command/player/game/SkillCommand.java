package lkd.namsic.game.command.player.game;

import android.app.Notification;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import lkd.namsic.game.command.PlayerCommand;
import lkd.namsic.game.manager.SkillManager;
import lkd.namsic.game.object.Player;

public class SkillCommand extends PlayerCommand {

    @Override
    public void executeCommand(@NonNull Player player, @NonNull String command, @NonNull List<String> commands,
                               @Nullable String second, @Nullable String third, @Nullable String fourth,
                               @NonNull Notification.Action session) {
        if(second == null) {
            SkillManager.getInstance().displaySkillHelp(player);
        } else if(second.equals("목록") || second.equals("list")) {
            SkillManager.getInstance().displaySkillList(player);
        }
    }

}
