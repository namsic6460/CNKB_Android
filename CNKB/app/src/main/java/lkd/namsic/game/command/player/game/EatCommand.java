package lkd.namsic.game.command.player.game;

import android.app.Notification;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import lkd.namsic.game.KakaoTalk;
import lkd.namsic.game.command.PlayerCommand;
import lkd.namsic.game.gameObject.Player;
import lkd.namsic.game.manager.ItemManager;

public class EatCommand extends PlayerCommand {

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
            itemName = itemName.replace(lastWord, "");
        } catch (NumberFormatException ignore) {}

        if(!ItemManager.getInstance().tryEat(player, itemName.trim(), count)) {
            player.replyPlayer("아이템을 먹는것을 실패했습니다");
        }
    }

}
