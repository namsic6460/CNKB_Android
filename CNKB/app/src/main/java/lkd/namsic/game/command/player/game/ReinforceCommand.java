package lkd.namsic.game.command.player.game;

import android.app.Notification;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import lkd.namsic.game.KakaoTalk;
import lkd.namsic.game.command.PlayerCommand;
import lkd.namsic.game.manager.ItemDisplayManager;
import lkd.namsic.game.manager.ReinforceManager;
import lkd.namsic.game.object.Player;

public class ReinforceCommand extends PlayerCommand {

    @Override
    public void executeCommand(@NonNull final Player player, @NonNull String command, @NonNull List<String> commands,
                               @Nullable String second, @Nullable String third, @Nullable String fourth,
                               @NonNull Notification.Action session) {
        if(second == null) {
            ReinforceManager.getInstance().displayReinforceExplanation(player);
        } else if(second.equals("정보") || second.equals("info")) {
            ItemDisplayManager.getInstance().displayReinforceInfo(player);
        } else {
            KakaoTalk.checkDoing(player);
            ReinforceManager.getInstance().reinforce(player, Integer.parseInt(second));
        }
    }

}
