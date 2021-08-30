package lkd.namsic.game.command.player.debug;

import android.app.Notification;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;
import java.util.Objects;

import lkd.namsic.game.KakaoTalk;
import lkd.namsic.game.command.PlayerCommand;
import lkd.namsic.game.enums.object.ItemList;
import lkd.namsic.game.exception.WeirdCommandException;
import lkd.namsic.game.object.Player;

public class GiveCommand extends PlayerCommand {

    @Override
    public void executeCommand(@NonNull Player player, @NonNull String command, @NonNull List<String> commands,
                               @Nullable String second, @Nullable String third, @Nullable String fourth,
                               @NonNull Notification.Action session) {
        if(player.getId().getObjectId() == 1) {
            if(second == null) {
                throw new WeirdCommandException();
            }

            String countStr = commands.get(commands.size() - 1);
            String itemName = command.replace(countStr, "").trim();

            int count = 1;
            try {
                count = Integer.parseInt(countStr);
            } catch (NumberFormatException ignore) {}

            player.addItem(Objects.requireNonNull(ItemList.findByName(itemName)), count, false);
            KakaoTalk.reply(session, "Success");
        }
    }

}
