package lkd.namsic.game.command.player.game;

import android.app.Notification;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import lkd.namsic.game.command.PlayerCommand;
import lkd.namsic.game.config.Config;
import lkd.namsic.game.enums.Id;
import lkd.namsic.game.enums.Variable;
import lkd.namsic.game.exception.WeirdCommandException;
import lkd.namsic.game.object.Player;
import lkd.namsic.game.manager.DisplayManager;
import lkd.namsic.game.manager.ItemDisplayManager;

public class InfoCommand extends PlayerCommand {

    @Override
    public void executeCommand(@NonNull final Player player, @NonNull String command, @NonNull List<String> commands,
                               @Nullable String second, @Nullable String third, @Nullable String fourth,
                               @NonNull Notification.Action session) {
        if(second == null || command.equals(player.getNickName())) {
            DisplayManager.getInstance().displayInfo(player, player);
        } else if(second.equals("아이템") || second.equals("item")) {
            ItemDisplayManager.getInstance().displayItemInfo(player, command.replace(second, "").trim());
        } else if(second.equals("장비") || second.equals("equip")) {
            ItemDisplayManager.getInstance().displayEquipInfo(player, command.replace(second, "").trim());
        } else if(second.equals("퀘스트") || second.equals("quest")) {
            DisplayManager.getInstance().displayQuestInfo(player, command.replace(second, "").trim());
        } else if(second.equals("스킬") || second.equals("skill")) {
            DisplayManager.getInstance().displaySkillInfo(player, command.replace(second, "").trim());
        } else if(second.equals("몬스터") || second.equals("monster")) {
            DisplayManager.getInstance().displayMonsterInfo(player, command.replace(second, "").trim());
        } else {
            Long playerId = Config.PLAYER_ID.get(command);

            if(playerId == null) {
                throw new WeirdCommandException("알 수 없는 대상입니다");
            }

            Player target = Config.getData(Id.PLAYER, playerId);
            if(!target.getObjectVariable(Variable.INFO_PUBLIC, true) && !player.getCurrentTitle().equals("관리자")) {
                throw new WeirdCommandException("해당 유저의 정보는 비공개되어 있습니다");
            }

            DisplayManager.getInstance().displayInfo(player, target);
        }
    }

}
