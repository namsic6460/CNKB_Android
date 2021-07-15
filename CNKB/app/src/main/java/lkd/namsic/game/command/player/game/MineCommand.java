package lkd.namsic.game.command.player.game;

import android.app.Notification;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import lkd.namsic.game.KakaoTalk;
import lkd.namsic.game.command.PlayerCommand;
import lkd.namsic.game.enums.Doing;
import lkd.namsic.game.enums.Variable;
import lkd.namsic.game.enums.object_list.NpcList;
import lkd.namsic.game.exception.WeirdCommandException;
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
            if(player.getChatCount(NpcList.SECRET.getId()) != 3) {
                throw new WeirdCommandException("튜토리얼을 먼저 끝내주세요");
            }

            player.setVariable(Variable.IS_TUTORIAL, false);
        }

        if (MineManager.getInstance().canMine(player)) {
            if(second == null) {
                MineManager.getInstance().mine(player, player.getVariable(Variable.MINE));
            } else {
                int mineLv = player.getVariable(Variable.MINE);
                int inputLv = Integer.parseInt(second);

                if(inputLv < 0 || inputLv > mineLv) {
                    throw new WeirdCommandException("광질 레벨은 0 이상, " + mineLv + "(현재 레벨) 이하여야 합니다");
                }

                MineManager.getInstance().mine(player, inputLv);
            }

            if(isTutorial) {
                player.setDoing(Doing.WAIT_RESPONSE);
            }
        } else {
            player.replyPlayer("마을이 아니어서 광질을 할 수 없습니다");
        }
    }

}
