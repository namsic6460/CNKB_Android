package lkd.namsic.game.command.player.game;

import android.app.Notification;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import lkd.namsic.game.KakaoTalk;
import lkd.namsic.game.command.PlayerCommand;
import lkd.namsic.game.enums.Doing;
import lkd.namsic.game.enums.Variable;
import lkd.namsic.game.gameObject.Player;
import lkd.namsic.game.manager.MineManager;

public class MineCommand extends PlayerCommand {

    @Override
    public void executeCommand(@NonNull final Player player, @NonNull String command, @NonNull List<String> commands,
                               @Nullable String second, @Nullable String third, @Nullable String fourth,
                               @NonNull Notification.Action session) {
        boolean isTutorial = player.getObjectVariable(Variable.IS_TUTORIAL, false);
        if(!isTutorial) {
            KakaoTalk.checkDoing(player);
        } else {
            player.setVariable(Variable.IS_TUTORIAL, false);
        }

        if (MineManager.getInstance().canMine(player)) {
            MineManager.getInstance().mine(player);

            if(isTutorial) {
                player.setDoing(Doing.WAIT_RESPONSE);
            }
        } else {
            player.replyPlayer("광질이 불가능한 상태입니다");
        }
    }

}
