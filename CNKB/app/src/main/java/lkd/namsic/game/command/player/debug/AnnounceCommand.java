package lkd.namsic.game.command.player.debug;

import android.app.Notification;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import lkd.namsic.game.KakaoTalk;
import lkd.namsic.game.command.PlayerCommand;
import lkd.namsic.game.object.Player;

public class AnnounceCommand extends PlayerCommand {

    @Override
    public void executeCommand(@NonNull Player player, @NonNull String command, @NonNull List<String> commands,
                               @Nullable String second, @Nullable String third, @Nullable String fourth,
                               @NonNull Notification.Action session) {
        if(player.getId().getObjectId() == 1) {
            boolean sendSolo = false;

            if(second != null && second.equals("y")) {
                sendSolo = true;
            }

            String msg = "[공지]\n";
            if(second != null && (second.equals("y") || second.equals("n"))) {
                msg += command.replace(second, "").trim();
            } else {
                msg += command.trim();
            }

            if(sendSolo) {
                for(Notification.Action replySession : KakaoTalk.soloSessions.values()) {
                    KakaoTalk.reply(replySession, msg);
                }

                for(Notification.Action replySession : KakaoTalk.groupSessions.values()) {
                    KakaoTalk.reply(replySession, msg);
                }
            } else {
                KakaoTalk.replyAll(msg);
            }

            KakaoTalk.reply(session, "Success");
        }
    }

}
