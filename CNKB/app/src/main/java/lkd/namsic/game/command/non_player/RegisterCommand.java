package lkd.namsic.game.command.non_player;

import android.app.Notification;

import androidx.annotation.NonNull;

import lkd.namsic.game.KakaoTalk;
import lkd.namsic.game.command.NonPlayerCommand;
import lkd.namsic.game.config.Config;
import lkd.namsic.game.config.Emoji;
import lkd.namsic.game.enums.Id;
import lkd.namsic.game.enums.Variable;
import lkd.namsic.game.enums.object.BossList;
import lkd.namsic.game.enums.object.EquipList;
import lkd.namsic.game.enums.object.ItemList;
import lkd.namsic.game.enums.object.MonsterList;
import lkd.namsic.game.enums.object.NpcList;
import lkd.namsic.game.exception.WeirdCommandException;
import lkd.namsic.game.object.GameMap;
import lkd.namsic.game.object.Player;
import lkd.namsic.game.manager.ChatManager;

public class RegisterCommand extends NonPlayerCommand {

    @Override
    public void executeCommand(@NonNull String sender, @NonNull String image, @NonNull String command,
                               @NonNull String room, boolean isGroupChat, @NonNull Notification.Action session) {
        if (command.equals("")) {
            throw new WeirdCommandException("닉네임을 입력해주세요\n" +
                    "(예시: " + Emoji.focus("n 회원가입 홍길동") + ")");
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
                            Emoji.LIST + " 연속 공백은 최대 1칸\n" +
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

        String lowerNickname = command.toLowerCase();
        for(String forbiddenWord : Config.FORBIDDEN_NICKNAME) {
            if(lowerNickname.contains(forbiddenWord)) {
                throw new WeirdCommandException("닉네임에 금지된 단어가 포함되어 있습니다.\n" +
                        "닉네임을 변경한 후 다시 시도해주세요\n(금지된 단어 : " + forbiddenWord + ")");
            }
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
        player.getItemRecipe().add(ItemList.RECIPE.getId());
        player.getItemRecipe().add(ItemList.HIGH_RECIPE.getId());
        player.getItemRecipe().add(ItemList.CONFIRMED_LOW_RECIPE.getId());
        player.getItemRecipe().add(ItemList.CONFIRMED_RECIPE.getId());
        player.getItemRecipe().add(ItemList.CONFIRMED_HIGH_RECIPE.getId());
        player.getItemRecipe().add(ItemList.STONE_LUMP.getId());
        player.getItemRecipe().add(ItemList.LOW_EXP_POTION.getId());
        player.getItemRecipe().add(ItemList.SMALL_GOLD_BAG.getId());
        for(long itemId = ItemList.LOW_HP_POTION.getId(); itemId <= ItemList.HIGH_MP_POTION.getId(); itemId++) {
            player.getItemRecipe().add(itemId);
        }

        player.addEquip(EquipList.BASIC_HELMET.getId());
        player.addEquip(EquipList.BASIC_CHESTPLATE.getId());
        player.addEquip(EquipList.BASIC_LEGGINGS.getId());
        player.addEquip(EquipList.BASIC_SHOES.getId());

        GameMap map = Config.loadMap(0, 0);
        map.addEntity(player);
        Config.unloadMap(map);

        player.replyPlayer("회원가입에 성공하였습니다!\n※ 튜토리얼을 끝낸 후 게임을 플레이 할 수 있습니다 ※");
        Config.unloadObject(player);

        Config.saveConfig();

        Config.loadPlayer(sender, image, room, isGroupChat, true);
        ChatManager.getInstance().startChat(player, 1L, 1L);
        Config.unloadObject(player);
    }

}
