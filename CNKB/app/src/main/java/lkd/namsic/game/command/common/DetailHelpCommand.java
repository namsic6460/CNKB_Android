package lkd.namsic.game.command.common;

import android.app.Notification;

import androidx.annotation.NonNull;

import lkd.namsic.game.KakaoTalk;
import lkd.namsic.game.command.CommonCommand;
import lkd.namsic.game.config.Config;
import lkd.namsic.game.config.Emoji;

public class DetailHelpCommand extends CommonCommand {
    
    @Override
    public void executeCommand(@NonNull String command, @NonNull Notification.Action session) {
        KakaoTalk.reply(session, "=====명령어 목록=====\n" +
                "모든 명령어는 앞쪽에 " + Emoji.focus("n") + " 또는 " + Emoji.focus("ㅜ") + " 를 붙여야합니다\n" +
                "영어는 대소문자에 영향받지 않습니다\n" +
                Emoji.focus("*") + " 이 붙은 명령어는 언제나 사용이 가능한 명령어입니다\n" +
                "() : 필수 명령어, [] : 필수X 명령어, {} : 직접 입력\n" +
                "채팅방 링크 : https://open.kakao.com/o/gsPjUBnd",
            "[봇 버전 : " + Config.VERSION + "]\n\n" +
                "---전체 사용 가능 명령어---\n" +
                Emoji.LIST + " *(도움말/명령어/?/h/help) : 도움말을 표시합니다\n\n" +
                Emoji.LIST + " *(??/hh) : 상세 도움말을 표시합니다\n\n" +
                Emoji.LIST + " (회원가입/가입/register) ({닉네임}) : 회원가입을 합니다\n\n" +
                Emoji.LIST + " *(개발자/dev) : 개발자 정보를 표시합니다\n\n" +
                Emoji.LIST + " *(규칙/룰/rule) : 게임 규칙을 표시합니다\n\n" +
                "\n---유저 명령어---\n" +
                Emoji.LIST + " (휴식/rest) : 10분간 휴식하여 체력과 마나를 모두 회복합니다\n\n" +
                Emoji.LIST + " *(정보/info/i) [{플레이어 닉네임}] : 플레이어의 정보를 표시합니다\n\n" +
                Emoji.LIST + " *(정보/info/i) (아이템/item) ({아이템 이름}) : 아이템의 정보를 표시합니다\n\n" +
                Emoji.LIST + " *(정보/info/i) (장비/equip) ({장비 이름}) : 장비의 정보를 표시합니다\n\n" +
                Emoji.LIST + " *(정보/info/i) (퀘스트/quest) ({퀘스트 이름}) : 퀘스트의 정보를 표시합니다\n\n" +
                Emoji.LIST + " *(정보/info/i) (스킬/skill) ({스킬 이름}) : 스킬의 정보를 표시합니다\n\n" +
                Emoji.LIST + " *(정보/info/i) (몬스터/monster) ({몬스터 이름}) : 몬스터의 정보를 표시합니다\n\n" +
                Emoji.LIST + " *(가방/인벤토리/인벤/inventory/inven) [{페이지}] : 인벤토리를 표시합니다\n\n" +
                Emoji.LIST + " *(장비/equip) : 착용된 장비를 표시합니다\n\n" +
                Emoji.LIST + " *(장비/equip) (인벤토리/인벤/inventory/inven) [{페이지}] : 장비 인벤토리를 표시합니다\n\n" +
                Emoji.LIST + " (장비/equip) ({장비 번호}) : 장비를 착용 또는 착용 해제합니다\n\n" +
                Emoji.LIST + " *(장비/equip) (정보/info) ({장비 번호}) : 보유중인 장비의 정보를 표시합니다\n\n" +
                Emoji.LIST + " (장비/equip) (사용/use) ({장비 부위[.{대상}]}) : 장비를 사용합니다\n\n" +
                Emoji.LIST + " (장비/equip) (분해/dcp) ({장비 번호}) ({강화 제외 장비 이름}) : 장비를 분해합니다\n\n" +
                Emoji.LIST + " (대화/chat) ({Npc 이름}) : Npc 와 대화합니다\n\n" +
                Emoji.LIST + " *(맵/map) : 현재 위치의 정보를 표시합니다\n\n" +
                Emoji.LIST + " *(맵/map) (목록/list) : 이동 가능한 맵 목록을 표시합니다\n\n" +
                Emoji.LIST + " (이동/move) [맵/map] ({x좌표}-{y좌표}) : 좌표로 이동합니다(예시: " +
                Emoji.focus("n move 2-3") + ")\n\n" +
                Emoji.LIST + " (이동/move) [맵/map] ({맵 이름}) : 맵으로 이동합니다(예시 : " +
                Emoji.focus("n move map 시작의 마을") + ")\n\n" +
                Emoji.LIST + " (이동/move) (필드/field) ({x좌표}-{y좌표}) : 현재 맵의 필드 좌표로 이동합니다(예시: " +
                Emoji.focus("n move field 16-10") + ")\n\n" +
                Emoji.LIST + " (줍기/pick/p) : 현재 맵에 떨어진 모든 아이템을 줍습니다\n\n" +
                Emoji.LIST + " (사용/use) ({아이템 이름[.{대상}]}) [{사용 개수}] : 아이템을 일정 개수 사용합니다(예시: " +
                Emoji.focus("n 사용 장비 완화제.무기 5") + ")\n\n" +
                Emoji.LIST + " (먹기/eat) ({아이템 이름}) [{사용 개수}] : 아이템을 일정 개수 먹습니다(예시: " +
                Emoji.focus("n 먹기 양고기 3") + ")\n\n" +
                Emoji.LIST + " (광질/mine) [{광질 레벨}] : 광질을 합니다\n\n" +
                Emoji.LIST + " (감정/appraise/apr) [{아이템 개수}] : 돌을 감정하여 보석을 찾습니다\n\n" +
                Emoji.LIST + " (감정/appraise/apr) (중지/정지/stop) : 감정을 중지합니다(보상의 50%만 적용됩니다)\n\n" +
                Emoji.LIST + " (낚시/fish) [{낚시 레벨}] : 낚시를 합니다\n\n" +
                Emoji.LIST + " (전투/fight/f) ({플레이어 닉네임}/{번호}) : 전투를 시작합니다\n\n" +
                Emoji.LIST + " (리스폰/respawn) : 10초 후 현재 맵의 몬스터를 리스폰시킵니다\n\n" +
                Emoji.LIST + " (모험/adventure/adv) : 모험을 합니다\n\n" +
                Emoji.LIST + " *(농사/농장/farm) : 농장에 대한 설명을 표시합니다\n\n" +
                Emoji.LIST + " (농사/농장/farm) (구입/구매/buy) : 농장을 구매합니다\n\n" +
                Emoji.LIST + " (농사/농장/farm) (심기/plant/p) ({씨앗 이름}) [{개수}] : 농장에 씨앗을 심습니다\n\n" +
                Emoji.LIST + " *(농사/농장/farm) (확인/check/c) : 농장을 확인합니다\n\n" +
                Emoji.LIST + " (농사/농장/farm) (제거/remove/r) ({씨앗 번호}) : 씨앗을 제거합니다\n\n" +
                Emoji.LIST + " (농사/농장/farm) (수확/harvest/h) : 작물을 수확합니다\n\n" +
                Emoji.LIST + " (농사/농장/farm) (업그레이드/upgrade/u) : 농장을 업그레이드 합니다\n\n" +
                Emoji.LIST + " *(스킬/skill) : 스킬에 대한 설명을 표시합니다\n\n" +
                Emoji.LIST + " *(스킬/skill) (목록/list) : 보유하고 있는 스킬 목록을 표시합니다\n\n" +
                Emoji.LIST + " *(스텟/stat) : 스텟 정보를 표시합니다\n\n" +
                Emoji.LIST + " (스텟/stat) ({스텟 이름}) ({숫자}) : SP를 소모하여 스텟을 증가시킵니다\n\n" +
                Emoji.LIST + " *(제작/craft) (아이템/item/장비/equip) : 제작 가능한 아이템 또는 장비 목록을 표시합니다\n\n" +
                Emoji.LIST + " *(제작/craft) (확인/check) ({아이템 또는 장비 이름}) : 아이템 또는 장비의 제작법을 표시합니다\n\n" +
                Emoji.LIST + " *(제작/craft) (하급/low) : 하급 제작법으로 획득 가능한 제작법 목록을 표시합니다\n\n" +
                Emoji.LIST + " *(제작/craft) (중급/middle) : 중급 제작법으로 획득 가능한 제작법 목록을 표시합니다\n\n" +
                Emoji.LIST + " *(제작/craft) (상급/high) : 상급 제작법으로 획득 가능한 제작법 목록을 표시합니다\n\n" +
                Emoji.LIST + " (제작/craft) ({아이템 또는 장비 이름}) [{아이템 개수}] [{레시피 번호}] :" +
                "아이템 또는 장비를 제작합니다\n\n" +
                Emoji.LIST + " (강화/제련/reinforce/r) : 강화에 대한 설명을 표시합니다\n\n" +
                Emoji.LIST + " (강화/제련/reinforce/r) (정보/info) : 장착중인 장비들의 강화 상태를 표시합니다\n\n" +
                Emoji.LIST + " (강화/제련/reinforce/r) ({장비 번호}) : 장비를 강화합니다\n\n" +
                Emoji.LIST + " *(상점/shop) (도움말/명령어/?/help/h) : 상점 도움말을 표시합니다\n\n" +
                Emoji.LIST + " *(상점/shop) (목록/list) : 현재 위치에서 이용 가능한 상점 목록을 표시합니다\n\n" +
                Emoji.LIST + " (상점/shop) ({Npc 이름}) : 상점을 이용합니다\n\n" +
                Emoji.LIST + " *(랭킹/랭크/ranking/rank) [레벨/lv] : 레벨 랭킹을 표시합니다\n\n" +
                Emoji.LIST + " *(랭킹/랭크/ranking/rank) [탑/타워/tower] : 탑 랭킹을 표시합니다\n\n" +
                Emoji.LIST + " *(확률/percent) (광질/mine) : 광질 확률을 표시합니다\n\n" +
                Emoji.LIST + " *(확률/percent) (낚시/fish) : 낚시 확률을 표시합니다\n\n" +
                Emoji.LIST + " *(확률/percent) (모험/adventure/adv) : 모험 보상 확률을 표시합니다\n\n" +
                Emoji.LIST + " *(설정/setting/set) (가방/인벤토리/inventory/inven) (추가/add) ({아이템 이름}) : " +
                "아이템을 인벤토리 표시 우선순위 목록에 추가합니다\n\n" +
                Emoji.LIST + " *(설정/setting/set) (가방/인벤토리/inventory/inven) (제거/remove) ({아이템 이름}) : " +
                "아이템을 인벤토리 표시 우선순위 목록에서 제거합니다\n\n" +
                Emoji.LIST + " *(설정/setting/set) (공개/public) : 자신의 정보를 공개 또는 비공개 상태로 전환합니다\n\n" +
                Emoji.LIST + " *(설정/setting/set) (거점/home) : 현재 위치를 거점으로 설정합니다\n\n" +
                Emoji.LIST + " *(설정/setting/set) (거점/home) (확인/check) : 거점을 확인합니다\n\n"
        );
    }
    
}
