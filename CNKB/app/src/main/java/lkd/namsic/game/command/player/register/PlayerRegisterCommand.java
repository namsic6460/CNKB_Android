package lkd.namsic.game.command.player.register;

import android.app.Notification;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import lkd.namsic.game.KakaoTalk;
import lkd.namsic.game.command.PlayerCommand;
import lkd.namsic.game.gameObject.Player;

public class PlayerRegisterCommand extends PlayerCommand {

    @Override
    public void executeCommand(@NonNull Player player, @NonNull String command, @NonNull List<String> commands,
                               @Nullable String second, @Nullable String third, @Nullable String fourth,
                               @NonNull Notification.Action session) {
        KakaoTalk.reply(session, "이미 회원가입이 되어 있습니다.\n" +
                "회원가입을 진행한 적이 없는 경우, 프로필 이미지 또는 카카오톡 이름을 변경한 후 다시 시도해주세요");
    }

}
