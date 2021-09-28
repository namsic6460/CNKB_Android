package lkd.namsic.game.command.player.game;

import android.app.Notification;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Arrays;
import java.util.List;

import lkd.namsic.game.command.PlayerCommand;
import lkd.namsic.game.config.Config;
import lkd.namsic.game.enums.Doing;
import lkd.namsic.game.exception.DoingFilterException;
import lkd.namsic.game.exception.WeirdCommandException;
import lkd.namsic.game.manager.ShopManager;
import lkd.namsic.game.object.Player;

public class ShopCommand extends PlayerCommand {

    @Override
    public void executeCommand(@NonNull final Player player, @NonNull String command, @NonNull List<String> commands,
                               @Nullable String second, @Nullable String third, @Nullable String fourth,
                               @NonNull Notification.Action session) {
        if(second == null) {
            throw new WeirdCommandException();
        } else {
            if(Arrays.asList("도움말", "명령어", "?", "help", "h").contains(second)) {
                ShopManager.getInstance().displayShopHelp(player);
            } else if(second.equals("목록") || second.equals("list")) {
                ShopManager.getInstance().displayShopList(player);
            } else if(player.getDoing().equals(Doing.NONE)) {
                ShopManager.getInstance().startShopping(player, command);
            } else if(player.getDoing().equals(Doing.SHOP)) {
                String subCommand = command.replace(second, "").trim();
                int count = 0;

                if(fourth != null) {
                    String countStr = commands.get(commands.size() - 1);

                    try {
                        count = Integer.parseInt(countStr);
                        subCommand = Config.replaceLast(subCommand, countStr, "").trim();
                    } catch (NumberFormatException ignore) {}
                }

                ShopManager.getInstance().shopCommand(player, second, subCommand, count);
            } else {
                throw new DoingFilterException(player.getDoing());
            }
        }
    }

}
