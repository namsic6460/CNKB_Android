package lkd.namsic.game.command.player.game;

import android.app.Notification;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import lkd.namsic.game.KakaoTalk;
import lkd.namsic.game.command.PlayerCommand;
import lkd.namsic.game.enums.Doing;
import lkd.namsic.game.exception.DoingFilterException;
import lkd.namsic.game.exception.WeirdCommandException;
import lkd.namsic.game.object.Player;
import lkd.namsic.game.manager.MoveManager;

public class MoveCommand extends PlayerCommand {

    @Override
    public void executeCommand(@NonNull Player player, @NonNull String command, @NonNull List<String> commands,
                               @Nullable String second, @Nullable String third, @Nullable String fourth,
                               @NonNull Notification.Action session) {
        if(second == null) {
            throw new WeirdCommandException();
        }

        if(second.equals("맵") || second.equals("map")) {
            KakaoTalk.checkDoing(player);

            String locationStr = command.replace(second, "").trim();
            MoveManager.getInstance().moveMap(player, locationStr);
        } else if(second.equals("필드") || second.equals("field")) {
            try {
                KakaoTalk.checkDoing(player);
            } catch (DoingFilterException e) {
                if(!Doing.fightList().contains(player.getDoing())) {
                    throw e;
                }
            }

            if(third == null) {
                throw new WeirdCommandException();
            }

            MoveManager.getInstance().moveField(player, third);
        } else {
            KakaoTalk.checkDoing(player);
            MoveManager.getInstance().moveMap(player, command);
        }
    }

}
