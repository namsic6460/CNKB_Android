package lkd.namsic.game.command.common;

import android.app.Notification;

import androidx.annotation.NonNull;

import lkd.namsic.game.KakaoTalk;
import lkd.namsic.game.command.CommonCommand;

public class DevCommand extends CommonCommand {
    
    @Override
    public void executeCommand(@NonNull String command, @NonNull Notification.Action session) {
        KakaoTalk.reply(session, "---개발자 정보---\n" +
            "닉네임 : 남식(namsic)\n" +
            "메일 : namsic6460@gmail.com\n" +
            "문의 : 개인 카카오톡 또는 이메일");
    }
    
}
