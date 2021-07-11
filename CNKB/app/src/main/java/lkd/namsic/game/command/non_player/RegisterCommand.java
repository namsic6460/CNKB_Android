package lkd.namsic.game.command.non_player;

import android.app.Notification;

import androidx.annotation.NonNull;

import lkd.namsic.game.KakaoTalk;
import lkd.namsic.game.command.NonPlayerCommand;
import lkd.namsic.game.config.Config;
import lkd.namsic.game.config.Emoji;
import lkd.namsic.game.enums.Id;
import lkd.namsic.game.enums.Variable;
import lkd.namsic.game.enums.object_list.BossList;
import lkd.namsic.game.enums.object_list.ItemList;
import lkd.namsic.game.enums.object_list.MonsterList;
import lkd.namsic.game.enums.object_list.NpcList;
import lkd.namsic.game.exception.WeirdCommandException;
import lkd.namsic.game.gameObject.GameMap;
import lkd.namsic.game.gameObject.Player;
import lkd.namsic.game.manager.ChatManager;

public class RegisterCommand extends NonPlayerCommand {

    @Override
    public void executeCommand(@NonNull String sender, @NonNull String image, @NonNull String command,
                               @NonNull String room, boolean isGroupChat, @NonNull Notification.Action session) {
        if (command.equals("")) {
            throw new WeirdCommandException("닉네임을 입력해주세요\n" +
                    "(예시: " + Emoji.focus("n 회원가입 남식") + ")");
        }

        String checkedNickName = Config.getRegex(command, "");
        if(!command.equals(checkedNickName)) {
            KakaoTalk.reply(session, "닉네임에 허용되지 않는 문자가 포함되어 있습니다\n" +
                            "상세 내용은 전체보기에서 확인해주세요",
                    "입력받은 닉네임 : " + command + "\n" +
                            "변경되어야 하는 닉네임: " + checkedNickName + "\n\n" +
                            "[닉네임 규칙]\n" +
                            Emoji.LIST + " 영어 대소문자\n" +
                            Emoji.LIST + " 한글\n" +
                            Emoji.LIST + " 숫자\n" +
                            Emoji.LIST + " 하이픈(-)\n" +
                            Emoji.LIST + " 언더바(_)\n" +
                            Emoji.LIST + " 연속 공백은 최대 1칸" +
                            Emoji.LIST + " 길이는 2~16자");
            return;
        }

        int length = command.length();
        if(length < 2 || length > 16) {
            throw new WeirdCommandException("닉네임의 길이가 2~16자가 되도록 변경해주세요\n" +
                    "현재 " + length + "자 입니다");
        }

        if(Config.PLAYER_ID.get(command) != null) {
            throw new WeirdCommandException("[" + command + "]\n동일한 닉네임을 지닌 플레이어가 존재합니다.\n" +
                    "닉네임을 변경한 후 다시 시도해주세요");
        }

        if(NpcList.findByName(command) != null) {
            throw new WeirdCommandException("[" + command + "]\n동일한 닉네임을 지닌 NPC가 존재합니다.\n" +
                    "닉네임을 변경한 후 다시 시도해주세요");
        }

        if(MonsterList.findByName(command) != null || BossList.findByName(command) != null) {
            throw new WeirdCommandException("[" + command + "]\n동일한 닉네임을 지닌 몬스터가 존재합니다.\n" +
                    "닉네임을 변경한 후 다시 시도해주세요");
        }

        Player player = new Player(sender, command, image, room);
        player.setGroup(isGroupChat);
        player.setVariable(Variable.IS_TUTORIAL, true);

        long objectId = Config.ID_COUNT.get(Id.PLAYER) + 1;
        player.getId().setObjectId(objectId);
        Config.PLAYER_ID.put(command, objectId);
        Config.PLAYER_LIST.put(objectId, new String[] {sender, image});
        Config.ID_COUNT.put(Id.PLAYER, objectId);

        player.getItemRecipe().add(ItemList.GLASS.getId());
        player.getItemRecipe().add(ItemList.GLASS_BOTTLE.getId());
        for(long itemId = ItemList.LOW_HP_POTION.getId(); itemId <= ItemList.HIGH_MP_POTION.getId(); itemId++) {
            player.getItemRecipe().add(itemId);
        }

        GameMap map = Config.loadMap(0, 0);
        map.addEntity(player);
        Config.unloadMap(map);

        player.replyPlayer("회원가입에 성공하였습니다!");
        Config.unloadObject(player);

        Config.loadPlayer(sender, image);
        ChatManager.getInstance().startChat(player, 1L, 1L);
        Config.unloadObject(player);
    }

}
