package lkd.namsic.game.command.player.game;

import android.app.Notification;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Arrays;
import java.util.List;

import lkd.namsic.game.KakaoTalk;
import lkd.namsic.game.command.PlayerCommand;
import lkd.namsic.game.enums.Doing;
import lkd.namsic.game.exception.WeirdCommandException;
import lkd.namsic.game.object.Player;
import lkd.namsic.game.manager.AppraiseManager;

public class AppraiseCommand extends PlayerCommand {

    @Override
    public void executeCommand(@NonNull final Player player, @NonNull String command, @NonNull List<String> commands,
                               @Nullable String second, @Nullable String third, @Nullable String fourth,
                               @NonNull Notification.Action session) {
        if(Arrays.asList("중지", "정지", "stop").contains(second)) {
            if(player.getDoing().equals(Doing.APPRAISE)) {
                AppraiseManager.getInstance().stopAppraise(player);
            } else {
                throw new WeirdCommandException();
            }
        } else {
            KakaoTalk.checkDoing(player);

            int count = second == null ? 1 : Integer.parseInt(second);
            AppraiseManager.getInstance().appraiseStone(player, count);
        }
    }

}
