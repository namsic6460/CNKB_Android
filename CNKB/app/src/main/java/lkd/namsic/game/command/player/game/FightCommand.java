package lkd.namsic.game.command.player.game;

import android.app.Notification;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import lkd.namsic.game.command.PlayerCommand;
import lkd.namsic.game.enums.Doing;
import lkd.namsic.game.exception.DoingFilterException;
import lkd.namsic.game.exception.WeirdCommandException;
import lkd.namsic.game.object.Player;
import lkd.namsic.game.manager.FightManager;

public class FightCommand extends PlayerCommand {

    @Override
    public void executeCommand(@NonNull final Player player, @NonNull String command, @NonNull List<String> commands,
                               @Nullable String second, @Nullable String third, @Nullable String fourth,
                               @NonNull Notification.Action session) {
        if(player.getId().getObjectId() != 1) {
            throw new WeirdCommandException("현재 전투는 버그로 인해 비활성화 되었습니다");
        }

        if(second == null) {
            throw new WeirdCommandException("전투 대상을 선택해주세요");
        } else {
            if (player.getDoing().equals(Doing.NONE)) {
                if (!FightManager.getInstance().tryFight(player, command, false)) {
                    player.replyPlayer("해당 상대와의 전투가 불가능한 상태입니다",
                            "대상을 정확히 지정했는지 다시 확인해주세요\n" +
                                    "대상이 플레이어인 경우, 본인 또는 대상의 PvP가 꺼져있을 수 있습니다\n" +
                                    "또는 대상이 현재 다른 행동을 하고 있을 수 있습니다\n" +
                                    "또는 일부 전투의 경우 난입이 불가능할 수도 있습니다");
                }
            } else if (Doing.fightList().contains(player.getDoing())) {
                FightManager.getInstance().fightCommand(player, second, command.replace(second, "").trim());
            } else {
                throw new DoingFilterException(player.getDoing());
            }
        }
    }

}
