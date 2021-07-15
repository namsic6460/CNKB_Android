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
                        "영어는 대소문자에 영향받지 않습니다\n" +
                        Emoji.focus("*") + " 이 붙은 명령어는 언제나 사용이 가능한 명령어입니다\n" +
                        "() : 필수 명령어, [] : 필수X 명령어, {} : 직접 입력\n" +
                        "채팅방 링크 : https://open.kakao.com/o/gsPjUBnd\n",
                "---전체 사용 가능 명령어---\n" +
                        Emoji.LIST + " *(도움말/명령어/?/h/help) : 도움말을 표시합니다\n\n" +
                        Emoji.LIST + " (회원가입/가입/register) ({닉네임}) : 회원가입을 합니다\n\n" +
                        Emoji.LIST + " *(개발자/dev) : 개발자 정보를 표시합니다\n\n" +
                        "\n---유저 명령어---\n" +
                        Emoji.LIST + " *(정보/info/i) [{플레이어 닉네임}] : 플레이어의 정보를 표시합니다\n\n" +
                        Emoji.LIST + " *(정보/info/i) (아이템/item) ({아이템 이름}) : 아이템의 정보를 표시합니다\n\n" +
                        Emoji.LIST + " *(정보/info/i) (장비/equip) ({장비 이름}) : 장비의 정보를 표시합니다\n\n" +
                        Emoji.LIST + " *(가방/인벤토리/인벤/inventory/inven) [{페이지}] : 현재 인벤토리를 표시합니다\n\n" +
                        Emoji.LIST + " *(장비/equip) : 장비 착용 현황을 표시합니다\n\n" +
                        Emoji.LIST + " *(장비/equip) (인벤토리/인벤/inventory/inven) [{페이지}] : 현재 장비 인벤토리를 표시합니다\n\n" +
                        Emoji.LIST + " (장비/equip) ({장비 번호}) : 장비를 착용 또는 착용 해제합니다\n\n" +
                        Emoji.LIST + " (대화/chat) ({NPC 이름}) : 해당 NPC 와 대화를 합니다\n\n" +
                        Emoji.LIST + " *(맵/map) : 현재 위치 정보를 표시합니다\n\n" +
                        Emoji.LIST + " *(맵/map) (목록/list) : 이동 가능한 주변 맵의 정보를 표시합니다\n\n" +
                        Emoji.LIST + " (이동/move) [맵/map] ({x좌표}-{y좌표}) : 해당 좌표의 지역으로 이동합니다(예시: " +
                        Emoji.focus("n move 2-3") + ")\n\n" +
                        Emoji.LIST + " (이동/move) [맵/map] ({맵 이름}) : 해당 맵으로 이동합니다(예시 : " +
                        Emoji.focus("n move map 시작의 마을") + ")\n\n" +
                        Emoji.LIST + " (이동/move) (필드/field) ({x좌표}-{y좌표}) : 해당 좌표의 필드 위치로 이동합니다(예시: " +
                        Emoji.focus("n move field 16-10") + ")\n\n" +
                        Emoji.LIST + " (사용/use) ({아이템 이름}) [{사용 개수}] : 해당 아이템을 일정 개수 사용합니다(예시: " +
                        Emoji.focus("n 사용 골드 주머니 5") + ")\n\n" +
                        Emoji.LIST + " (먹기/eat) ({아이템 이름}) [{사용 개수}] : 해당 아이템을 일정 개수 먹습니다(예시: " +
                        Emoji.focus("n 먹기 양고기 3") + ")\n\n" +
                        Emoji.LIST + " (광질/mine) [{광질 레벨}] : 광질을 합니다(약 1초 소요)(마을에서만 가능)\n\n" +
                        Emoji.LIST + " (감정/appraise/apr) [{아이템 개수}] : 돌을 감정하여 보석을 선별해냅니다(개당 5초 소요)\n\n" +
                        Emoji.LIST + " (감정/appraise/apr) (중지/정지/stop) : 감정을 중지합니다(보상의 50%만 적용됩니다)\n\n" +
                        Emoji.LIST + " (낚시/fish) [{낚시 레벨}] : 낚시를 합니다(추가 명령 필요)(강 또는 바다에서만 가능)\n\n" +
                        Emoji.LIST + " (전투/fight/f) ({닉네임}/{번호}) : 대상과의 전투를 시작합니다" +
                        "(좌표로 지정할 경우 몬스터 또는 보스만 대상이 됩니다)\n\n" +
                        Emoji.LIST + " (모험/adventure/adv) : 현재 지역을 탐색합니다(마을 이외 지역에서만 가능)(모험 중 공격받을 수 있음)\n\n" +
                        Emoji.LIST + " (스텟/stat) : 각 스텟들의 정보를 보여줍니다\n\n" +
                        Emoji.LIST + " (스텟/stat) ({스텟 이름}) ({숫자}) : SP를 소모하여 스텟을 증가시킵니다(영구 적용)\n\n" +
                        Emoji.LIST + " *(제작/craft) (아이템/item/장비/equip) : 제작 가능한 아이템 또는 장비 목록을 확인합니다\n\n" +
                        Emoji.LIST + " *(제작/craft) (확인/check) ({아이템 또는 장비 이름}) : 해당 아이템 또는 장비의 제작법을 확인합니다\n\n" +
                        Emoji.LIST + " (제작/craft) ({아이템 또는 장비 이름}) [{아이템 개수}] : 해당 아이템 또는 장비를 제작합니다\n\n" +
                        Emoji.LIST + " (제작/craft) ({아이템 또는 장비 이름}) ({아이템 개수}) [{레시피 번호}] : " +
                        "해당 아이템 또는 장비를 특정 레시피로 제작합니다\n\n" +
                        Emoji.LIST + " (랭킹/랭크/ranking/rank) [레벨] : 레벨 랭킹을 표시합니다\n\n" +
                        Emoji.LIST + " *(설정/setting/set) (가방/인벤토리/inventory/inven) (추가/add) ({아이템 이름}) : " +
                        "해당 아이템을 인벤토리 표시 우선순위 목록에 추가합니다\n\n" +
                        Emoji.LIST + " *(설정/setting/set) (가방/인벤토리/inventory/inven) (제거/remove) ({아이템 이름}) : " +
                        "해당 아이템을 인벤토리 표시 우선순위 목록에서 제거합니다\n\n" +
                        Emoji.LIST + " *(설정/setting/set) (pvp) : PvP 설정을 확인합니다\n\n" +
                        Emoji.LIST + " *(설정/setting/set) (pvp) (켜기/on) : PvP 설정을 활성화합니다\n\n" +
                        Emoji.LIST + " *(설정/setting/set) (pvp) (끄기/off) (1/7) : PvP 설정을 1일 또는 7일간 비활성화합니다" +
                        "(아이템 필요)(시간은 반올림 적용)"
        );
    }

}
