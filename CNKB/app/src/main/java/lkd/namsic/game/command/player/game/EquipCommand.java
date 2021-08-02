package lkd.namsic.game.command.player.game;

import android.app.Notification;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Arrays;
import java.util.List;

import lkd.namsic.game.KakaoTalk;
import lkd.namsic.game.command.PlayerCommand;
import lkd.namsic.game.enums.EquipType;
import lkd.namsic.game.object.Player;
import lkd.namsic.game.manager.EquipManager;
import lkd.namsic.game.manager.ItemDisplayManager;

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
            EquipManager.getInstance().tryUse(player, EquipType.findByName(command.replace(second, "").trim().toLowerCase()));
        } else {
            KakaoTalk.checkDoing(player);
            EquipManager.getInstance().equip(player, Integer.parseInt(second));
        }
    }

}
