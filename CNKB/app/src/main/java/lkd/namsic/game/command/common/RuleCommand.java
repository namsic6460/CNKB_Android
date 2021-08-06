package lkd.namsic.game.command.common;

import android.app.Notification;

import androidx.annotation.NonNull;

import lkd.namsic.game.KakaoTalk;
import lkd.namsic.game.command.CommonCommand;
import lkd.namsic.game.config.Emoji;

public class RuleCommand extends CommonCommand {

    @Override
    public void executeCommand(@NonNull String command, @NonNull Notification.Action session) {
        KakaoTalk.reply(session, "[게임 규칙]\n" +
                "1. 매크로류의 모든 행위를 금지한다\n" +
                "2. 게임 버그의 악용을 금지한다(버그 및 이상점 제보시 보상)\n" +
                "3. 뉴비 학살을 금지한다(과도하게 죽이는 것)\n" +
                "4. 무차별 PvP를 금지한다\n" +
                "5. 숟가락 살인을 목적으로 한 PvP를 금지한다" +
                "6. 필드 아이템 스틸을 허용한다\n" +
                "7. 동맹 및 다구리를 허용한다\n" +
                "8. 무지성 도배를 금지한다\n" +
                "9. 본 규칙을 숙지하지 않아 발생한 문제는 개인의 책임이다\n" +
                "10. 그 외의 모든 행위는 게임의 제작자의 판단 하에 처벌 또는 보상한다"
        );
    }

}
