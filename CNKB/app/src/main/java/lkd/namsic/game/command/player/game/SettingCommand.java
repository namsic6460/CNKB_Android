package lkd.namsic.game.command.player.game;

import android.app.Notification;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Arrays;
import java.util.List;

import lkd.namsic.game.command.PlayerCommand;
import lkd.namsic.game.config.Config;
import lkd.namsic.game.config.Emoji;
import lkd.namsic.game.enums.MapType;
import lkd.namsic.game.enums.Variable;
import lkd.namsic.game.enums.object.ItemList;
import lkd.namsic.game.enums.object.SkillList;
import lkd.namsic.game.exception.WeirdCommandException;
import lkd.namsic.game.object.GameMap;
import lkd.namsic.game.object.Player;

public class SettingCommand extends PlayerCommand {

    @Override
    public void executeCommand(@NonNull Player player, @NonNull String command, @NonNull List<String> commands,
                               @Nullable String second, @Nullable String third, @Nullable String fourth,
                               @NonNull Notification.Action session) {
        if(second == null) {
            throw new WeirdCommandException();
        }

        if(Arrays.asList("가방", "인벤토리", "inventory", "inven").contains(second)) {
            if(fourth == null) {
                throw new WeirdCommandException();
            }

            Long itemId = ItemList.findByName(command.split(third)[1].trim());
            if(itemId == null) {
                throw new WeirdCommandException("해당 이름을 가진 아이템을 찾을 수 없습니다");
            }

            List<Long> highPriorityItems = player.getListVariable(Variable.HIGH_PRIORITY_ITEM);

            assert third != null;
            if(third.equals("추가") || third.equals("add")) {
                if (!highPriorityItems.isEmpty()) {
                    if (highPriorityItems.contains(itemId)) {
                        throw new WeirdCommandException("해당 아이템은 이미 우선순위에 등록되어 있습니다");
                    }
                }

                highPriorityItems.add(itemId);
                player.replyPlayer("아이템이 우선순위에 성공적으로 등록되었습니다");
            } else if(third.equals("제거") || third.equals("remove")) {
                if(highPriorityItems.isEmpty()) {
                    throw new WeirdCommandException("우선순위 목록이 비어있습니다");
                } else if(!highPriorityItems.contains(itemId)) {
                    throw new WeirdCommandException("우선순위 목록에 해당 아이템이 등록되어있지 않습니다");
                }

                if(highPriorityItems.size() == 1) {
                    player.getVariable().remove(Variable.HIGH_PRIORITY_ITEM);
                } else {
                    highPriorityItems.remove(itemId);
                }

                player.replyPlayer("아이템이 우선순위에서 성공적으로 제거되었습니다");
            } else {
                throw new WeirdCommandException();
            }
        } else if(second.equals("공개") || second.equals("public")) {
            boolean isPublic = !player.getObjectVariable(Variable.INFO_PUBLIC, true);
            player.setVariable(Variable.INFO_PUBLIC, isPublic);

            String msg;
            if(isPublic) {
                msg = "공개";
            } else {
                msg = "비공개";
            }
            
            player.replyPlayer("이제 당신의 정보는 " + Emoji.focus(msg) + " 상태입니다");
        } else if(second.equals(SkillList.CUTTING_MOONLIGHT.getDisplayName())) {
            boolean cuttingMoonlight = !player.getObjectVariable(Variable.CUTTING_MOONLIGHT_DAMAGE_SAME, false);
            player.setVariable(Variable.CUTTING_MOONLIGHT_DAMAGE_SAME, cuttingMoonlight);

            String msg;
            if(cuttingMoonlight) {
                msg = "가립니다";
            } else {
                msg = "가리지 않습니다";
            }

            player.replyPlayer("이제 당신의 " + Emoji.focus(SkillList.CUTTING_MOONLIGHT.getDisplayName()) + "는 피아를 " + msg);
        } else if(second.equals("거점") || second.equals("home")) {
            GameMap map = Config.getMapData(player.getLocation());

            if(third == null) {
                if(!MapType.cityList().contains(map.getMapType())) {
                    throw new WeirdCommandException("거점은 마을에서만 설정할 수 있습니다");
                }

                player.getBaseLocation().setMap(player.getLocation().getX(), player.getLocation().getY());
                player.replyPlayer("거점이 " + map.getLocationName() + " 으로 설정되었습니다");
            } else if(third.equals("확인") || third.equals("check")) {
                player.replyPlayer("현재 거점은 " + map.getLocationName() + " 입니다");
            } else {
                throw new WeirdCommandException();
            }
        }
    }

}
