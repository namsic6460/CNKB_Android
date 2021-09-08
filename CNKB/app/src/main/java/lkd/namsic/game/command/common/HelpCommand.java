package lkd.namsic.game.command.common;

import android.app.Notification;

import androidx.annotation.NonNull;

import lkd.namsic.game.KakaoTalk;
import lkd.namsic.game.command.CommonCommand;
import lkd.namsic.game.config.Emoji;

public class HelpCommand extends CommonCommand {

    @Override
    public void executeCommand(@NonNull String command, @NonNull Notification.Action session) {
        KakaoTalk.reply(session, "=====명령어 목록=====\n" +
                        "모든 명령어는 앞쪽에 " + Emoji.focus("n") + " 또는 " + Emoji.focus("ㅜ") + " 를 붙여야합니다\n" +
                        "도움말에 표시되는 () 는 입력하지 않습니다.\n" +
                        "이곳에 없는 명령어는 상세 도움말로 확인해주세요\n" +
                        "채팅방 링크 : https://open.kakao.com/o/gsPjUBnd\n",
                "❓ ㅜ 도움말 -> 도움말을 표시합니다\n\n" +
                        "❓ ㅜ ?? -> 상세 도움말을 표시합니다\n\n" +
                        "\uD83D\uDC4B ㅜ 회원가입 (이름) -> 회원가입을 합니다\n\n" +
                        "\uD83D\uDCBB ㅜ 개발자 -> 개발자 정보를 표시합니다\n\n" +
                        "\uD83D\uDCDD ㅜ 규칙 -> 게임 규칙을 표시합니다\n\n" +
                        "\uD83D\uDE34 ㅜ 휴식 -> 10분간 휴식하여 체력과 마나를 회복합니다\n\n" +
                        "\uD83D\uDC66 ㅜ 정보 (다른 플레이어 이름) -> 자신 또는 다른 플레이어의 정보를 표시합니다\n\n" +
                        "\uD83D\uDCBC ㅜ 인벤토리 -> 인벤토리를 표시합니다\n\n" +
                        "\uD83D\uDC55 ㅜ 장비 -> 착용된 장비를 표시합니다\n\n" +
                        "\uD83D\uDC55 ㅜ 장비 인벤토리 -> 장비 인벤토리를 표시합니다\n\n" +
                        "\uD83D\uDC55 ㅜ 장비 (장비 번호) -> 장비를 착용 또는 착용 해제합니다\n\n" +
                        "\uD83D\uDCAC ㅜ 대화 (Npc 이름) -> Npc 와 대화합니다\n\n" +
                        "\uD83D\uDC40 ㅜ 맵 -> 현재 위치의 정보를 표시합니다\n\n" +
                        "\uD83D\uDC40 ㅜ 맵 목록 -> 이동 가능한 맵 목록을 표시합니다\n\n" +
                        "\uD83D\uDC63 ㅜ 이동 (x-y or 맵 이름) -> 맵으로 이동합니다\n\n" +
                        "\uD83D\uDC63 ㅜ 이동 필드 (x-y) -> 필드를 이동합니다\n\n" +
                        "✋ ㅜ 줍기 -> 맵에 떨어진 모든 아이템을 줍습니다\n\n" +
                        "✋ ㅜ 사용 (아이템 이름) (개수) -> 아이템을 사용합니다\n\n" +
                        "\uD83C\uDF56 ㅜ 먹기 (아이템 이름) (개수) -> 아이템을 먹습니다\n\n" +
                        "⛏ ㅜ 광질 -> 광질을 합니다\n\n" +
                        "\uD83D\uDEAE ㅜ 감정 -> 돌을 감정하여 보석을 찾습니다\n\n" +
                        "\uD83D\uDEAE ㅜ 감정 중지 -> 감정을 바로 멈추고 보상의 절반만 획득합니다\n\n" +
                        "\uD83C\uDFA3 ㅜ 낚시 -> 낚시를 합니다\n\n" +
                        "\uD83D\uDC7E ㅜ 전투 (번호) -> 맵 정보에서 확인한 번호로 몬스터와 사냥합니다\n\n" +
                        "\uD83D\uDC7E ㅜ 전투 (플레이어 이름) -> 플레이어와 전투합니다\n\n" +
                        "⏳ ㅜ 리스폰 -> 10초 후 현재 맵의 몬스터를 리스폰시킵니다\n\n" +
                        "\uD83D\uDC5F ㅜ 모험 -> 모험을 합니다\n\n" +
                        "\uD83C\uDF3E ㅜ 농사 -> 농사에 대한 설명을 표시합니다\n\n" +
                        "\uD83C\uDF3E ㅜ 농사 구매 -> 농장을 구매합니다\n\n" +
                        "\uD83C\uDF3E ㅜ 농사 심기 (씨앗 이름) -> 농장에 씨앗을 심습니다\n\n" +
                        "\uD83C\uDF3E ㅜ 농사 확인 -> 농장을 확인합니다\n\n" +
                        "\uD83C\uDF3E ㅜ 농사 제거 (씨앗 번호) -> 씨앗을 제거합니다\n\n" +
                        "\uD83C\uDF3E ㅜ 농사 수확 -> 작물을 수확합니다\n\n" +
                        "\uD83C\uDF3E ㅜ 농장 업그레이드 -> 농장을 업그레이드 합니다\n\n" +
                        "\uD83C\uDF59 ㅜ 스텟 -> 스텟 정보를 표시합니다\n\n" +
                        "\uD83C\uDF59 ㅜ 스텟 (스텟이름) (개수) -> 스텟을 증가시킵니다\n\n" +
                        "✨ ㅜ 스킬 -> 스킬에 대한 설명을 표시합니다\n\n" +
                        "✨ ㅜ 스킬 목록 -> 보유하고 있는 스킬 목록을 표시합니다\n\n" +
                        "\uD83D\uDEE0 ㅜ 제작 (아이템 or 장비) -> 제작 가능한 아이템 또는 장비를 보여줍니다\n\n" +
                        "\uD83D\uDEE0 ㅜ 제작 (아이템 이름 or 장비 이름) -> 아이템 또는 장비를 제작 합니다\n\n" +
                        "\uD83D\uDEE0 ㅜ 제작 확인 (아이템 이름 or 장비 이름) -> 아이템 또는 장비의 제작법을 표시합니다\n\n" +
                        "\uD83C\uDD99 ㅜ 강화 -> 강화에 대한 설명을 표시합니다\n\n" +
                        "\uD83C\uDD99 ㅜ 강화 정보 -> 장착중인 장비들의 강화 상태를 표시합니다\n\n" +
                        "\uD83C\uDD99 ㅜ 강화 (장비 번호) -> 장비 인벤토리에서 확인한 번호로 장비를 강화합니다\n\n" +
                        "\uD83C\uDFEA ㅜ 상점 도움말 -> 상점 도움말을 표시합니다\n\n" +
                        "\uD83C\uDFEA ㅜ 상점 목록 -> 이용 가능한 상점 목록을 표시합니다\n" +
                        "\uD83C\uDFEA ㅜ 상점 (Npc 이름) -> 상점을 이용합니다\n\n" +
                        "\uD83D\uDCCA ㅜ 랭킹 -> 레벨 랭킹을 표시합니다");
    }

}
