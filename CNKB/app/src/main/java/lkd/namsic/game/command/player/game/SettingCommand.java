package lkd.namsic.game.command.player.game;

import android.app.Notification;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Arrays;
import java.util.List;

import lkd.namsic.game.KakaoTalk;
import lkd.namsic.game.command.PlayerCommand;
import lkd.namsic.game.enums.Variable;
import lkd.namsic.game.enums.object_list.ItemList;
import lkd.namsic.game.exception.ObjectNotFoundException;
import lkd.namsic.game.exception.WeirdCommandException;
import lkd.namsic.game.gameObject.Player;

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

            Long itemId = ItemList.findByName(fourth);
            if(itemId == null) {
                throw new ObjectNotFoundException("해당 이름을 가진 아이템을 찾을 수 없습니다");
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
                    throw new ObjectNotFoundException("우선순위 목록이 비어있습니다");
                } else if(!highPriorityItems.contains(itemId)) {
                    throw new ObjectNotFoundException("우선순위 목록에 해당 아이템이 등록되어있지 않습니다");
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
        } else if(second.equals("pvp")) {
            KakaoTalk.checkDoing(player);

            if(third == null) {
                player.replyPlayer("현재 PVP가 " + (player.isPvp() ? "활성화" : "비활성화") + "되어있습니다");
            } else {
                if (third.equals("켜기") || third.equals("on")) {
                    player.setPvp(true, null);
                } else if (third.equals("끄기") || third.equals("off")) {
                    if(fourth == null) {
                        throw new WeirdCommandException();
                    }

                    int day = Integer.parseInt(fourth);
                    if(day != 1 && day != 7) {
                        throw new WeirdCommandException("PvP 비활성화는 1일 또는 7일만 가능합니다");
                    }

                    player.setPvp(false, day);
                }
            }
        }
    }

}
