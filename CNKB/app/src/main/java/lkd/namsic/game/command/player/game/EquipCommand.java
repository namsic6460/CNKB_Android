package lkd.namsic.game.command.player.game;

import android.app.Notification;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Arrays;
import java.util.List;

import lkd.namsic.game.KakaoTalk;
import lkd.namsic.game.command.PlayerCommand;
import lkd.namsic.game.enums.EquipType;
import lkd.namsic.game.exception.WeirdCommandException;
import lkd.namsic.game.manager.EquipManager;
import lkd.namsic.game.manager.ItemDisplayManager;
import lkd.namsic.game.object.Player;

public class EquipCommand extends PlayerCommand {

    @Override
    public void executeCommand(@NonNull final Player player, @NonNull String command, @NonNull List<String> commands,
                               @Nullable String second, @Nullable String third, @Nullable String fourth,
                               @NonNull Notification.Action session) {
        if(second == null) {
            ItemDisplayManager.getInstance().displayEquippedInfo(player);
        } else if(Arrays.asList("인벤토리", "인벤", "inventory", "inven").contains(second)) {
            int page = third == null ? 1 : Integer.parseInt(third);
            ItemDisplayManager.getInstance().displayEquipInventory(player, page);
        } else if(second.equals("사용") || second.equals("use")) {
            if(third == null) {
                throw new WeirdCommandException();
            }

            KakaoTalk.checkDoing(player);

            EquipType equipType;
            String other = null;

            String[] split = command.replace(second, "").trim().split("\\.");
            equipType = EquipType.findByName(split[0]);
            if(split.length == 2) {
                other = split[1].trim();
            }

            EquipManager.getInstance().tryUse(player, equipType, other);
        } else if(second.equals("정보") || second.equals("info")) {
            if(third == null) {
                throw new WeirdCommandException();
            }

            ItemDisplayManager.getInstance().displayEquipInfo(player, Integer.parseInt(third));
        } else if(second.equals("분해") || second.equals("dcp")) {
            if(fourth == null) {
                throw new WeirdCommandException();
            }

            int equipIndex = Integer.parseInt(third);
            String equipName = command.replace(second, "").replace(third, "").trim();

            EquipManager.getInstance().decompose(player, equipIndex, equipName);
        } else if(second.equals("전체") || second.equals("all")) {
            if(third == null) {
                throw new WeirdCommandException();
            }

            if(third.equals("착용") || third.equals("equip")) {
                EquipManager.getInstance().equipAll(player);
            } else if(third.equals("해제") || third.equals("unequip")) {
                EquipManager.getInstance().unEquipAll(player);
            } else {
                throw new WeirdCommandException();
            }
        } else {
            KakaoTalk.checkDoing(player);
            EquipManager.getInstance().equip(player, Integer.parseInt(second));
        }
    }

}
