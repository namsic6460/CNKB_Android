package lkd.namsic.game.command.player.debug;

import android.app.Notification;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import lkd.namsic.game.Eval;
import lkd.namsic.game.KakaoTalk;
import lkd.namsic.game.command.PlayerCommand;
import lkd.namsic.game.object.Player;

public class EvalCommand extends PlayerCommand {

    @Override
    public void executeCommand(@NonNull Player player, @NonNull String command, @NonNull List<String> commands,
                               @Nullable String second, @Nullable String third, @Nullable String fourth,
                               @NonNull Notification.Action session) {
        if(player.getId().getObjectId() == 1) {
            String result = Eval.execute(command, player);
            KakaoTalk.reply(session, result);
        }
    }

}
