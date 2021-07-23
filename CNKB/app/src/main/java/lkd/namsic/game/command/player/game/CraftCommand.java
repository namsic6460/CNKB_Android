package lkd.namsic.game.command.player.game;

import android.app.Notification;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Arrays;
import java.util.List;

import lkd.namsic.game.KakaoTalk;
import lkd.namsic.game.command.PlayerCommand;
import lkd.namsic.game.config.Config;
import lkd.namsic.game.exception.WeirdCommandException;
import lkd.namsic.game.gameObject.Player;
import lkd.namsic.game.manager.ItemDisplayManager;
import lkd.namsic.game.manager.ItemManager;

public class CraftCommand extends PlayerCommand {

    @Override
    public void executeCommand(@NonNull final Player player, @NonNull String command, @NonNull List<String> commands,
                               @Nullable String second, @Nullable String third, @Nullable String fourth,
                               @NonNull Notification.Action session) {
        if(second == null) {
            throw new WeirdCommandException();
        }

        if(Arrays.asList("아이템", "item", "장비", "equip").contains(second)) {
            ItemDisplayManager.getInstance().displayRecipes(player, second.equals("아이템") || second.equals("item"));
        } else if(second.equals("확인") || second.equals("check")) {
            ItemDisplayManager.getInstance().displayRecipe(player, command.replace(second, "").trim());
        } else {
            KakaoTalk.checkDoing(player);

            String lastStr = "";
            int lastNumber = 1;

            if(third != null) {
                lastStr = commands.get(commands.size() - 1);

                try {
                    lastNumber = Integer.parseInt(lastStr);

                    if(fourth != null) {
                        String frontStr = commands.get(commands.size() - 2);
                        int frontNumber = Integer.parseInt(frontStr);

                        if(lastNumber < 1) {
                            throw new WeirdCommandException("레시피 번호는 1 이상이어야 합니다");
                        }

                        ItemManager.getInstance().craft(player, Config.replaceLast(Config.replaceLast(
                                command, lastStr, ""), frontStr, ""), frontNumber, lastNumber);
                        return;
                    }
                } catch (NumberFormatException e) {
                    lastStr = "";
                }
            }

            ItemManager.getInstance().craft(player, Config.replaceLast(command, lastStr, ""), lastNumber, null);
        }
    }

}
