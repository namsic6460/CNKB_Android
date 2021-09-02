package lkd.namsic.game.command.player.game;

import android.app.Notification;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import lkd.namsic.game.KakaoTalk;
import lkd.namsic.game.command.PlayerCommand;
import lkd.namsic.game.config.Config;
import lkd.namsic.game.enums.Doing;
import lkd.namsic.game.enums.EquipType;
import lkd.namsic.game.enums.Id;
import lkd.namsic.game.enums.Variable;
import lkd.namsic.game.enums.object.EquipList;
import lkd.namsic.game.object.Equipment;
import lkd.namsic.game.object.Player;
import lkd.namsic.setting.Logger;

public class RestCommand extends PlayerCommand {

    @Override
    public void executeCommand(@NonNull final Player player, @NonNull String command, @NonNull List<String> commands,
                               @Nullable String second, @Nullable String third, @Nullable String fourth,
                               @NonNull Notification.Action session) {
        KakaoTalk.checkDoing(player);

        player.setDoing(Doing.REST);
        player.setVariable(Variable.REST, System.currentTimeMillis() + Config.REST_TIME);

        player.replyPlayer("휴식을 시작합니다");

        long restTime = Config.REST_TIME;

        long earringId = player.getEquipped(EquipType.EARRINGS);
        if(earringId != EquipList.NONE.getId()) {
            Equipment earring = Config.getData(Id.EQUIPMENT, earringId);
            if(earring.getOriginalId() == EquipList.AQUAMARINE_EARRING.getId()) {
                restTime /= 2;
            }
        }

        try {
            Thread.sleep(restTime);
        } catch (InterruptedException e) {
            Logger.e("Rest", Config.errorString(e));
        }

        player.removeVariable(Variable.REST);
        player.setDoing(Doing.NONE);
        player.replyPlayer("휴식으로 체력과 마나가 모두 체워졌습니다!");
    }

}
