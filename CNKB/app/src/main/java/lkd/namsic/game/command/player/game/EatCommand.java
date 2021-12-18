package lkd.namsic.game.command.player.game;

import android.app.Notification;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import lkd.namsic.game.KakaoTalk;
import lkd.namsic.game.command.PlayerCommand;
import lkd.namsic.game.manager.ItemManager;
import lkd.namsic.game.object.Player;

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
        } catch(NumberFormatException ignore) {
        }
        
        ItemManager.getInstance().tryEat(player, itemName.trim(), count);
    }
    
}
