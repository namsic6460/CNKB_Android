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
import lkd.namsic.game.manager.DisplayManager;
import lkd.namsic.game.manager.FarmManager;
import lkd.namsic.game.object.Player;

public class FarmCommand extends PlayerCommand {

    @Override
    public void executeCommand(@NonNull final Player player, @NonNull String command, @NonNull List<String> commands,
                               @Nullable String second, @Nullable String third, @Nullable String fourth,
                               @NonNull Notification.Action session) {
        if (second == null) {
            DisplayManager.getInstance().displayFarmHelp(player);
        } else if (Arrays.asList("구입", "구매", "buy").contains(second)) {
            KakaoTalk.checkDoing(player);
            FarmManager.getInstance().buyFarm(player);
        } else if (second.equals("심기") || second.equals("plant")) {
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
        } else if (second.equals("확인") || second.equals("check")) {
            DisplayManager.getInstance().displayFarm(player);
        } else if (second.equals("제거") || second.equals("remove")) {
            KakaoTalk.checkDoing(player);

            if (third == null) {
                throw new WeirdCommandException();
            }

            FarmManager.getInstance().removePlant(player, Integer.parseInt(third));
        } else if (second.equals("수확") || second.equals("harvest")) {
            KakaoTalk.checkDoing(player);
            FarmManager.getInstance().harvest(player);
        } else if (second.equals("업그레이드") || second.equals("upgrade")) {
            KakaoTalk.checkDoing(player);
            FarmManager.getInstance().upgrade(player);
        } else {
            throw new WeirdCommandException();
        }
    }

}
