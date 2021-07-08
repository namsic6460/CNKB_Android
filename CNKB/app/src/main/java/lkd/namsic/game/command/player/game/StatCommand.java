package lkd.namsic.game.command.player.game;

import android.app.Notification;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import lkd.namsic.game.KakaoTalk;
import lkd.namsic.game.command.PlayerCommand;
import lkd.namsic.game.enums.StatType;
import lkd.namsic.game.exception.WeirdCommandException;
import lkd.namsic.game.gameObject.Player;
import lkd.namsic.game.manager.DisplayManager;
import lkd.namsic.game.manager.StatManager;

public class StatCommand extends PlayerCommand {

    @Override
    public void executeCommand(@NonNull final Player player, @NonNull String command, @NonNull List<String> commands,
                               @Nullable String second, @Nullable String third, @Nullable String fourth,
                               @NonNull Notification.Action session) {
        if(second == null) {
            throw new WeirdCommandException();
        } else if(second.equals("정보") || second.equals("info")) {
            DisplayManager.getInstance().displayStatInfo(player);
        } else {
            if(third == null) {
                throw new WeirdCommandException();
            }

            KakaoTalk.checkDoing(player);

            String statStr = commands.get(commands.size() - 1);
            int stat = Integer.parseInt(statStr);

            StatType statType = StatType.findByName(command.replace(statStr, "").trim());
            if(statType == null) {
                throw new WeirdCommandException("알 수 없는 스텟 이름입니다");
            }

            StatManager.getInstance().increaseStat(player, statType, stat);
        }
    }

}
