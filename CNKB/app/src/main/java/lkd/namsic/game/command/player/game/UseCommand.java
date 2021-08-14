package lkd.namsic.game.command.player.game;

import android.app.Notification;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import lkd.namsic.game.KakaoTalk;
import lkd.namsic.game.command.PlayerCommand;
import lkd.namsic.game.config.Config;
import lkd.namsic.game.object.Player;
import lkd.namsic.game.manager.ItemManager;

public class UseCommand extends PlayerCommand {

    @Override
    public void executeCommand(@NonNull Player player, @NonNull String command, @NonNull List<String> commands,
                               @Nullable String second, @Nullable String third, @Nullable String fourth,
                               @NonNull Notification.Action session) {
        KakaoTalk.checkDoing(player);

        String itemName = command;
        String lastWord = commands.get(commands.size() - 1);

        int count = 1;
        try {
            count = Integer.parseInt(lastWord);
            itemName = Config.replaceLast(command, lastWord, "");
        } catch (NumberFormatException ignore) {}

        String other = null;
        String[] split = itemName.split(",");
        if(split.length == 2) {
            itemName = split[0];
            other = split[1].trim();
        }

        ItemManager.getInstance().tryUse(player, itemName.trim(), other, count);
    }

}
