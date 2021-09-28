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
import lkd.namsic.game.manager.FarmManager;
import lkd.namsic.game.object.Player;

public class FarmCommand extends PlayerCommand {

    @Override
    public void executeCommand(@NonNull final Player player, @NonNull String command, @NonNull List<String> commands,
                               @Nullable String second, @Nullable String third, @Nullable String fourth,
                               @NonNull Notification.Action session) {
        if (second == null) {
            FarmManager.getInstance().displayFarmHelp(player);
        } else if (Arrays.asList("구입", "구매", "buy").contains(second)) {
            KakaoTalk.checkDoing(player);
            FarmManager.getInstance().buyFarm(player);
        } else if (Arrays.asList("심기", "plant", "p").contains(second)) {
            KakaoTalk.checkDoing(player);

            if (third == null) {
                throw new WeirdCommandException();
            }

            String seedName = command.replace(second, "");

            int seedCount = 1;

            if(fourth != null) {
                String lastStr = commands.get(commands.size() - 1);
                try {
                    seedCount = Integer.parseInt(lastStr);
                    seedName = Config.replaceLast(seedName, lastStr, "");
                } catch (NumberFormatException ignore) {}
            }

            FarmManager.getInstance().plant(player, seedName.trim(), seedCount);
        } else if (Arrays.asList("확인", "check", "c").contains(second)) {
            FarmManager.getInstance().displayFarm(player);
        } else if (Arrays.asList("제거", "remove", "r").contains(second)) {
            KakaoTalk.checkDoing(player);

            if (third == null) {
                throw new WeirdCommandException();
            }

            FarmManager.getInstance().removePlant(player, Integer.parseInt(third));
        } else if (Arrays.asList("수확", "harvest", "h").contains(second)) {
            KakaoTalk.checkDoing(player);
            FarmManager.getInstance().harvest(player);
        } else if (Arrays.asList("업그레이드", "upgrade", "u").contains(second)) {
            KakaoTalk.checkDoing(player);
            FarmManager.getInstance().upgrade(player);
        } else {
            throw new WeirdCommandException();
        }
    }

}
