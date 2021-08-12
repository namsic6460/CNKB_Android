package lkd.namsic.game.command.player.debug;

import android.app.Notification;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;
import java.util.Objects;

import lkd.namsic.game.KakaoTalk;
import lkd.namsic.game.command.PlayerCommand;
import lkd.namsic.game.config.Config;
import lkd.namsic.game.enums.Doing;
import lkd.namsic.game.enums.Id;
import lkd.namsic.game.object.Player;

public class CleanCommand extends PlayerCommand {

    @Override
    public void executeCommand(@NonNull Player player, @NonNull String command, @NonNull List<String> commands,
                               @Nullable String second, @Nullable String third, @Nullable String fourth,
                               @NonNull Notification.Action session) {
        if(player.getId().getObjectId() == 1) {
            long objectId = Long.parseLong(Objects.requireNonNull(second));

            Player target = Config.loadObject(Id.PLAYER, objectId);

            target.setDoing(Doing.NONE);

            long count = Objects.requireNonNull(Objects.requireNonNull(Config.OBJECT_COUNT.get(Id.PLAYER)).get(objectId));
            for(int i = 1; i < count; i++) {
                Config.unloadObject(target);
            }

            Config.unloadObject(target);
            KakaoTalk.reply(session, "Success");
        }
    }

}
