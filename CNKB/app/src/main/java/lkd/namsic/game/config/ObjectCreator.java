package lkd.namsic.game.config;

import android.util.Log;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import lkd.namsic.MainActivity;
import lkd.namsic.game.base.Bool;
import lkd.namsic.game.base.ChatLimit;
import lkd.namsic.game.base.ConcurrentHashSet;
import lkd.namsic.game.base.EquipUse;
import lkd.namsic.game.base.Int;
import lkd.namsic.game.base.LoNg;
import lkd.namsic.game.enums.EquipType;
import lkd.namsic.game.enums.Id;
import lkd.namsic.game.enums.MapType;
import lkd.namsic.game.enums.MonsterType;
import lkd.namsic.game.enums.StatType;
import lkd.namsic.game.enums.Variable;
import lkd.namsic.game.enums.WaitResponse;
import lkd.namsic.game.enums.object_list.EquipList;
import lkd.namsic.game.enums.object_list.ItemList;
import lkd.namsic.game.enums.object_list.MapList;
import lkd.namsic.game.enums.object_list.MonsterList;
import lkd.namsic.game.enums.object_list.NpcList;
import lkd.namsic.game.enums.object_list.QuestList;
import lkd.namsic.game.event.DamageEvent;
import lkd.namsic.game.event.DamagedEvent;
import lkd.namsic.game.event.MineEvent;
import lkd.namsic.game.gameObject.Chat;
import lkd.namsic.game.gameObject.Entity;
import lkd.namsic.game.gameObject.Equipment;
import lkd.namsic.game.gameObject.GameMap;
import lkd.namsic.game.gameObject.GameObject;
import lkd.namsic.game.gameObject.Item;
import lkd.namsic.game.gameObject.Monster;
import lkd.namsic.game.gameObject.Npc;
import lkd.namsic.game.gameObject.Player;
import lkd.namsic.game.gameObject.Quest;
import lkd.namsic.setting.Logger;

public class ObjectCreator {
    
    //TODO : 생성자에서 name + id를 list 의 enum 으로 대체
    //TODO : Use, EquipUse, Event 등등은 직렬화 대신 코드 내에 저장해두고 직접 호출하는 방식으로 변경
    //TODO : 장비를 가진 몬스터는 생성 떄 장비를 복사하지 말고 드롭할 때 원래 엔티티에서 장비를 가져와서 드롭  

    public static void start(Button button) {
        Logger.i("ObjectMaker", "Making objects...");

        Thread thread = new Thread(() -> {
            MainActivity.mainActivity.runOnUiThread(() -> button.setEnabled(false));

            try {
                Config.IGNORE_FILE_LOG = true;

                createItems();
                createEquips();
                createMonsters();
                createMaps();
                createChats();
                createQuests();
                createNpc();

                Config.IGNORE_FILE_LOG = false;
                Logger.i("ObjectMaker", "Object making is done!");

                Config.save();
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("namsic!", Config.errorString(e));
                Logger.e("ObjectMaker", e);
            } finally {
                MainActivity.mainActivity.runOnUiThread(() -> button.setEnabled(true));
            }
        });

        MainActivity.startThread(thread);
    }

    private static void createItem(@NonNull String name, @NonNull String description) {
        Item item = new Item(name, description);
        Config.unloadObject(item);
    }

    private static void createItem(@NonNull String name, @NonNull String description, int handleLv) {
        Item item = new Item(name, description);
        item.getHandleLv().set(handleLv);
        Config.unloadObject(item);
    }

    private static void createItems() {
        Item item;

        createItem("돌멩이", "평범한 돌멩이다");
        createItem("나뭇가지", "평범한 나뭇가지다");
        createItem("나뭇잎", "평범한 나뭇잎이다");
        createItem("잡초", "평범한 잡초다");

        item = new Item("골드 주머니", "골드가 소량 들어간 주머니다");
        item.setUse((self, other) -> {
            int money = new Random().nextInt(10 * self.getLv().get()) + 500;

            self.addMoney(money);
            return "골드 주머니를 사용하여 " + money + "G 를 획득했습니다\n" + Emoji.GOLD + " 골드: " + self.getMoney() + "\n";
        });
        Config.unloadObject(item);

        item = new Item("골드 보따리", "골드가 들어간 주머니다");
        item.getHandleLv().set(5);
        item.setUse((self, other) -> {
            int money = new Random().nextInt(50 * self.getLv().get()) + 3000;

            self.addMoney(money);
            return "골드 보따리를 사용하여 " + money + "G 를 획득했습니다\n" + Emoji.GOLD + " 골드: " + self.getMoney() + "\n";
        });
        Config.unloadObject(item);

        createItem("약초", "약의 기운이 있는 풀이다");

        item = new Item("마나 조각", "마나의 기운이 있는 고체 마나 파편이다");
        for(long itemId = ItemList.RED_SPHERE.getId(); itemId < ItemList.WHITE_SPHERE.getId(); itemId++) {
            long itemId_ = itemId;
            
            item.addRecipe(new HashMap<Long, Integer>() {{
                put(ItemList.NONE.getId(), 3);

                put(itemId_, 1);
            }}, true);
        }
        Config.unloadObject(item);

        createItem("흙", "땅에서 흔히 볼 수 있는 평번한 흙이다");
        createItem("모래", "땅에서 흔히 볼 수 있는 평번한 모래다");

        item = new Item("유리", "투명한 고체 물질이다");
        item.getHandleLv().set(2);
        item.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.SAND.getId(), 3);
            put(ItemList.COAL.getId(), 1);
        }}, true);
        Config.unloadObject(item);

        item = new Item("유리병", "유리를 가공하여 만든 병이다");
        item.getHandleLv().set(2);
        item.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.GLASS.getId(), 2);
            put(ItemList.COAL.getId(), 1);
        }}, true);
        Config.unloadObject(item);

        item = new Item("하급 체력 포션", "최대 체력의 15%를 회복시켜주는 포션이다");
        item.getHandleLv().set(2);
        item.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.LEAF.getId(), 3);
            put(ItemList.HERB.getId(), 3);
            put(ItemList.GLASS_BOTTLE.getId(), 1);
            put(ItemList.COAL.getId(), 1);
        }}, true);
        item.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.NONE.getId(), 3);

            put(ItemList.HP_POTION.getId(), 1);
        }}, true);
        item.setUse((self, other) -> {
            self.addBasicStat(StatType.HP, (int) (self.getStat(StatType.MAXHP) * 0.15));
            return "최대 체력의 15%를 회복했습니다\n현재 체력: " + self.getDisplayHp() + "\n";
        });
        Config.unloadObject(item);

        item = new Item("중급 체력 포션", "최대 체력의 50%를 회복시켜주는 포션이다");
        item.getHandleLv().set(8);
        item.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.HERB.getId(), 5);
            put(ItemList.LOW_HP_POTION.getId(), 1);
            put(ItemList.RED_STONE.getId(), 3);
            put(ItemList.GLOW_STONE.getId(), 1);
            put(ItemList.FIRE_QUARTZ.getId(), 3);
        }}, true);
        item.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.NONE.getId(), 2);

            put(ItemList.HIGH_HP_POTION.getId(), 1);
        }}, true);
        item.setUse((self, other) -> {
            self.addBasicStat(StatType.HP, self.getStat(StatType.MAXHP) / 2);
            return "최대 체력의 50%를 회복했습니다\n현재 체력: " + self.getDisplayHp() + "\n";
        });
        Config.unloadObject(item);

        item = new Item("상급 체력 포션", "체력을 모두 회복시켜주는 포션이다");
        item.getHandleLv().set(10);
        item.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.HERB.getId(), 30);
            put(ItemList.HP_POTION.getId(), 1);
            put(ItemList.RED_STONE.getId(), 10);
            put(ItemList.GLOW_STONE.getId(), 10);
            put(ItemList.HARD_COAL.getId(), 1);
            put(ItemList.LIQUID_STONE.getId(), 3);
        }}, true);
        item.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.HIGH_MP_POTION.getId(), 1);
            put(ItemList.LAPIS_RED_STONE.getId(), 1);
        }}, true);
        item.setUse((self, other) -> {
            self.setBasicStat(StatType.HP, self.getStat(StatType.MAXHP));
            return "체력을 100% 회복했습니다\n현재 체력: " + self.getDisplayHp() + "\n";
        });
        Config.unloadObject(item);

        item = new Item("하급 마나 포션", "최대 마나의 15%를 회복시켜주는 포션이다");
        item.getHandleLv().set(2);
        item.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.LEAF.getId(), 3);
            put(ItemList.PIECE_OF_MANA.getId(), 3);
            put(ItemList.GLASS_BOTTLE.getId(), 1);
            put(ItemList.COAL.getId(), 1);
        }}, true);
        item.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.NONE.getId(), 3);

            put(ItemList.MP_POTION.getId(), 1);
        }}, true);
        item.setUse((self, other) -> {
            self.addBasicStat(StatType.MN, (int) (self.getStat(StatType.MAXMN) * 0.15));
            return "최대 마나의 15%를 회복했습니다\n현재 마나: " + self.getDisplayMn() + "\n";
        });
        Config.unloadObject(item);

        item = new Item("중급 마나 포션", "최대 마나의 50%를 회복시켜주는 포션이다");
        item.getHandleLv().set(8);
        item.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.PIECE_OF_MANA.getId(), 5);
            put(ItemList.LOW_MP_POTION.getId(), 1);
            put(ItemList.LAPIS.getId(), 3);
            put(ItemList.GLOW_STONE.getId(), 1);
            put(ItemList.FIRE_QUARTZ.getId(), 3);
        }}, true);
        item.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.NONE.getId(), 2);

            put(ItemList.HIGH_MP_POTION.getId(), 1);
        }}, true);
        item.setUse((self, other) -> {
            self.setBasicStat(StatType.MN, self.getStat(StatType.MAXMN) / 2);
            return "최대 마나의 50%를 회복했습니다\n현재 마나: " + self.getDisplayMn() + "\n";
        });
        Config.unloadObject(item);

        item = new Item("상급 마나 포션", "마나를 모두 회복시켜주는 포션이다");
        item.getHandleLv().set(10);
        item.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.PIECE_OF_MANA.getId(), 30);
            put(ItemList.MP_POTION.getId(), 1);
            put(ItemList.LAPIS.getId(), 10);
            put(ItemList.GLOW_STONE.getId(), 10);
            put(ItemList.HARD_COAL.getId(), 1);
            put(ItemList.LIQUID_STONE.getId(), 3);
        }}, true);
        item.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.HIGH_HP_POTION.getId(), 1);
            put(ItemList.LAPIS_RED_STONE.getId(), 1);
        }}, true);
        item.setUse((self, other) -> {
            self.setBasicStat(StatType.MN, self.getStat(StatType.MAXMN));
            return "마나를 100% 회복했습니다\n현재 마나: " + self.getDisplayMn() + "\n";
        });
        Config.unloadObject(item);

        item = new Item("돌", "가장 기본적인 광석이다");
        item.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.COBBLE_STONE.getId(), 10);
            put(ItemList.COAL.getId(), 5);
        }}, true);
        item.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.MAGIC_STONE.getId(), 1);
            put(ItemList.GOD_WATER.getId(), 1);
        }}, true);
        Config.unloadObject(item);

        createItem("석탄", "불이 잘 붙는 흔한 광물이다", 2);

        createItem("석영", "약간 투명한 보석이다", 3);
        createItem("구리", "붉으스름한 광물이다", 3);
        createItem("납", "다양한 곳에서 사용되는 광물이다", 3);
        createItem("주석", "다양한 곳에서 사용되는 광물이다", 3);
        createItem("니켈", "다양한 곳에서 사용되는 광물이다", 3);

        createItem("철", "매우 많이 사용되는 일반적인 광물이다", 4);
        createItem("리튬", "다양한 곳에서 사용되는 광물이다", 4);

        createItem("청금석", "푸른색을 띄는 광물이다", 5);
        createItem("레드스톤", "붉은색을 띄는 광물이다", 5);

        createItem("은", "회백색을 띄며 신비한 기운을 내뿜는 광물이다", 6);

        item = new Item("금", "밝은 노란색을 띄는 보석이다");
        item.getHandleLv().set(7);
        item.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.NONE.getId(), 3);

            put(ItemList.DARK_QUARTZ.getId(), 3);
            put(ItemList.WHITE_GOLD.getId(), 1);
        }}, true);
        Config.unloadObject(item);

        createItem("발광석", "스스로 빛을 내는 광물이다", 7);

        createItem("화염 석영", "주변의 열기를 흡수하는 석영이다", 8);
        createItem("암흑 석영", "주변의 빛을 흡수하는 석영이다", 8);

        item = new Item("명청석", "스스로 빛을 내는 마나의 기운이 담긴 푸른 광물이다");
        item.getHandleLv().set(9);
        item.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.LAPIS.getId(), 3);
            put(ItemList.GLOW_STONE.getId(), 5);
        }}, true);
        item.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.NONE.getId(), 10);

            put(ItemList.GLOW_STONE.getId(), 10);
            put(ItemList.LAPIS_RED_STONE.getId(), 1);
        }}, true);
        Config.unloadObject(item);

        item = new Item("명적석", "스스로 빛을 내는 힘의 기운이 담긴 붉은 광물이다");
        item.getHandleLv().set(9);
        item.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.RED_STONE.getId(), 3);
            put(ItemList.GLOW_STONE.getId(), 5);
        }}, true);
        item.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.NONE.getId(), 10);

            put(ItemList.GLOW_STONE.getId(), 10);
            put(ItemList.LAPIS_RED_STONE.getId(), 1);
        }}, true);
        Config.unloadObject(item);

        item = new Item("백금", "주변의 어둠을 흡수하는 보석이다");
        item.getHandleLv().set(10);
        item.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.GOLD.getId(), 5);
            put(ItemList.LIQUID_STONE.getId(), 1);
        }}, true);
        item.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.NONE.getId(), 5);

            put(ItemList.AITUME.getId(), 1);
        }}, true);
        Config.unloadObject(item);

        createItem("무연탄", "일반 석탄보다 훨씬 질이 좋은 광물이다", 10);
        createItem("티타늄", "꽤나 단단한 광물이다", 10);

        item = new Item("투명석", "거의 보이지 않을 정도로 투명한 보석이다");
        item.getHandleLv().set(11);
        item.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.QUARTZ.getId(), 10);
            put(ItemList.GLOW_STONE.getId(), 1);
            put(ItemList.FIRE_QUARTZ.getId(), 10);
            put(ItemList.DARK_QUARTZ.getId(), 1);
            put(ItemList.WHITE_GOLD.getId(), 5);
        }}, true);
        Config.unloadObject(item);

        item = new Item("다이아몬드", "매우 단단하고 아름다운 보석이다");
        item.getHandleLv().set(11);
        item.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.TITANIUM.getId(), 10);
            put(ItemList.LIQUID_STONE.getId(), 3);
        }}, true);
        Config.unloadObject(item);

        createItem("오리하르콘", "일반적으로 가장 단단하다고 여겨지는 광물이다", 12);

        item = new Item("적청석", "힘과 마나, 두개의 상반된 기운을 한번에 가진 광물이다");
        item.getHandleLv().set(13);
        item.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.GLOW_LAPIS.getId(), 50);
            put(ItemList.GLOW_RED_STONE.getId(), 50);
            put(ItemList.WHITE_GOLD.getId(), 20);
            put(ItemList.DIAMOND.getId(), 10);
        }}, true);
        Config.unloadObject(item);

        item = new Item("랜디움", "부서질수록 단단해자고, 스스로 복구되는 광물이다");
        item.getHandleLv().set(13);
        item.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.WHITE_GOLD.getId(), 10);
            put(ItemList.AITUME.getId(), 1);
        }}, true);
        Config.unloadObject(item);

        item = new Item("에이튬", "공기처럼 가벼우나 오리하르콘만큼 단단한 광물이다");
        item.getHandleLv().set(13);
        item.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.WHITE_GOLD.getId(), 10);
            put(ItemList.LANDIUM.getId(), 1);
        }}, true);
        Config.unloadObject(item);

        createItem("가넷", "검붉은색을 띄는 보석이다", 11);
        createItem("자수정", "보라색을 띄는 보석이다", 11);
        createItem("아쿠아마린", "파란색을 띄는 보석이다", 11);
        createItem("에메랄드", "녹색을 띄는 보석이다", 11);
        createItem("진주", "흰색을 띄는 보석이다", 11);
        createItem("루비", "붉은색을 띄는 보석이다", 11);
        createItem("페리도트", "연두색을 띄는 보석이다", 11);
        createItem("사파이어", "남색을 띄는 보석이다", 11);
        createItem("오팔", "다양한 색을 띄는 보석이다", 11);
        createItem("토파즈", "주황색을 띄는 보석이다", 11);
        createItem("터키석", "청록색을 띄는 보석이다", 11);

        createItem("쓰레기", "누가 이런걸 물에 버렸어?");
        createItem("물풀", "누가 이런걸 물에 버렸어?");

        item = new Item("(일반) 금강모치", "일반 등급의 물고기다");
        item.getHandleLv().set(2);
        item.setCanEat(true);
        item.setEatBuff(-1, StatType.HP, 10);
        Config.unloadObject(item);
        
        item = new Item("(일반) 미꾸라지", "일반 등급의 물고기다");
        item.getHandleLv().set(2);
        item.setCanEat(true);
        item.setEatBuff(-1, StatType.HP, 10);
        Config.unloadObject(item);

        item = new Item("(일반) 붕어", "일반 등급의 물고기다");
        item.getHandleLv().set(2);
        item.setCanEat(true);
        item.setEatBuff(-1, StatType.HP, 10);
        Config.unloadObject(item);

        item = new Item("(일반) 송사리", "일반 등급의 물고기다");
        item.getHandleLv().set(2);
        item.setCanEat(true);
        item.setEatBuff(-1, StatType.HP, 10);
        Config.unloadObject(item);

        item = new Item("(일반) 피라미", "일반 등급의 물고기다");
        item.getHandleLv().set(2);
        item.setCanEat(true);
        item.setEatBuff(-1, StatType.HP, 10);
        Config.unloadObject(item);

        item = new Item("(희귀) 망둑어", "희귀 등급의 물고기다");
        item.getHandleLv().set(3);
        item.setCanEat(true);
        item.setEatBuff(-1, StatType.HP, 20);
        Config.unloadObject(item);

        item = new Item("(희귀) 미꾸라지", "희귀 등급의 물고기다");
        item.getHandleLv().set(3);
        item.setCanEat(true);
        item.setEatBuff(-1, StatType.HP, 20);
        Config.unloadObject(item);

        item = new Item("(희귀) 배스", "희귀 등급의 물고기다");
        item.getHandleLv().set(3);
        item.setCanEat(true);
        item.setEatBuff(-1, StatType.HP, 20);
        Config.unloadObject(item);

        item = new Item("(희귀) 살치", "희귀 등급의 물고기다");
        item.getHandleLv().set(3);
        item.setCanEat(true);
        item.setEatBuff(-1, StatType.HP, 20);
        Config.unloadObject(item);

        item = new Item("(희귀) 쏘가리", "희귀 등급의 물고기다");
        item.getHandleLv().set(3);
        item.setCanEat(true);
        item.setEatBuff(-1, StatType.HP, 20);
        Config.unloadObject(item);

        item = new Item("(희귀) 은어", "희귀 등급의 물고기다");
        item.getHandleLv().set(3);
        item.setCanEat(true);
        item.setEatBuff(-1, StatType.HP, 20);
        Config.unloadObject(item);

        item = new Item("(특별) 강준치", "특별 등급의 물고기다");
        item.getHandleLv().set(5);
        item.setCanEat(true);
        item.setEatBuff(-1, StatType.HP, 45);
        Config.unloadObject(item);

        item = new Item("(특별) 망둑어", "특별 등급의 물고기다");
        item.getHandleLv().set(5);
        item.setCanEat(true);
        item.setEatBuff(-1, StatType.HP, 45);
        Config.unloadObject(item);

        item = new Item("(특별) 메기", "특별 등급의 물고기다");
        item.getHandleLv().set(5);
        item.setCanEat(true);
        item.setEatBuff(-1, StatType.HP, 45);
        Config.unloadObject(item);

        item = new Item("(특별) 뱀장어", "특별 등급의 물고기다");
        item.getHandleLv().set(5);
        item.setCanEat(true);
        item.setEatBuff(-1, StatType.HP, 45);
        Config.unloadObject(item);

        item = new Item("(특별) 산천어", "특별 등급의 물고기다");
        item.getHandleLv().set(5);
        item.setCanEat(true);
        item.setEatBuff(-1, StatType.HP, 45);
        Config.unloadObject(item);

        item = new Item("(특별) 숭어", "특별 등급의 물고기다");
        item.getHandleLv().set(5);
        item.setCanEat(true);
        item.setEatBuff(-1, StatType.HP, 45);
        Config.unloadObject(item);

        item = new Item("(특별) 쏘가리", "특별 등급의 물고기다");
        item.getHandleLv().set(5);
        item.setCanEat(true);
        item.setEatBuff(-1, StatType.HP, 45);
        Config.unloadObject(item);

        item = new Item("(특별) 연어", "특별 등급의 물고기다");
        item.getHandleLv().set(5);
        item.setCanEat(true);
        item.setEatBuff(-1, StatType.HP, 45);
        Config.unloadObject(item);

        item = new Item("(특별) 은어", "특별 등급의 물고기다");
        item.getHandleLv().set(5);
        item.setCanEat(true);
        item.setEatBuff(-1, StatType.HP, 45);
        Config.unloadObject(item);

        item = new Item("(특별) 잉어", "특별 등급의 물고기다");
        item.getHandleLv().set(5);
        item.setCanEat(true);
        item.setEatBuff(-1, StatType.HP, 45);
        Config.unloadObject(item);

        item = new Item("(유일) 강준치", "유일 등급의 물고기다");
        item.getHandleLv().set(8);
        item.setCanEat(true);
        item.setEatBuff(-1, StatType.HP, 80);
        Config.unloadObject(item);

        item = new Item("(유일) 메기", "유일 등급의 물고기다");
        item.getHandleLv().set(8);
        item.setCanEat(true);
        item.setEatBuff(-1, StatType.HP, 80);
        Config.unloadObject(item);

        item = new Item("(유일) 뱀장어", "유일 등급의 물고기다");
        item.getHandleLv().set(8);
        item.setCanEat(true);
        item.setEatBuff(-1, StatType.HP, 80);
        Config.unloadObject(item);

        item = new Item("(유일) 산천어", "유일 등급의 물고기다");
        item.getHandleLv().set(8);
        item.setCanEat(true);
        item.setEatBuff(-1, StatType.HP, 80);
        Config.unloadObject(item);

        item = new Item("(유일) 숭어", "유일 등급의 물고기다");
        item.getHandleLv().set(8);
        item.setCanEat(true);
        item.setEatBuff(-1, StatType.HP, 80);
        Config.unloadObject(item);

        item = new Item("(유일) 연어", "유일 등급의 물고기다");
        item.getHandleLv().set(8);
        item.setCanEat(true);
        item.setEatBuff(-1, StatType.HP, 80);
        Config.unloadObject(item);

        item = new Item("(유일) 잉어", "유일 등급의 물고기다");
        item.getHandleLv().set(8);
        item.setCanEat(true);
        item.setEatBuff(-1, StatType.HP, 80);
        Config.unloadObject(item);
        
        item = new Item("(전설) 다금바리", "전설 등급의 물고기다");
        item.getHandleLv().set(10);
        item.setCanEat(true);
        item.setEatBuff(-1, StatType.HP, 200);
        Config.unloadObject(item);

        item = new Item("(전설) 돗돔", "전설 등급의 물고기다");
        item.getHandleLv().set(10);
        item.setCanEat(true);
        item.setEatBuff(-1, StatType.HP, 200);
        Config.unloadObject(item);

        item = new Item("(전설) 자치", "전설 등급의 물고기다");
        item.getHandleLv().set(10);
        item.setCanEat(true);
        item.setEatBuff(-1, StatType.HP, 200);
        Config.unloadObject(item);

        item = new Item("(전설) 쿠니마스", "전설 등급의 물고기다");
        item.getHandleLv().set(10);
        item.setCanEat(true);
        item.setEatBuff(-1, StatType.HP, 200);
        Config.unloadObject(item);

        item = new Item("(신화) 실러캔스", "신화 등급의 물고기다");
        item.getHandleLv().set(12);
        item.setCanEat(true);
        item.setEatBuff(-1, StatType.HP, 500);
        Config.unloadObject(item);

        item = new Item("(신화) 폐어", "신화 등급의 물고기다");
        item.getHandleLv().set(12);
        item.setCanEat(true);
        item.setEatBuff(-1, StatType.HP, 500);
        Config.unloadObject(item);


        item = new Item("전투 비활성화권(1일)", "PvP를 1일간 비활성화 하기 위해 필요한 아이템이다(중첩 불가)");
        item.setUse((self, other) -> {
            ((Player) self).setPvp(false, 1);
            return "1일간 PvP를 비활성화했습니다(중첩 불가)\n활성화 설정은 설정 명령어로 할 수 있습니다\n";
        });
        Config.unloadObject(item);

        item = new Item("전투 비활성화권(7일)", "PvP를 7일간 비활성화 하기 위해 필요한 아이템이다(중첩 불가)");
        item.setUse((self, other) -> {
            ((Player) self).setPvp(false, 7);
            return "7일간 PvP를 비활성화했습니다(중첩 불가)\n활성화 설정은 설정 명령어로 할 수 있습니다\n";
        });
        Config.unloadObject(item);

        item = new Item("스텟 포인트", "스텟 포인트(SP)를 50% 확률로 1 얻을 수 있게 해주는 아이템이다");
        item.setUse((self, other) -> {
            if (Math.random() < 0.5) {
                Player player = (Player) self;

                player.getSp().add(1);
                return "스텟 포인트를 1 획득하였습니다\n" + Emoji.SP + " 스텟 포인트: " + player.getSp().get() + "\n";
            } else {
                return "스텟 포인트를 획득하는데에 실패했습니다...";
            }
        });
        Config.unloadObject(item);

        item = new Item("모험 스텟", "모험 스텟(ADV)를 50% 확률로 1 얻을 수 있게 해주는 아이템이다");
        item.setUse((self, other) -> {
            if (Math.random() < 0.5) {
                Player player = (Player) self;

                player.getAdv().add(1);
                return "모험 스텟을 1 획득하였습니다\n" + Emoji.ADV + " 모험: " + player.getAdv().get() + "\n";
            } else {
                return "모험 스텟을 획득하는데에 실패했습니다...";
            }
        });
        Config.unloadObject(item);

        item = new Item("하급 제작법", "하급 아이템 또는 하급 장비의 제작법을 무작위로 1개 획득할 수 있다(중복 가능)");
        item.setUse((self, other) -> {
            Random random = new Random();
            Player player = (Player) self;

            if(random.nextBoolean()) {
                long itemId = RandomList.lowRecipeItems.get(random.nextInt(RandomList.lowRecipeItems.size()));

                player.getItemRecipe().add(itemId);
                return ItemList.findById(itemId) + " 의 제작법을 획득했습니다";
            } else {
                long equipId = RandomList.lowRecipeEquips.get(random.nextInt(RandomList.lowRecipeEquips.size()));

                player.getEquipRecipe().add(equipId);
                return EquipList.findById(equipId) + " 의 제작법을 획득했습니다";
            }
        });
        Config.unloadObject(item);

        item = new Item("중급 제작법", "중급 아이템 또는 중급 장비의 제작법을 무작위로 1개 획득할 수 있다(중복 가능)");
        item.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.LOW_CRAFT_GUIDE.getId(), 6);
        }}, true);
        item.setUse((self, other) -> {
            Random random = new Random();
            Player player = (Player) self;

            if(random.nextBoolean()) {
                long itemId = RandomList.middleRecipeItems.get(random.nextInt(RandomList.middleRecipeItems.size()));

                player.getItemRecipe().add(itemId);
                return ItemList.findById(itemId) + " 의 제작법을 획득했습니다";
            } else {
                long equipId = RandomList.middleRecipeEquips.get(random.nextInt(RandomList.middleRecipeEquips.size()));

                player.getEquipRecipe().add(equipId);
                return EquipList.findById(equipId) + " 의 제작법을 획득했습니다";
            }
        });
        Config.unloadObject(item);

        item = new Item("상급 제작법", "상급 아이템 또는 상급 장비의 제작법을 무작위로 1개 획득할 수 있다(중복 가능)");
        item.setUse((self, other) -> {
            Random random = new Random();
            Player player = (Player) self;

            //TODO : 고급 장비 생성 시 주석 해제
//            if(random.nextBoolean()) {
                long itemId = RandomList.highRecipeItems.get(random.nextInt(RandomList.highRecipeItems.size()));

                player.getItemRecipe().add(itemId);
                return ItemList.findById(itemId) + " 의 제작법을 획득했습니다";
//            } else {
//                long equipId = RandomList.highRecipeEquips.get(random.nextInt(RandomList.highRecipeEquips.size()));
//
//                player.getEquipRecipe().add(equipId);
//                return ItemList.findById(equipId) + " 의 제작법을 획득했습니다";
//            }
        });
        Config.unloadObject(item);

        createItem("붉은색 구체", "불의 기운을 담고 있는 구체다");
        createItem("녹색 구체", " 독의 기운을 담고 있는 구체다");
        createItem("연녹색 구체", "풀의 기운을 담고 있는 구체다");
        createItem("파란색 구체", "물의 기운을 담고 있는 구체다");
        createItem("갈색 구체", "얼음의 기운을 담고 있는 구체다");
        createItem("회색 구체", "흙의 기운을 담고 있는 구체다");
        createItem("은색 구체", "돌의 기운을 담고 있는 구체다");
        createItem("연회색 구체", "반사의 기운을 담고 있는 구체다");
        createItem("노란색 구체", "쇠의 기운을 담고 있는 구체다");
        createItem("검은색 구체", " 마법의 기운을 담고 있는 구체다");
        createItem("붉은색 구체", "어둠의 기운을 담고 있는 구체다");
        createItem("흰색 구체", "빛의 기운을 담고 있는 구체다");

        item = new Item("하급 경험치 포션", "경험치를 소량 제공해주는 포션이다");
        item.setUse((self, other) -> {
            long exp = new Random().nextInt(500 * self.getLv().get()) + 20000;

            Player player = (Player) self;
            player.addExp(exp);
            return "하급 경험치 포션을 사용하여 " + exp + " 경험치를 획득했습니다\n" + Emoji.LV + " 레벨: " + player.getDisplayLv() + "\n";
        });
        Config.unloadObject(item);

        item = new Item("중급 경험치 포션", "경험치를 제공해주는 포션이다");
        item.getHandleLv().set(5);
        item.setUse((self, other) -> {
            long exp = new Random().nextInt(8000 * self.getLv().get()) + 150_000;

            Player player = (Player) self;
            player.addExp(exp);
            return "중급 경험치 포션을 사용하여 " + exp + " 경험치를 획득했습니다\n" + Emoji.LV + " 레벨: " + player.getDisplayLv() + "\n";
        });
        Config.unloadObject(item);

        item = new Item("상급 경험치 포션", "경험치를 대량 제공해주는 포션이다");
        item.getHandleLv().set(10);
        item.setUse((self, other) -> {
            long exp = new Random().nextInt(100_000 * self.getLv().get()) + 3_000_000;

            Player player = (Player) self;
            player.addExp(exp);
            return "상급 경험치 포션을 사용하여 " + exp + " 경험치를 획득했습니다\n" + Emoji.LV + " 레벨: " + player.getDisplayLv() + "\n";
        });
        Config.unloadObject(item);

        createItem("하급 모험의 증표", "간단한 모험을 완수했다는 증표이다");
        createItem("중급 모험의 증표", "모험을 완수했다는 증표이다", 5);
        createItem("상급 모험의 증표", "어려운 모험을 완수했다는 증표이다", 10);

        item = new Item("무색의 구체", "무슨 색이든 될 수 있는 구체이다");
        item.setUse((self, other) -> {
            //101 ~ 111
            long itemId = new Random().nextInt(11) + 101;
            self.addItem(itemId, 1, false);

            return ItemList.findById(itemId) + " " + 1 + "개를 획득헀습니다\n" +
                    "현재 개수: " + self.getItem(itemId) + "\n";
        });
        Config.unloadObject(item);

        createItem("하급 강화석", "0~4강 무기를 강화할 수 있는 아이템이다");
        createItem("중급 강화석", "5~9강 무기를 강화할 수 있는 아이템이다", 5);
        createItem("상급 강화석", "10~14강 무기를 강화할 수 있는 아이템이다", 10);

        createItem("하급 부적 파편", "장착은 불가하지만 약간의 부적의 기운을 띄고 있는 파편이다");
        createItem("중급 부적 파편", "장착은 불가하지만 부적의 기운을 띄고 있는 파편이다", 4);
        createItem("상급 부적 파편", "장착은 불가하지만 강한 부적의 기운을 띄고 있는 파편이다", 8);

        createItem("보석 조각", "장착은 불가하지만 조각을 모으면 보석을 만들 수 있을 것 같다", 3);
        createItem("보석 연마제", "보석을 연마할 수 있는 아이템이다", 3);
        createItem("빛나는 보석 연마제", "중심 보석을 연마할 수 있는 아이템이다", 8);

        createItem("무기 완화제", "무기의 제한 레벨을 낮출 수 있는 아이템이다", 3);

        createItem("하급 광부의 증표", "간단한 광질을 완수했다는 증표이다");
        createItem("중급 광부의 증표", "광질을 완수했다는 증표이다", 5);
        createItem("상급 광부의 증표", "어려운 광질을 완수했다는 증표이다", 10);
        createItem("하급 낚시꾼의 증표", "간단한 낚시를 완수했다는 증표이다");
        createItem("중급 낚시꾼의 증표", "낚시를 완수했다는 증표이다", 5);
        createItem("상급 낚시꾼의 증표", "어려운 낚시를 완수했다는 증표이다", 10);
        createItem("하급 사냥꾼의 증표", "간단한 사냥을 완수했다는 증표이다");
        createItem("중급 사냥꾼의 증표", "사냥을 완수했다는 증표이다", 5);
        createItem("상급 사냥꾼의 증표", "어려운 사냥을 완수했다는 증표이다", 10);

        createItem("하급 증표", "간단한 노동을 완수했다는 증표이다");
        createItem("중급 증표", "노동을 완수했다는 증표이다", 5);
        createItem("상급 증표", "어려운 노동을 완수했다는 증표이다", 10);

        item = new Item("하급 부적", "하급 부적 1개를 뽑을 수 있는 아이템이다");
        item.getHandleLv().set(2);
        item.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.PIECE_OF_LOW_AMULET.getId(), 10);
        }}, true);
        item.setUse((self, other) -> {
            long equipId = RandomList.lowAmulets.get(new Random().nextInt(RandomList.lowAmulets.size()));
            self.addEquip(equipId);

            return EquipList.findById(equipId) + " (을/를) 획득하였습니다";
        });
        Config.unloadObject(item);

        item = new Item("중급 부적", "중급 부적 1개를 뽑을 수 있는 아이템이다");
        item.getHandleLv().set(5);
        item.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.PIECE_OF_AMULET.getId(), 10);
        }}, true);
        item.setUse((self, other) -> {
            long equipId = RandomList.middleAmulets.get(new Random().nextInt(RandomList.middleAmulets.size()));
            self.addEquip(equipId);

            return EquipList.findById(equipId) + " (을/를) 획득하였습니다";
        });
        Config.unloadObject(item);

        item = new Item("상급 부적", "상급 부적 1개를 뽑을 수 있는 아이템이다");
        item.getHandleLv().set(8);
        item.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.PIECE_OF_HIGH_AMULET.getId(), 10);
        }}, true);
        item.setUse((self, other) -> {
            long equipId = RandomList.highAmulets.get(new Random().nextInt(RandomList.highAmulets.size()));
            self.addEquip(equipId);

            return EquipList.findById(equipId) + " (을/를) 획득하였습니다";
        });
        Config.unloadObject(item);

        item = new Item("양고기", "양에서 나온 고기다");
        item.setCanEat(true);
        item.setEatBuff(100000, StatType.ATS, 20);
        item.setEatBuff(100000, StatType.ATK, 1);
        Config.unloadObject(item);
        
        createItem("양가죽", "양에서 나온 가죽이다");
        createItem("양털", "양에서 나온 털이다");

        item = new Item("돼지고기", "돼지에서 나온 고기다");
        item.getHandleLv().set(2);
        item.setCanEat(true);
        item.setEatBuff(100000, StatType.ACC, 10);
        item.setEatBuff(100000, StatType.ATS, 10);
        Config.unloadObject(item);
        
        createItem("돼지머리", "행운의 상징으로 자주 쓰이는 아이템이다", 2);

        item = new Item("소고기", "소에게서 나온 고기다");
        item.getHandleLv().set(2);
        item.setCanEat(true);
        item.setEatBuff(50000, StatType.DEF, 3);
        item.setEatBuff(50000, StatType.AGI, 10);
        Config.unloadObject(item);

        createItem("가죽", "소에서 나온 가죽이다", 2);
        
        createItem("좀비머리", "보기엔 좀 역겨운 좀비 머리다", 3);
        createItem("좀비영혼파편", "좀비의 영혼을 강제로 해제하여 떨어진 파편이다", 3);
        createItem("좀비심장", "심장이지만 좀비의 것이라 그런지 피는 나지 않는다", 3);

        item = new Item("하급 합금", "다양한 광물을 합쳐 만든 합금이다");
        item.getHandleLv().set(3);
        item.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.COAL.getId(), 30);
            put(ItemList.COPPER.getId(), 20);
            put(ItemList.LEAD.getId(), 15);
            put(ItemList.TIN.getId(), 15);
            put(ItemList.NICKEL.getId(), 15);
        }}, true);
        Config.unloadObject(item);

        item = new Item("중급 합금", "다양한 광물을 합쳐 만든 괜찮은 합금이다");
        item.getHandleLv().set(7);
        item.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.COAL.getId(), 100);
            put(ItemList.LITHIUM.getId(), 50);
            put(ItemList.SILVER.getId(), 20);
            put(ItemList.GOLD.getId(), 5);
        }}, true);
        Config.unloadObject(item);

        item = new Item("상급 합금", "다양한 광물을 합쳐 만든 질 좋은 합금이다");
        item.getHandleLv().set(13);
        item.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.WHITE_GOLD.getId(), 10);
            put(ItemList.TITANIUM.getId(), 30);
            put(ItemList.LIQUID_STONE.getId(), 30);
            put(ItemList.DIAMOND.getId(), 10);
            put(ItemList.ORICHALCON.getId(), 10);
            put(ItemList.LANDIUM.getId(), 2);
            put(ItemList.AITUME.getId(), 2);
            put(ItemList.EMPTY_SPHERE.getId(), 50);
        }}, true);
        Config.unloadObject(item);
        
        createItem("슬라임 조각", "슬라임에게서 떨어져나온 파편이다", 5);
        createItem("거미 다리", "찢어진 거미 다리다. 별로 좋은 형태는 아니다", 6);
        createItem("거미 눈", "거미 눈이다. 반짝거려서 꽤나 예쁘다", 6);

        item = new Item("강철", "기존의 철을 개선한 단단한 철이다");
        item.getHandleLv().set(3);
        item.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.IRON.getId(), 100);
            put(ItemList.LOW_ALLOY.getId(), 3);
            put(ItemList.COAL.getId(), 100);
        }}, true);
        Config.unloadObject(item);
        
        createItem("엘릭서 허브", "엘프의 기운이 들어간 약초다");

        item = new Item("하급 엘릭서", "최대 체력과 마나의 15%를 회복시켜주는 포션이다");
        item.getHandleLv().set(3);
        item.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.ELIXIR_HERB.getId(), 3);
            put(ItemList.GLASS_BOTTLE.getId(), 1);
        }}, true);
        item.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.NONE.getId(), 2);

            put(ItemList.ELIXIR.getId(), 1);
        }}, true);
        item.setUse((self, other) -> {
            self.setBasicStat(StatType.HP, (int) (self.getStat(StatType.MAXHP) * 0.15));
            self.setBasicStat(StatType.MN, (int) (self.getStat(StatType.MAXMN) * 0.15));
            return "최대 체력과 마나의 15%를 회복했습니다\n현재 체력: " + self.getDisplayHp() + "\n현재 마나: " + self.getDisplayMn();
        });
        Config.unloadObject(item);

        item = new Item("중급 엘릭서", "최대 체력과 마나의 50%를 회복시켜주는 포션이다");
        item.getHandleLv().set(8);
        item.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.LOW_ELIXIR.getId(), 3);
            put(ItemList.HP_POTION.getId(), 1);
            put(ItemList.MP_POTION.getId(), 1);
        }}, true);
        item.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.NONE.getId(), 2);

            put(ItemList.HIGH_ELIXIR.getId(), 1);
        }}, true);
        item.setUse((self, other) -> {
            self.setBasicStat(StatType.HP, self.getStat(StatType.MAXHP) / 2);
            self.setBasicStat(StatType.MN, self.getStat(StatType.MAXMN) / 2);
            return "최대 체력과 마나의 50%를 회복했습니다\n현재 체력: " + self.getDisplayHp() + "\n현재 마나: " + self.getDisplayMn();
        });
        Config.unloadObject(item);

        item = new Item("상급 엘릭서", "최대 체력과 마나를 모두 회복시켜주는 포션이다");
        item.getHandleLv().set(12);
        item.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.ELIXIR.getId(), 5);
            put(ItemList.HIGH_HP_POTION.getId(), 1);
            put(ItemList.HIGH_MP_POTION.getId(), 1);
        }}, true);
        item.setUse((self, other) -> {
            self.setBasicStat(StatType.HP, self.getStat(StatType.MAXHP));
            self.setBasicStat(StatType.MN, self.getStat(StatType.MAXMN));
            return "최대 체력과 마나의 모두 회복했습니다\n현재 체력: " + self.getDisplayHp() + "\n현재 마나: " + self.getDisplayMn();
        });
        Config.unloadObject(item);

        item = new Item("(일반) 생선구이", "일반 등급의 생선을 구운 음식이다");
        item.getHandleLv().set(1);
        for(long itemId = ItemList.COMMON_FISH1.getId(); itemId <= ItemList.COMMON_FISH5.getId(); itemId++) {
            long itemId_ = itemId;

            item.addRecipe(new HashMap<Long, Integer>() {{
                put(itemId_, 1);
                put(ItemList.COAL.getId(), 5);
            }}, true);
        }
        item.setCanEat(true);
        item.setEatBuff(-1, StatType.HP, 15);
        Config.unloadObject(item);

        item = new Item("(희귀) 생선구이", "희귀 등급의 생선을 구운 음식이다");
        item.getHandleLv().set(3);
        for(long itemId = ItemList.RARE_FISH1.getId(); itemId <= ItemList.RARE_FISH6.getId(); itemId++) {
            long itemId_ = itemId;

            item.addRecipe(new HashMap<Long, Integer>() {{
                put(itemId_, 1);
                put(ItemList.COAL.getId(), 5);
            }}, true);
        }
        item.setCanEat(true);
        item.setEatBuff(-1, StatType.HP, 30);
        Config.unloadObject(item);

        item = new Item("(특별) 생선구이", "특별 등급의 생선을 구운 음식이다");
        item.getHandleLv().set(5);
        for(long itemId = ItemList.SPECIAL_FISH1.getId(); itemId <= ItemList.SPECIAL_FISH10.getId(); itemId++) {
            long itemId_ = itemId;

            item.addRecipe(new HashMap<Long, Integer>() {{
                put(itemId_, 1);
                put(ItemList.COAL.getId(), 5);
            }}, true);
        }
        item.setCanEat(true);
        item.setEatBuff(-1, StatType.HP, 67);
        Config.unloadObject(item);

        item = new Item("(유일) 생선구이", "유일 등급의 생선을 구운 음식이다");
        item.getHandleLv().set(8);
        for(long itemId = ItemList.UNIQUE_FISH1.getId(); itemId <= ItemList.UNIQUE_FISH7.getId(); itemId++) {
            long itemId_ = itemId;

            item.addRecipe(new HashMap<Long, Integer>() {{
                put(itemId_, 1);
                put(ItemList.COAL.getId(), 5);
            }}, true);
        }
        item.setCanEat(true);
        item.setEatBuff(-1, StatType.HP, 120);
        Config.unloadObject(item);

        item = new Item("(전설) 생선구이", "전설 등급의 생선을 구운 음식이다");
        item.getHandleLv().set(10);
        for(long itemId = ItemList.LEGENDARY_FISH1.getId(); itemId <= ItemList.LEGENDARY_FISH4.getId(); itemId++) {
            long itemId_ = itemId;

            item.addRecipe(new HashMap<Long, Integer>() {{
                put(itemId_, 1);
                put(ItemList.COAL.getId(), 5);
            }}, true);
        }
        item.setCanEat(true);
        item.setEatBuff(-1, StatType.HP, 300);
        Config.unloadObject(item);

        item = new Item("(신화) 생선구이", "신화 등급의 생선을 구운 음식이다");
        item.getHandleLv().set(12);
        for(long itemId = ItemList.MYSTIC_FISH1.getId(); itemId <= ItemList.MYSTIC_FISH2.getId(); itemId++) {
            long itemId_ = itemId;

            item.addRecipe(new HashMap<Long, Integer>() {{
                put(itemId_, 1);
                put(ItemList.COAL.getId(), 5);
            }}, true);
        }
        item.setCanEat(true);
        item.setEatBuff(-1, StatType.HP, 750);
        Config.unloadObject(item);

        item = new Item("익힌 양고기", "양고기를 노릇하게 익힌 음식이다");
        item.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.LAMB.getId(), 1);
            put(ItemList.COAL.getId(), 5);
        }}, true);
        item.setEatBuff(100000, StatType.ATS, 30);
        item.setEatBuff(100000, StatType.ATK, 1);
        item.setCanEat(true);
        Config.unloadObject(item);

        item = new Item("익힌 돼지고기", "돼지고기를 노릇하게 익힌 음식이다");
        item.getHandleLv().set(2);
        item.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.PORK.getId(), 1);
            put(ItemList.COAL.getId(), 5);
        }}, true);
        item.setEatBuff(100000, StatType.ACC, 15);
        item.setEatBuff(100000, StatType.ATS, 15);
        item.setCanEat(true);
        Config.unloadObject(item);

        item = new Item("익힌 소고기", "소고기를 살짝 익힌 음식이다");
        item.getHandleLv().set(2);
        item.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.BEEF.getId(), 1);
            put(ItemList.COAL.getId(), 5);
        }}, true);
        item.setEatBuff(50000, StatType.DEF, 4);
        item.setEatBuff(50000, StatType.AGI, 15);
        item.setCanEat(true);
        Config.unloadObject(item);

        createItem("신의 물", "천계의 신의 힘이 담긴 물이다");

        item = new Item("하얘진 좀비 영혼", "신의 힘으로 어둠이 사라진 좀비의 영혼이다");
        item.getHandleLv().set(3);
        item.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.ZOMBIE_SOUL.getId(), 3);
            put(ItemList.GOD_WATER.getId(), 1);
        }}, true);
        Config.unloadObject(item);

        item = new Item("굳은 슬라임", "신의 힘으로 어둠이 사라져 굳어버린 슬라임이다");
        item.getHandleLv().set(5);
        item.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.PIECE_OF_SLIME.getId(), 30);
            put(ItemList.GOD_WATER.getId(), 1);
        }}, true);
        Config.unloadObject(item);

        createItem("마정석", "어둠의 힘이 담긴 돌이다");

        Config.ID_COUNT.put(Id.ITEM, Math.max(Config.ID_COUNT.get(Id.ITEM), 177L));
        Logger.i("ObjectMaker", "Item making is done!");
    }

    private static void createEquips() {
        Equipment equipment = new Equipment(EquipType.WEAPON, "목검", "나무로 만든 기본적인 검이다");
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.BRANCH.getId(), 5);
            put(ItemList.GRASS.getId(), 5);
        }}, true);
        equipment.addBasicStat(StatType.ATK, 1);
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.WEAPON, "철검", "철로 만든 날카로운 검이다");
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.STONE.getId(), 20);
            put(ItemList.LAPIS.getId(), 10);
            put(ItemList.IRON.getId(), 20);
        }}, true);
        equipment.addBasicStat(StatType.ATK, 3);
        equipment.addBasicStat(StatType.ATS, 5);
        equipment.addBasicStat(StatType.ACC, 10);
        equipment.getHandleLv().set(2);
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.WEAPON, "합검",
                "공격 시 33% 확률로 데미지 1.1배, 33% 확률로 +2 데미지를 준다\n" +
                "사용 시 5 마나를 소모하여 잃은 체력의 10%를 회복한다");
        equipment.getHandleLv().set(2);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.LOW_ALLOY.getId(), 3);
            put(ItemList.QUARTZ.getId(), 4);
        }}, true);
        equipment.addBasicStat(StatType.ATK, 5);
        equipment.addBasicStat(StatType.ATS, 5);
        equipment.getEvents().add(new DamageEvent(-1) {
            @Override
            public void onDamage(@NonNull Entity self, @NonNull Entity victim, Int totalDmg, Int totalDra, Bool isCrit) {
                int randomValue = new Random().nextInt(3);

                switch (randomValue) {
                    case 0:
                        break;

                    case 1:
                        totalDmg.multiple(1.1);
                        break;

                    case 2:
                        totalDmg.add(2);
                        break;
                }
            }
        });
        equipment.setEquipUse(new EquipUse(0, 10) {
            @Override
            @NonNull
            public String use(@NonNull Entity self, @NonNull List<GameObject> other) {
                int lostHp = self.getStat(StatType.MAXHP) - self.getStat(StatType.HP);
                int heal = (int) (lostHp * 0.1);

                self.addBasicStat(StatType.HP, heal);
                return "마나 10을 사용하여 체력을 " + lostHp + "만큼 회복했습니다";
            }
        });
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.WEAPON, "하트 브레이커1", "공격 시 체력이 10% 미만인 적을 처형시킨다");
        equipment.getHandleLv().set(3);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.IRON.getId(), 30);
            put(ItemList.ZOMBIE_HEART.getId(), 10);
        }}, true);
        equipment.addBasicStat(StatType.ATK, 15);
        equipment.addBasicStat(StatType.ATS, 50);
        equipment.getHandleLv().set(4);
        equipment.getEvents().add(new DamageEvent(-1) {
            @Override
            public void onDamage(@NonNull Entity self, @NonNull Entity victim, Int totalDmg, Int totalDra, Bool isCrit) {
                int hp = victim.getStat(StatType.HP);
                double remainPercent = (double) hp / victim.getStat(StatType.MAXHP);
                if(remainPercent < 0.1) {
                    totalDmg.set(Math.max(hp, totalDmg.get()));
                }
            }
        });
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.WEAPON, "헤드 헌터1", "치명타 미 발동시 20% 확률로 1.8배의 치명타를 발동시킨다");
        equipment.getHandleLv().set(3);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.RED_STONE.getId(), 30);
            put(ItemList.PIG_HEAD.getId(), 5);
            put(ItemList.ZOMBIE_HEAD.getId(), 5);
        }}, true);
        equipment.addBasicStat(StatType.ATK, 20);
        equipment.addBasicStat(StatType.ATS, 10);
        equipment.addBasicStat(StatType.ACC, 10);
        equipment.addBasicStat(StatType.AGI, 10);
        equipment.getEvents().add(new DamageEvent(-1) {
            @Override
            public void onDamage(@NonNull Entity self, @NonNull Entity victim, Int totalDmg, Int totalDra, Bool isCrit) {
                if(!isCrit.get()) {
                    if(Math.random() < 0.2) {
                        isCrit.set(true);
                        totalDmg.multiple(1.8);
                    }
                }
            }
        });
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.WEAPON, "원혼의 검1",
                "공격시 적 최대 체력의 6%에 해당하는 추가 데미지를 입힌다\n" +
                        "사용 시 최대 체력의 6%를 소모하여 다음 공격의 데미지를 66% 증가시킨다");
        equipment.getHandleLv().set(3);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.ZOMBIE_SOUL.getId(), 3);
            put(ItemList.SILVER.getId(), 20);
        }}, true);
        equipment.addBasicStat(StatType.ATS, 150);
        equipment.getEvents().add(new DamageEvent(-1) {
            @Override
            public void onDamage(@NonNull Entity self, @NonNull Entity victim, Int totalDmg, Int totalDra, Bool isCrit) {
                int maxHp = victim.getStat(StatType.MAXHP);
                totalDmg.add(maxHp * 0.06);

                if(self.getId().getId().equals(Id.PLAYER)) {
                    Player player = (Player) self;

                    if(player.getObjectVariable(Variable.GHOST_SWORD_1, false)) {
                        totalDmg.multiple(1.66);
                        player.removeVariable(Variable.GHOST_SWORD_1);
                    }
                }
            }
        });
        equipment.setEquipUse(new EquipUse(0, 0.06, 0, 0, 0, 0) {
            @Override
            @NonNull
            public String use(@NonNull Entity self, @NonNull List<GameObject> other) {
                self.setVariable(Variable.GHOST_SWORD_1, true);
                return "최대 체력의 6%를 소모하여 다음 공격을 강화했습니다";
            }
        });
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.AMULET, "건강의 부적", "착용하고 있으면 건강해지는 기분이 드는 부적이다");
        equipment.getHandleLv().set(1);
        equipment.addBasicStat(StatType.MAXHP, 30);
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.AMULET, "피의 부적", "누군가의 피가 들어간 섬뜩한 부적이다");
        equipment.getHandleLv().set(3);
        equipment.addBasicStat(StatType.MAXHP, 100);
        equipment.addBasicStat(StatType.ATK, 10);
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.AMULET, "용의 부적", "용이 지니고 다녔다는 부적이다");
        equipment.getHandleLv().set(7);
        equipment.addBasicStat(StatType.MAXHP, 200);
        equipment.addBasicStat(StatType.DEF, 30);
        equipment.addBasicStat(StatType.EVA, 20);
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.HELMET, "가죽 투구", "가죽으로 만든 간단한 보호구다\n" +
                "치명타 데미지를 5% 줄여준다");
        equipment.getHandleLv().set(2);
        equipment.addBasicStat(StatType.MAXHP, 10);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.SHEEP_LEATHER.getId(), 10);
            put(ItemList.LEATHER.getId(), 5);
        }}, true);
        equipment.getEvents().add(new DamagedEvent(-1) {
            @Override
            public void onDamaged(@NonNull Entity self, @NonNull Entity attacker, Int totalDmg, Int totalDra, Bool isCrit) {
                if(isCrit.get()) {
                    totalDmg.multiple(0.95);
                }
            }
        });
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.CHESTPLATE, "가죽 갑옷", "가죽으로 만든 간단한 보호구다");
        equipment.getHandleLv().set(2);
        equipment.addBasicStat(StatType.MAXHP, 10);
        equipment.addBasicStat(StatType.DEF, 2);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.SHEEP_LEATHER.getId(), 10);
            put(ItemList.LEATHER.getId(), 5);
        }}, true);
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.LEGGINGS, "가죽 바지", "가죽으로 만든 간단한 보호구다");
        equipment.getHandleLv().set(2);
        equipment.addBasicStat(StatType.MAXHP, 10);
        equipment.addBasicStat(StatType.MDEF, 2);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.SHEEP_LEATHER.getId(), 10);
            put(ItemList.LEATHER.getId(), 5);
        }}, true);
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.CHESTPLATE, "가죽 신발", "가죽으로 만든 간단한 보호구다");
        equipment.getHandleLv().set(2);
        equipment.addBasicStat(StatType.MAXHP, 8);
        equipment.addBasicStat(StatType.AGI, 8);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.SHEEP_LEATHER.getId(), 10);
            put(ItemList.LEATHER.getId(), 5);
        }}, true);
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.HELMET, "하급 합금 투구", "하급 합금으로 만든 간단한 보호구다\n" +
                "치명타 데미지를 20% 확률로 50% 방어한다");
        equipment.getHandleLv().set(3);
        equipment.addBasicStat(StatType.MAXHP, 25);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.LOW_ALLOY.getId(), 5);
            put(ItemList.GOLD.getId(), 5);
        }}, true);
        equipment.getEvents().add(new DamagedEvent(-1) {
            @Override
            public void onDamaged(@NonNull Entity self, @NonNull Entity attacker, Int totalDmg, Int totalDra, Bool isCrit) {
                if(isCrit.get() && Math.random() < 0.2) {
                    totalDmg.divide(2);
                }
            }
        });
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.CHESTPLATE, "하급 합금 갑옷", "하급 합금으로 만든 간단한 보호구다\n" +
                "사용 시 4 마나를 사용하여 75% 확률로 방어를 한다");
        equipment.getHandleLv().set(3);
        equipment.addBasicStat(StatType.MAXHP, 25);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.LOW_ALLOY.getId(), 5);
            put(ItemList.GOLD.getId(), 5);
        }}, true);
        equipment.setEquipUse(new EquipUse(0, 4) {
            @NonNull
            @Override
            public String use(@NonNull Entity self, @NonNull List<GameObject> other) {
                if(Math.random() < 0.75) {
                    self.setVariable(Variable.IS_DEFENCING, true);
                    return "방어 태세로 전환했습니다";
                } else {
                    return "방어 태세로의 전환에 실패했습니다";
                }
            }
        });
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.LEGGINGS, "하급 합금 바지", "하급 합금으로 만든 간단한 보호구다\n" +
                "사용 시 5 마나를 사용하여 20초간 회피 15의 버프를 얻는다");
        equipment.getHandleLv().set(3);
        equipment.addBasicStat(StatType.MAXHP, 25);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.LOW_ALLOY.getId(), 5);
            put(ItemList.GOLD.getId(), 5);
        }}, true);
        equipment.setEquipUse(new EquipUse(0, 5) {
            @NonNull
            @Override
            public String use(@NonNull Entity self, @NonNull List<GameObject> other) {
                self.addBuff(System.currentTimeMillis() + 20000L, StatType.EVA, 15);
                return "20초간 회피 15의 버프를 획득하였습니다";
            }
        });
        Config.unloadObject(equipment);
        
        equipment = new Equipment(EquipType.SHOES, "하급 합금 신발", "하급 합금으로 만든 간단한 보호구다\n" +
                "타격 시 합검을 장착하고 있다면 5의 추가 데미지를 준다");
        equipment.getHandleLv().set(3);
        equipment.addBasicStat(StatType.MAXHP, 25);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.LOW_ALLOY.getId(), 5);
            put(ItemList.GOLD.getId(), 5);
        }}, true);
        equipment.getEvents().add(new DamageEvent(-1) {
            @Override
            public void onDamage(@NonNull Entity self, @NonNull Entity victim, Int totalDmg, Int totalDra, Bool isCrit) {
                long equippedId = self.getEquipped(EquipType.WEAPON);
                Equipment equipped = Config.getData(Id.EQUIPMENT, equippedId);

                if(equipped.getOriginalId() == EquipList.MIX_SWORD.getId()) {
                    totalDmg.add(5);
                }
            }
        });
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.WEAPON, "하급 마나소드", "마나의 기운을 담고 있는 검이다\n" +
                "공격 시 마나 1을 회복한다");
        equipment.getHandleLv().set(3);
        equipment.addBasicStat(StatType.ATS, 15);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.PIECE_OF_MANA.getId(), 5);
            put(ItemList.LAPIS.getId(), 20);
            put(ItemList.RED_STONE.getId(), 20);
        }}, true);
        equipment.getEvents().add(new DamageEvent(-1) {
            //TODO : 마나 회복 안됨(어째써?)

            @Override
            public void onDamage(@NonNull Entity self, @NonNull Entity victim, Int totalDmg, Int totalDra, Bool isCrit) {
                self.addBasicStat(StatType.MN, 1);
            }
        });
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.WEAPON, "석영 검", "다양한 석영으로 만들어진 빛나는 검이다");
        equipment.getHandleLv().set(5);
        equipment.addBasicStat(StatType.ATK, 5);
        equipment.addBasicStat(StatType.MAXMN, 5);
        equipment.addBasicStat(StatType.ATS, 10);
        equipment.addBasicStat(StatType.ACC, 10);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.QUARTZ.getId(), 50);
            put(ItemList.DARK_QUARTZ.getId(), 10);
            put(ItemList.FIRE_QUARTZ.getId(), 10);
        }}, true);
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.HELMET, "슬라임 투구", "끈적끈적한 투구다\n" +
                "피격 시 데미지의 8% 를 회복한다\n해당 공격이 치명타일 경우 15% 를 회복한다");
        equipment.getHandleLv().set(5);
        equipment.addBasicStat(StatType.ATS, -10);
        equipment.addBasicStat(StatType.MAXHP, 20);
        equipment.addBasicStat(StatType.ATK, 3);
        equipment.addBasicStat(StatType.DEF, 5);
        equipment.addBasicStat(StatType.BRE, 5);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.PIECE_OF_SLIME.getId(), 30);
        }}, true);
        equipment.getEvents().add(new DamagedEvent(-1) {
            @Override
            public void onDamaged(@NonNull Entity self, @NonNull Entity attacker, Int totalDmg, Int totalDra, Bool isCrit) {
                double coe;
                if(isCrit.get()) {
                    coe = 0.15;
                } else {
                    coe = 0.08;
                }

                int heal = (int) (totalDmg.get() * coe);
                self.addBasicStat(StatType.HP, heal);
            }
        });
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.CHESTPLATE, "슬라임 갑옷", "끈적끈적한 갑옷이다\n" +
                "피격 시 데미지의 20% 를 저장한다\n해당 공격이 치명타일 경우 40% 를 저장한다\n" +
                "사용 시 마나 5를 소모하여 다음 공격이 저장한 데미지를 사용하여 추가 데미지를 입힌다");
        equipment.getHandleLv().set(5);
        equipment.addBasicStat(StatType.ATS, -10);
        equipment.addBasicStat(StatType.MAXHP, 20);
        equipment.addBasicStat(StatType.ATK, 3);
        equipment.addBasicStat(StatType.DEF, 5);
        equipment.addBasicStat(StatType.BRE, 5);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.PIECE_OF_SLIME.getId(), 30);
        }}, true);
        equipment.setEquipUse(new EquipUse(0, 5) {
            @NonNull
            @Override
            public String use(@NonNull Entity self, @NonNull List<GameObject> other) {
                self.setVariable(Variable.SLIME_CHESTPLATE_USE, true);

                int saved = self.getVariable(Variable.SLIME_CHESTPLATE);
                return "다음 공격이 " + saved + " 만큼의 추가 데미지를 입힙니다";
            }
        });
        equipment.getEvents().add(new DamagedEvent(-1) {
            @Override
            public void onDamaged(@NonNull Entity self, @NonNull Entity attacker, Int totalDmg, Int totalDra, Bool isCrit) {
                double coe;
                if(isCrit.get()) {
                    coe = 0.4;
                } else {
                    coe = 0.2;
                }

                int saved = (int) (totalDmg.get() * coe);
                self.setVariable(Variable.SLIME_CHESTPLATE, saved);
            }
        });
        equipment.getEvents().add(new DamageEvent(-1) {
            @Override
            public void onDamage(@NonNull Entity self, @NonNull Entity victim, Int totalDmg, Int totalDra, Bool isCrit) {
                boolean used = self.getObjectVariable(Variable.SLIME_CHESTPLATE_USE, false);

                if(used) {
                    int saved = self.getVariable(Variable.SLIME_CHESTPLATE);
                    totalDmg.add(saved);

                    self.removeVariable(Variable.SLIME_CHESTPLATE);
                    self.removeVariable(Variable.SLIME_CHESTPLATE_USE);
                }
            }
        });
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.LEGGINGS, "슬라임 바지", "끈적끈적한 바지다\n" +
                "사용 시 마나를 10 사용하여 30초간 모든 적의 공격 속도를 25 감소시킨다");
        equipment.getHandleLv().set(5);
        equipment.addBasicStat(StatType.ATS, -10);
        equipment.addBasicStat(StatType.MAXHP, 20);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.PIECE_OF_SLIME.getId(), 30);
        }}, true);
        equipment.setEquipUse(new EquipUse(0, 10) {
            @NonNull
            @Override
            public String use(@NonNull Entity self, @NonNull List<GameObject> other) {
                Id id;
                Entity entity;
                Set<Player> playerSet = new HashSet<>();
                for(Map.Entry<Id, ConcurrentHashSet<Long>> entry : self.getEnemies().entrySet()) {
                    id = entry.getKey();
                    
                    for(long objectId : entry.getValue()) {
                        entity = Config.getData(id, objectId);
                        
                        if(id.equals(Id.PLAYER)) {
                            playerSet.add((Player) entity);
                        }
                        
                        entity.addBuff(System.currentTimeMillis() + 30000L, StatType.ATS, -25);
                    }
                }

                Player.replyPlayers(playerSet, self.getName() + " 이 슬라임 바지 아이템을 사용하여 모든 적의 공격속도를 25 감소시켰습니다");
                return "모든 적의 공격속도를 25 감소시켰습니다";
            }
        });
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.SHOES, "슬라임 신발", "끈적끈적한 신발이다");
        equipment.getHandleLv().set(5);
        equipment.addBasicStat(StatType.ATS, -10);
        equipment.addBasicStat(StatType.MAXHP, 30);
        equipment.addBasicStat(StatType.BRE, 5);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.PIECE_OF_SLIME.getId(), 30);
        }}, true);
        Config.unloadObject(equipment);
        
        equipment = new Equipment(EquipType.HELMET, "양털 모자", "양털로 만든 따뜻한 모자다");
        equipment.getHandleLv().set(2);
        equipment.addBasicStat(StatType.MAXHP, 15);
        equipment.addBasicStat(StatType.MDEF, 5);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.WOOL.getId(), 10);
        }}, true);
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.CHESTPLATE, "강철 갑옷", "강철로 만든 단단한 갑옷이다");
        equipment.getHandleLv().set(3);
        equipment.addBasicStat(StatType.MAXHP, 50);
        equipment.addBasicStat(StatType.ATS, -20);
        equipment.addBasicStat(StatType.DEF, 10);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.HARD_IRON.getId(), 5);
            put(ItemList.COAL.getId(), 100);
        }}, true);
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.LEGGINGS, "기괴한 바지", "거미 다리로 만든 기괴하게 생긴 바지다\n" +
                "피격 시 10% 확률로 데미지를 50% 감소시키고 다음 자신의 공격을 치명타로 만든다");
        equipment.getHandleLv().set(6);
        equipment.addBasicStat(StatType.MAXHP, 30);
        equipment.addBasicStat(StatType.ATS, 50);
        equipment.addBasicStat(StatType.DEF, 5);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.PIECE_OF_SLIME.getId(), 10);
            put(ItemList.SPIDER_LEG.getId(), 4);
        }}, true);
        equipment.getEvents().add(new DamagedEvent(-1) {
            @Override
            public void onDamaged(@NonNull Entity self, @NonNull Entity attacker, Int totalDmg, Int totalDra, Bool isCrit) {
                if(Math.random() < 0.1) {
                    totalDmg.set(0);
                    self.setVariable(Variable.WEIRD_LEGGINGS, true);
                    
                    if(attacker.getId().getId().equals(Id.PLAYER)) {
                        ((Player) attacker).replyPlayer("상대방의 기괴한 바지의 효과로 공격이 상쇄되었습니다");
                    }
                    
                    if(self.getId().getId().equals(Id.PLAYER)) {
                        ((Player) self).replyPlayer("기괴한 바지의 효과로 공격이 상쇄되며, 다음 공격이 치명타가 됩니다");
                    }
                }
            }
        });
        equipment.getEvents().add(new DamageEvent(-1) {
            @Override
            public void onDamage(@NonNull Entity self, @NonNull Entity victim, Int totalDmg, Int totalDra, Bool isCrit) {
                boolean weirdLeggings = self.getObjectVariable(Variable.WEIRD_LEGGINGS, false);

                if(weirdLeggings) {
                    isCrit.set(true);
                    totalDmg.multiple(2);

                    self.removeVariable(Variable.WEIRD_LEGGINGS);
                }
            }
        });
        Config.unloadObject(equipment);

        equipment = new Equipment(EquipType.SHOES, "광부의 신발", "광부들을 위해 맞춤 제작된 가볍고 튼튼한 신발이다\n" +
                "광질 시 돌을 채광하면 25% 확률로 돌을 추가로 1개 더 채광한다");
        equipment.getHandleLv().set(1);
        equipment.getHandleLv().set(1);
        equipment.addRecipe(new HashMap<Long, Integer>() {{
            put(ItemList.STONE.getId(), 1000);
            put(ItemList.COAL.getId(), 1000);
        }}, true);
        equipment.getEvents().add(new MineEvent(-1) {
            @Override
            public void onMine(@NonNull Entity self, @NonNull LoNg itemId, @NonNull Int mineCount) {
                if(itemId.get().equals(ItemList.STONE.getId()) && Math.random() < 0.25) {
                    mineCount.add(1);
                }
            }
        });
        Config.unloadObject(equipment);
        
        equipment = new Equipment(EquipType.WEAPON, "트롤의 몽둥이", "트롤이 가지고 다니는 무식하게 생긴 몽둥이다\n" +
                "사용 시 마나 10을 사용하여 다음 공격이 자신보다 낮은 레벨의 몬스터를 공격하는 것이라면 3배의 데미지를 준다");
        equipment.getHandleLv().set(5);
        equipment.addBasicStat(StatType.ATK, 20);
        equipment.addBasicStat(StatType.ATS, -20);
        equipment.setEquipUse(new EquipUse(0, 10) {
            @NonNull
            @Override
            public String use(@NonNull Entity self, @NonNull List<GameObject> other) {
                self.setVariable(Variable.TROLL_CLUB, true);
                return "다음 공격을 강화했습니다";
            }
        });
        equipment.getEvents().add(new DamageEvent(-1) {
            @Override
            public void onDamage(@NonNull Entity self, @NonNull Entity victim, Int totalDmg, Int totalDra, Bool isCrit) {
                boolean trollClub = self.getObjectVariable(Variable.TROLL_CLUB, false);

                if(trollClub) {
                    if(victim.getId().getId().equals(Id.MONSTER)) {
                        totalDmg.multiple(3);
                    }

                    self.removeVariable(Variable.TROLL_CLUB);
                }
            }
        });
        Config.unloadObject(equipment);

        Config.ID_COUNT.put(Id.EQUIPMENT, Math.max(Config.ID_COUNT.get(Id.EQUIPMENT), 29L));
        Logger.i("ObjectMaker", "Equipment making is done!");
    }

    private static void createMonsters() {
        Monster monster = new Monster("양");
        monster.getLv().set(2);
        monster.setLocation(null);
        monster.setType(MonsterType.MIDDLE);

        monster.setBasicStat(StatType.MAXHP, 20);
        monster.setBasicStat(StatType.HP, 20);
        monster.setBasicStat(StatType.ATK, 3);
        monster.setBasicStat(StatType.DEF, 5);

        monster.setItemDrop(ItemList.LAMB.getId(), 0.5D, 1, 1);
        monster.setItemDrop(ItemList.SHEEP_LEATHER.getId(), 0.2D, 1, 1);
        monster.setItemDrop(ItemList.WOOL.getId(), 0.5D, 1, 3);

        Config.unloadObject(monster);


        monster = new Monster("돼지");
        monster.getLv().set(8);
        monster.setLocation(null);
        monster.setType(MonsterType.MIDDLE);

        monster.setBasicStat(StatType.MAXHP, 60);
        monster.setBasicStat(StatType.HP, 60);
        monster.setBasicStat(StatType.ATK, 10);
        monster.setBasicStat(StatType.DEF, 5);
        monster.setBasicStat(StatType.ATS, 50);

        monster.setItemDrop(ItemList.PORK.getId(), 0.5D, 1, 1);
        monster.setItemDrop(ItemList.PIG_HEAD.getId(), 0.1D, 1, 1);

        Config.unloadObject(monster);


        monster = new Monster("소");
        monster.getLv().set(20);
        monster.setLocation(null);
        monster.setType(MonsterType.MIDDLE);

        monster.setBasicStat(StatType.MAXHP, 100);
        monster.setBasicStat(StatType.HP, 100);
        monster.setBasicStat(StatType.ATK, 10);
        monster.setBasicStat(StatType.DEF, 15);
        monster.setBasicStat(StatType.ATS, 70);
        monster.setBasicStat(StatType.EVA, 20);

        monster.setItemDrop(ItemList.BEEF.getId(), 0.5D, 1, 1);
        monster.setItemDrop(ItemList.LEATHER.getId(), 0.5D, 1, 1);

        Config.unloadObject(monster);


        monster = new Monster("좀비");
        monster.getLv().set(30);
        monster.setLocation(null);

        monster.setBasicStat(StatType.MAXHP, 300);
        monster.setBasicStat(StatType.HP, 300);
        monster.setBasicStat(StatType.ATK, 20);
        monster.setBasicStat(StatType.ATS, 150);
        monster.setBasicStat(StatType.EVA, 40);

        monster.setItemDrop(ItemList.ZOMBIE_HEAD.getId(), 0.1D, 1, 1);
        monster.setItemDrop(ItemList.ZOMBIE_SOUL.getId(), 0.01D, 1, 1);
        monster.setItemDrop(ItemList.ZOMBIE_HEART.getId(), 0.1D, 1, 1);

        Config.unloadObject(monster);


        monster = new Monster("슬라임");
        monster.getLv().set(45);
        monster.setLocation(null);

        monster.setBasicStat(StatType.MAXHP, 400);
        monster.setBasicStat(StatType.HP, 400);
        monster.setBasicStat(StatType.ATK, 30);
        monster.setBasicStat(StatType.ATS, 50);
        monster.setBasicStat(StatType.ACC, 20);
        monster.setBasicStat(StatType.DRA, 10);
        monster.setBasicStat(StatType.BRE, 15);

        monster.setItemDrop(ItemList.PIECE_OF_SLIME.getId(), 1D, 1, 3);

        Config.unloadObject(monster);


        monster = new Monster("거미");
        monster.getLv().set(50);
        monster.setLocation(null);

        monster.setBasicStat(StatType.MAXHP, 75);
        monster.setBasicStat(StatType.HP, 75);
        monster.setBasicStat(StatType.ATK, 30);
        monster.setBasicStat(StatType.DEF, 10);
        monster.setBasicStat(StatType.ATS, 200);
        monster.setBasicStat(StatType.ACC, 20);
        monster.setBasicStat(StatType.EVA, 40);
        monster.setBasicStat(StatType.AGI, 200);

        monster.setItemDrop(ItemList.SPIDER_LEG.getId(), 0.3D, 1, 1);
        monster.setItemDrop(ItemList.SPIDER_EYE.getId(), 0.2D, 1, 1);

        Config.unloadObject(monster);


        monster = new Monster("트롤");
        monster.getLv().set(70);
        monster.setLocation(null);

        monster.setBasicStat(StatType.MAXHP, 500);
        monster.setBasicStat(StatType.HP, 500);
        monster.setBasicStat(StatType.ATK, 50);
        monster.setBasicStat(StatType.DRA, 50);

        monster.addBasicEquip(EquipList.TROLL_CLUB.getId());
        monster.setEquipDropPercent(EquipType.WEAPON, 0.03);

        monster.setItemDrop(ItemList.MAGIC_STONE.getId(), 0.1, 1, 1);

        Config.unloadObject(monster);


        monster = new Monster("엔트");
        monster.getLv().set(60);
        monster.setLocation(null);

        monster.setBasicStat(StatType.MAXHP, 100);
        monster.setBasicStat(StatType.HP, 100);
        monster.setBasicStat(StatType.DEF, 999);

        monster.setItemDrop(ItemList.BRANCH.getId(), 1, 3, 5);
        monster.setItemDrop(ItemList.LEAF.getId(), 1, 3, 5);
        monster.setItemDrop(ItemList.MAGIC_STONE.getId(), 1, 1, 1);

        monster.setVariable(Variable.ENT_WAIT_TURN, 3);
        monster.addEvent(new DamagedEvent(3) {
            @Override
            public void onDamaged(@NonNull Entity self, @NonNull Entity attacker, Int totalDmg, Int totalDra, Bool isCrit) {
                int waitTurn = self.getVariable(Variable.ENT_WAIT_TURN);

                if(waitTurn == 0) {
                    self.removeVariable(Variable.ENT_WAIT_TURN);

                    if(attacker.getId().getId().equals(Id.PLAYER)) {
                        ((Player) attacker).replyPlayer("마수의 기운이 깨어났습니다!");
                    }

                    self.addBasicStat(StatType.ATK, 50);
                    self.addBasicStat(StatType.BRE, 10);
                } else {
                    self.setVariable(Variable.ENT_WAIT_TURN, waitTurn - 1);

                    if(attacker.getId().getId().equals(Id.PLAYER)) {
                        ((Player) attacker).replyPlayer("마수의 기운이 깨어나고 있습니다");
                    }
                }
            }
        });

        Config.unloadObject(monster);


        Config.ID_COUNT.put(Id.MONSTER, Math.max(Config.ID_COUNT.get(Id.MONSTER), 9L));
        Logger.i("ObjectMaker", "Monster making is done!");
    }

    private static void createMaps() {
        GameMap map = new GameMap(MapList.findByLocation(0, 0));
        map.getLocation().set(0, 0, 1, 1);
        Config.unloadMap(map);

        map = new GameMap(MapList.findByLocation(0, 1));
        map.setMapType(MapType.SEA);
        map.getLocation().set(0, 1, 1, 1);
        Config.unloadMap(map);

        map = new GameMap(MapList.findByLocation(0, 2));
        map.setMapType(MapType.CAVE);
        map.getLocation().set(0, 2, 1, 1);
        map.setSpawnMonster(MonsterList.SPIDER.getId(), 1D, 4);
        Config.unloadMap(map);

        map = new GameMap(MapList.findByLocation(1, 0));
        map.setMapType(MapType.FIELD);
        map.getLocation().set(1, 0, 1, 1);
        map.setSpawnMonster(MonsterList.SHEEP.getId(), 1D, 8);
        Config.unloadMap(map);

        map = new GameMap(MapList.findByLocation(1, 1));
        map.setMapType(MapType.RIVER);
        map.getLocation().set(1, 1, 1, 1);
        Config.unloadMap(map);

        map = new GameMap(MapList.findByLocation(1, 2));
        map.setMapType(MapType.SWAMP);
        map.getRequireLv().set(40);
        map.getLocation().set(1, 2, 1, 1);
        map.setSpawnMonster(MonsterList.SLIME.getId(), 1, 8);
        Config.unloadMap(map);

        map = new GameMap(MapList.findByLocation(2, 0));
        map.setMapType(MapType.FIELD);
        map.getRequireLv().set(5);
        map.getLocation().set(2, 0, 1, 1);
        map.setSpawnMonster(MonsterList.PIG.getId(), 1D, 8);
        Config.unloadMap(map);

        map = new GameMap(MapList.findByLocation(2, 1));
        map.setMapType(MapType.FIELD);
        map.getRequireLv().set(15);
        map.getLocation().set(2, 1, 1, 1);
        map.setSpawnMonster(MonsterList.COW.getId(), 1D, 8);
        Config.unloadMap(map);

        map = new GameMap(MapList.findByLocation(2, 2));
        map.setMapType(MapType.CEMETERY);
        map.getRequireLv().set(20);
        map.getLocation().set(2, 2, 1, 1);
        map.setSpawnMonster(MonsterList.ZOMBIE.getId(), 1D, 3);
        Config.unloadMap(map);

        map = new GameMap(MapList.findByLocation(3, 0));
        map.setMapType(MapType.FIELD);
        map.getRequireLv().set(50);
        map.getLocation().set(3, 0, 1, 1);
        map.setSpawnMonster(MonsterList.ENT.getId(), 1D, 16);
        Config.unloadMap(map);

        map = new GameMap(MapList.findByLocation(3, 1));
        map.setMapType(MapType.FOREST);
        map.getRequireLv().set(60);
        map.getLocation().set(3, 1, 1, 1);
        map.setSpawnMonster(MonsterList.TROLL.getId(), 1D, 3);
        Config.unloadMap(map);

        for(int x = 0; x <= 10; x++) {
            for(int y = 0; y <= 10; y++) {
                if(!MapList.findByLocation(x, y).equals(Config.INCOMPLETE)) {
                    continue;
                }

                map = new GameMap(Config.INCOMPLETE);
                map.getLocation().setMap(x, y);
                map.setMapType(MapType.FIELD);
                map.getLocation().set(x, y, 1, 1);
                Config.unloadMap(map);
            }
        }

        Player player;
        for(String[] playerData : Config.PLAYER_LIST.values()) {
            player = Config.loadPlayer(playerData[0], playerData[1]);
            Config.unloadObject(player);
            map = Config.loadMap(player.getLocation());
            map.addEntity(player);
            Config.unloadMap(map);
        }

        Logger.i("ObjectMaker", "Map making is done!");
    }

    private static Chat createChat(@Nullable String name, long chatId, @NonNull String...texts) {
        Chat chat = new Chat(name);
        chat.getId().setObjectId(chatId);
        chat.getText().addAll(Arrays.asList(texts));
        return chat;
    }

    private static void createChats() {
        Chat chat = createChat(null, 1L,
                "드디어 일어났네 __nickname",
                "이 소리가 어디서 들려오는지는 아직은 몰라도 돼. 결국엔 알게 될테니까",
                "어찌됬든 넌 여기서 성장해야만 해. 그리고 니가 나한테 했던 약속을 지켜야겠지",
                "음 뭐가됬든 기본적인거부터 가르쳐줄게. " + Emoji.focus("n 도움말") +
                        " 을 입력해서 명령어를 살펴봐"
        );
        chat.setAnyResponseChat("__도움말", 2L, true);
        chat.setAnyResponseChat("__명령어", 2L, true);
        chat.setAnyResponseChat("__?", 2L, true);
        chat.setAnyResponseChat("__h", 2L, true);
        chat.setAnyResponseChat("__help", 2L, true);
        Config.unloadObject(chat);

        chat = createChat(null, 2L,
                "좋아, 잘 따라오고 있네. 네 정보도 살펴봐야겠지?",
                "명령어 목록을 보고 네 정보를 표시하는 명령어를 사용해봐",
                "아 물론 거기 적혀있기도 하지만 네가 명령어 창을 연 것 처럼, " +
                        "모든 명령어에는 " + Emoji.focus("n") + "이나 " +
                        Emoji.focus("ㅜ") + "라는 글자가 붙으니까 기억해");
        chat.setAnyResponseChat("__정보", 3L, true);
        chat.setAnyResponseChat("__info", 3L, true);
        chat.setAnyResponseChat("__i", 3L, true);
        chat.setAnyResponseChat("__정보 __nickname", 3L, true);
        chat.setAnyResponseChat("__info __nickname", 3L, true);
        chat.setAnyResponseChat("__i __nickname", 3L, true);
        Config.unloadObject(chat);

        chat = createChat(null, 3L,
                "기본 정보랑 상세 정보로 나뉘어서 표시되는게 보이지?",
                "일단 너는 0-0-1-1, 그러니까 서남쪽 끝에 있는거고, 거점도 여기로 잡혀있어",
                "거점? 아 거점은 죽으면 태어나는 장소야. 어짜피 넌 내 권능때문에 죽을 수 없거든...",
                "마지막으로 간단한거 하나만 소개하고 가봐야겠네. 마을에서는 광질을 할 수 있으니까" +
                        " 광질 명령어를 입력해봐"
        );
        chat.setAnyResponseChat("__광질", 4L, true);
        chat.setAnyResponseChat("__mine", 4L, true);
        Config.unloadObject(chat);

        chat = createChat(null, 4L,
                "그럼 난 가볼게. 어짜피 할 일도 많고 너도 이젠 혼자서 다 할 수 있을거 같으니까",
                "맵 정보를 확인하면 다른 주민들 이름도 보이니까 대화도 해보고 말이야",
                "뭐라도 주고 가라고? 골드를 조금 넣어놨으니까 그거라도 써",
                "아 그리고... ... ... (더 이상 들리지 않는다)"
        );
        chat.getDelayTime().set(1500L);
        chat.getMoney().set(1000L);
        Config.unloadObject(chat);

        chat = createChat(null, 5L,
                "음? 못보던 얼굴이군",
                "그래 이름이 __nickname 이라고?",
                "난 이 '시작의 마을' 의 이장이라네. 잘 지네봄세",
                "우리 마을은 다양한 인종이나 종족이 있지",
                "한번 마을을 살펴보고 오는것도 좋을게야"
        );
        Config.unloadObject(chat);

        chat = createChat(null, 6L, "그래 무슨 일인가 __nickname?");
        chat.setBaseMsg(true);
        Config.unloadObject(chat);

        chat = createChat("아무일도 아닙니다", 7L, "그래그래, 무슨 일 있으면 언제나 말 걸게나");
        Config.unloadObject(chat);

        chat = createChat("광질을 하면서 할만한 퀘스트가 있을까요?", 8L,
                "음, 아무래도 돌은 항상 부족해서 말이지",
                "돌 30개만 구해다 줄 수 있겠나?"
        );
        chat.setResponseChat(WaitResponse.YES, 10L, true);
        chat.setResponseChat(WaitResponse.NO, 11L, true);
        Config.unloadObject(chat);

        chat = createChat("낚시를 하면서 할만한 퀘스트가 있을까요?", 9L,
                "요즘 바다나 강이 너무 더러워저셔 말일세",
                "쓰레기가 낚이는게 있으면 3개만 구해다 줄 수 있겠나?"
        );
        chat.setResponseChat(WaitResponse.YES, 12L, true);
        chat.setResponseChat(WaitResponse.NO, 11L, true);
        Config.unloadObject(chat);

        chat = createChat(null, 10L, "고맙네! 돌을 다 구하고 다시 말을 걸어주게나");
        chat.getQuestId().set(1L);
        Config.unloadObject(chat);

        chat = createChat(null,11L, "그럼 어쩔 수 없지. 나중에 마음 바뀌면 다시 받아가게나");
        Config.unloadObject(chat);

        chat = createChat(null, 12L, "고맙네! 쓰레기를 다 수거하고 다시 말을 걸어주게나");
        chat.getQuestId().set(2L);
        Config.unloadObject(chat);

        chat = createChat(null, 13L, "빨리 구해와줬군! 여기 보상이네");
        Config.unloadObject(chat);

        chat = createChat(null, 14L, "좋은 아침이네 __nickname!\n그래 무슨 일인가?");
        chat.setBaseMsg(true);
        Config.unloadObject(chat);

        chat = createChat(null, 15L, "점심은 먹었나 __nickname?\n그래 무슨 일로 왔나?");
        chat.setBaseMsg(true);
        Config.unloadObject(chat);

        chat = createChat(null, 16L, "한적한 저녁이군\n무슨 일로 왔나 __nickname?");
        chat.setBaseMsg(true);
        Config.unloadObject(chat);
        
        chat = createChat("얼굴에 걱정이 많아보이시는데, 무슨 일 있나요?", 17L,
                "이런, 얼굴에 보였나? 사실 감기에 걸렸는데 방에 불을 아무리 떼도 추워서 말일세",
                "아무래도 불의 기운을 좀 쥐고 있으면 괜찮아질 것 같은데...",
                "붉은색 구체 하나만 구해줄 수 있겠나?"
        );
        chat.setResponseChat(WaitResponse.YES, 24L, true);
        chat.setResponseChat(WaitResponse.NO, 18L, true);
        Config.unloadObject(chat);
        
        chat = createChat(null, 18L, "그래 뭐... 바쁘다면 어쩔 수 없는 일이지");
        Config.unloadObject(chat);

        chat = createChat(null, 19L,
                "드디어 따뜻한 방에서 쉴 수 있겠구만",
                "여기 보상이네!"
        );
        Config.unloadObject(chat);

        chat = createChat("오히려 얼굴이 더 안좋아지신 것 같은데요..?", 20L,
                "끄윽... 마침 잘왔네",
                "자네가 준 붉은색 구체가 너무 강해서 오히려 역효과가 나는 모양이야..",
                "마지막으로 하급 마나 포션 3개만 만들어와줄 수 있겠나?"
        );
        chat.setResponseChat(WaitResponse.YES, 21L, true);
        chat.setResponseChat(WaitResponse.NO, 22L, true);
        Config.unloadObject(chat);
        
        chat = createChat(null, 21L, "자꾸 번거롭게 해서 미안하네\n최대한 빨리 구해와주게나");
        chat.getQuestId().set(4L);
        Config.unloadObject(chat);

        chat = createChat(null, 22L,
                "40년만 젊었어도 직접 만들었을텐데...",
                "있는 일만 빨리 끝내고 다시 와주게"
        );
        Config.unloadObject(chat);

        chat = createChat(null, 23L,
                "휴 이제야 살 것 같구만\n고맙네 __nickname",
                "이건 보상이니 받아가게나"
        );
        Config.unloadObject(chat);

        chat = createChat(null, 24L, "그래도 __nickname 자네가 구해준다니 마음이 놓이는구만");
        chat.getQuestId().set(3L);
        Config.unloadObject(chat);

        chat = createChat(null, 25L,
                "여행자인가? 하하",
                "반갑네, 난 이 마을 대장장이 형석이라고 한다",
                "마을을 둘러볼거면 내 아내 '엘' 에게도 가봤나 모르겠군"
        );
        Config.unloadObject(chat);

        chat = createChat(null, 26L, "무슨 일이지?");
        chat.setBaseMsg(true);
        Config.unloadObject(chat);
        
        chat = createChat(null, 27L,
                "이렇게 늦은 시간에 찾아오다니",
                "하하 역시 이 마을은 내가 없으면 안되는군",
                "그래서 무슨 일이지?"
        );
        chat.setBaseMsg(true);
        Config.unloadObject(chat);

        chat = createChat("아무일도 아닙니다", 28L, "흠... 싱겁긴");
        Config.unloadObject(chat);

        chat = createChat(null, 29L, "... 안녕하세요");
        Config.unloadObject(chat);

        chat = createChat(null, 30L, "엄마가 모르는 사람이랑 얘기하지 말랬어요...");
        chat.setBaseMsg(true);
        Config.unloadObject(chat);

        chat = createChat("어... 그래", 31L, "...");
        Config.unloadObject(chat);
        
        chat = createChat(null, 32L,
                "음? 처음 보는 분이네요",
                "흐응, 그런 눈으로 보는걸 보니 엘프는 처음 보시나 보네요",
                "아무래도 인간들이 엘프를 처음 보면 그렇게 신기한 눈빛을 보내더라고요",
                "어쨌든 마을에 있는 동안 잘 부탁드려요"
        );
        Config.unloadObject(chat);
        
        chat = createChat(null, 33L, "안녕하세요");
        chat.setBaseMsg(true);
        Config.unloadObject(chat);

        chat = createChat("아 그냥 지나가다 들렀습니다", 34L,
                "오늘도 부지런하시네요",
                "가끔은 여유를 가지는 것도 좋아요",
                "다음번에 오실 땐 꽃 사러 오세요~"
        );
        Config.unloadObject(chat);
        
        chat = createChat(null, 35L,
                "...............",
                "아 멍때리고 있었어 미안",
                "__nickname 이라고? 그래 잘 부탁해",
                "너도 시간나면 여기서 낚시나 해보라구"
        );
        Config.unloadObject(chat);

        chat = createChat(null, 36L, "음? 왜?");
        chat.setBaseMsg(true);
        Config.unloadObject(chat);
        
        chat = createChat("그냥 뭐하나 싶어서", 37L,
                "엄... 나야 뭐 항상 여기서 낚싯대나 드리우고 있지",
                "그럼 잘가"
        );
        Config.unloadObject(chat);

        chat = createChat(null, 38L, "......");
        chat.setBaseMsg(true);
        Config.unloadObject(chat);
        
        chat = createChat(null, 39L,
                "잠시 기다려라...",
                "지금! 크 월척이군",
                "낚시 방해하지말고 조용히 가라");
        Config.unloadObject(chat);

        chat = createChat(null, 40L,"무슨 일로 찾아왔지");
        chat.setBaseMsg(true);
        Config.unloadObject(chat);

        chat = createChat("어... 글쎼요?", 41L, "꺼져");
        Config.unloadObject(chat);

        chat = createChat(null, 42L,
                "처음 뵙는 분이네요",
                "저는 이 마을의 광부 중 한명인 페드로 라고 합니다",
                "잘부탁드립니다");
        Config.unloadObject(chat);

        chat = createChat(null, 43L, "볼일 있으신가요?");
        chat.setBaseMsg(true);
        Config.unloadObject(chat);

        chat = createChat("아무것도 아닙니다", 44L,"네네 전 또 광산으로 들어가봐야곘네요. 수고하세요");
        Config.unloadObject(chat);
        
        chat = createChat(null, 45L,
                "흠... 자네는?",
                "__nickname 이라. 이건...? 아... 아닐세",
                "좀 특이해보여서 말이지. 잘 부탁하네"
        );
        Config.unloadObject(chat);

        chat = createChat(null, 46L, "할 말 있는가?");
        chat.setBaseMsg(true);
        Config.unloadObject(chat);

        chat = createChat("아... (입이 움직이지 않는다)", 47L,
                "아직은 이 정도 위압감도 버티기 힘든가 보군",
                "나중에 다시 오게나"
        );
        Config.unloadObject(chat);

        chat = createChat(null, 48L,
                "음? 안녕 처음 보는 사람이네",
                "나는 무명님께 가르침을 받고 있는 셀리나라고 해",
                "앞으로 잘 부탁해"
        );
        Config.unloadObject(chat);

        chat = createChat(null, 49L, "할 말 있어?");
        chat.setBaseMsg(true);
        Config.unloadObject(chat);

        chat = createChat("딱히..?", 50L,
                "할 말 없으면 수련이 끝나고 나중에 찾아와 줘",
                "수련중에 말하기는 힘들거든",
                "후욱... 지금도 겨우 하는거야"
        );
        Config.unloadObject(chat);

        chat = createChat("무명은 어떤 분이셔?", 51L,
                "그건 나도 말해주기 힘든데...",
                "말해줄 수 있는건 정말 강하시단거 정도?"
        );
        Config.unloadObject(chat);
        
        chat = createChat("후우... 지금은 어떤가요", 52L,
                "하하하 이제 이정도는 버틴다는 건가?",
                "그래 좋군, 하지만 아직 부족하네",
                "자네의 경험을 증명해 보게나",
                "하급 모험의 증표 3개, 하급 낚시꾼의 증표 7개, 하급 광부의 증표 30개",
                "받아들이곘는가?"
        );
        chat.setResponseChat(WaitResponse.YES, 53L, true);
        chat.setResponseChat(WaitResponse.NO, 54L, true);
        Config.unloadObject(chat);

        chat = createChat(null, 53L,
                "좋아 좋아. 그정도 의지는 있어야지",
                "기다리고 있겠네"
        );
        chat.getQuestId().set(5L);
        Config.unloadObject(chat);
        
        chat = createChat(null, 54L, 
                "흠... 그정도 의지도 없는겐가?",
                "약간 실망이군"
        );
        Config.unloadObject(chat);
        
        chat = createChat(null, 55L,
                "자네의 경험은 증명 됬네",
                "조금 더 성장해서 오는 그 때를 기대하고 있겠네"
        );
        Config.unloadObject(chat);
        
        chat = createChat("아무것도 아닙니다", 56L,
                "그래 빨리 성장해서 오게나"
        );
        Config.unloadObject(chat);

        chat = createChat("무명은 어떤 분이셔?", 57L,
                "내 스승님이자...",
                "과거 국가의 소드마스터이셨던 분",
                "딱 여기까지 알려줄 수 있겠네.."
        );
        Config.unloadObject(chat);

        chat = createChat("낚시는 할만 한가요?", 58L, 
                "하, 내가 누군데 당연히 잘 낚이지",
                "가끔 낚싯대가 부서지는게 문제지만...",
                "어? 이건 내 문제가 아니야. 낚싯대 문제라고...",
                "그래.. 낚싯대라... 낚싯대 재료 좀 구해와 봐",
                "실은 있으니 가죽 20개에 나뭇가지 10개면 적당할 것 같다"
        );
        chat.setResponseChat(WaitResponse.YES, 59L, true);
        chat.setResponseChat(WaitResponse.NO, 60L, true);
        Config.unloadObject(chat);
        
        chat = createChat(null, 59L, "최대한 빨리 구해와라");
        chat.getQuestId().set(QuestList.NEED_FISHING_ROD_ITEM.getId());
        Config.unloadObject(chat);

        chat = createChat(null, 60L, "...... 꺼져");
        Config.unloadObject(chat);
        
        chat = createChat(null, 61L,"그렇게 굼뱅이는 아니었군. 여기 보수다");
        Config.unloadObject(chat);
        
        chat = createChat("근데 어떤 수련을 하고 있는거야?", 62L,
                "음... 나는 마검사쪽을 공부하고 있어",
                "검술은 무명님께 배우면 되지만 아무래도 마법쪽은 어렵긴 하네",
                "아 말 나온 김에 혹시 하급 사냥꾼의 증표 5개랑 중급 사냥꾼의 증표 5개만 구해줄 수 있을까?",
                "사냥꾼의 증표에 있는 기운을 좀 쓰고 싶거든"
        );
        chat.setResponseChat(WaitResponse.YES, 63L, true);
        chat.setResponseChat(WaitResponse.NO, 64L, true);
        Config.unloadObject(chat);

        chat = createChat(null, 63L, "고마워! 다 구하면 다시 말 걸어줘");
        chat.getQuestId().set(QuestList.HUNTER_TALENT.getId());
        Config.unloadObject(chat);

        chat = createChat(null, 64L, "그래? 아쉽네...");
        Config.unloadObject(chat);
        
        chat = createChat(null, 65L,
                "고마워! 이제.....",
                "하.. 다시 수련하러 가야겠네..."
        );
        Config.unloadObject(chat);

        chat = createChat("뭔가 급해 보이시는데...", 66L,
                "아 __nickname 마침 잘왔군",
                "아내를 위한 선물을 해주려고 하는데 마땅한 아이템이 없어서 말이지",
                "혹시 금 5개와 1만 골드를 구해줄 수 있겠나?"
        );
        chat.setResponseChat(WaitResponse.YES, 67L, true);
        chat.setResponseChat(WaitResponse.NO, 68L, true);
        Config.unloadObject(chat);
        
        chat = createChat(null, 67L, "오 고맙군! 덕분에 선물을 만들어 줄 수 있곘군");
        chat.getQuestId().set(QuestList.GOLD_RING.getId());
        Config.unloadObject(chat);

        chat = createChat(null, 68L, "아쉽지만 별 수 없군! 다음에 또 들르게!");
        Config.unloadObject(chat);

        chat = createChat(null, 69L,
                "음... 보답을 해주고 싶은데 마땅한 광물이 없군",
                "아 이거라도 가져가게나! 꽤나 유용할거야"
        );
        Config.unloadObject(chat);

        chat = createChat("열심히 일하고 계시네요!", 70L,
                "음... 사실 형석씨한테 석탄을 납품하기로 했거든요",
                "근데 누가 석탄을 다 캐가고 있는건지 잘 안보여서 막막하네요",
                "혹시 석탄 남으시는거 있으시면 하급 강화석과 바꿀 수 있을까요?"
        );
        chat.setResponseChat(WaitResponse.YES, 71L, true);
        chat.setResponseChat(WaitResponse.NO, 72L, true);
        Config.unloadObject(chat);
        
        chat = createChat(null, 71L, 
                "오 감사합니다!",
                "일단... 석탄 100개 정도면 적당할 것 같아요",
                "다 구하시면 다시 와주세요!"
        );
        chat.getQuestId().set(QuestList.NEED_COAL.getId());
        Config.unloadObject(chat);

        chat = createChat(null, 72L,
                "아... 별 수없죠",
                "저는 석탄이나 더 찾아봐야겠네요"
        );
        Config.unloadObject(chat);
        
        chat = createChat(null, 73L, 
                "와 빨리 구해와주셨네요!",
                "덕분에 일이 훨씬 편해졌습니다",
                "여기 드리기로 한 하급 강화석이예요"
        );
        Config.unloadObject(chat);

        chat = createChat("기분 좋아 보이시는데요?", 74L,
                "아 오랜만이예요 __nickname 씨",
                "사실 그이가 선물을 줬거든요",
                "아 __nickname 씨는 모험가이시니까 다양한 재료를 구해올 수 있을 것 같은데...",
                "저도 보답으로 선물을 주고 싶어서요",
                "발광석이랑 물고기 몇마리정도면 될 것 같은데 구해와주실 수 있을까요?"
        );
        chat.setResponseChat(WaitResponse.YES, 75L, true);
        chat.setResponseChat(WaitResponse.NO, 76L, true);
        Config.unloadObject(chat);

        chat = createChat(null, 75L,
                "와아 고마워요",
                "그럼 최대한 빨리 부탁할게요?"
        );
        chat.getQuestId().set(QuestList.ANOTHER_PRESENT.getId());
        Config.unloadObject(chat);

        chat = createChat(null, 76L, "흐응.... 알겠어요");
        Config.unloadObject(chat);

        chat = createChat(null, 77L,
                "음~ 이정도면 될 것 같네요 고마워요",
                "음... 답례로 드릴 수 있는건 없지만 제 힘을 좀 써드릴게요",
                "다음에도 종종 부탁드릴게요?"
        );
        Config.unloadObject(chat);
        
        chat = createChat("혹시 다른 할 일이 있을까 싶어 와봤습니다", 78L,
                "허허 자네가 열심히 일을 해준 덕분에 지금은 딱히 없네",
                "아 그건 그렇고 마침 자네에게 할 말이 있었는데 잘 왔구만",
                "이번에 국가에서 모험가들을 장려한다고 일정 수준 이상의 모험가들에게 이 종이를 주라고 했네",
                "음.. 이름이 아마 스텟 머시깽이 였는데... 어쨌든...",
                "기준이 아마 50레벨 이었을테니 50레벨을 달성하면 와서 받아가게나"
        );
        chat.getQuestId().set(QuestList.LV50.getId());
        Config.unloadObject(chat);

        chat = createChat(null, 79L,
                "역시 자네라면 금방 받아갈 줄 알았지 허허",
                "자 여기 유용하게 쓰게나"
        );
        chat.setResponseChat(WaitResponse.NONE, 80L, true);
        Config.unloadObject(chat);

        chat = createChat(null, 80L,
                "아아 하나 까먹은게 있네",
                "이번 지원은 50레벨로 끝나는게 아니라 100레벨에서도 받을 수 있다고 하네",
                "100레벨을 달성하면 와서 그것도 받아가게나"
        );
        chat.getQuestId().set(QuestList.LV100.getId());
        Config.unloadObject(chat);
        
        chat = createChat("너답지 않게 고민이 있어 보이네?", 81L,
                "아아 마침 잘왔어 __nickname",
                "얼마 뒤에 제사가 있거든",
                "생선은 넉넉한데 평소에 돼지를 좋아하셔서 돼지 머리를 놓고 싶은데 어떻게 구해야할지 생각하고 있었어",
                "그래서 말인데 혹시 돼지머리 3개만 구해줄 수 있을까?"
        );
        chat.setResponseChat(WaitResponse.YES, 82L, true);
        chat.setResponseChat(WaitResponse.NO, 83L, true);
        Config.unloadObject(chat);
        
        chat = createChat(null, 82L, "고마워. 대신 구해오면 괜찮은거 알려줄게");
        Config.unloadObject(chat);
        
        chat = createChat(null, 83L,
                "그래? 바쁜가보네",
                "혹시 나중에라도 생각 바뀌면 다시 찾아와",
                "하하.. 나야 하루종일 여기 있는거 알잖아?"
        );
        Config.unloadObject(chat);
        
        chat = createChat(null, 84L,
                "오.. 구해왔구나?",
                "자 이 책 받아가"
        );
        Config.unloadObject(chat);

        chat = createChat("수련은 할만 해?", 85L,
                "좀 힘들긴 해도 재밌어. 그리고 힘들어도 이겨내야겠지 헤헤",
                "아 맞다, 수련하는데에 필요한 아이템이 있는데 좀 구해줄 수 있을까?",
                "거미의 눈에 마법의 힘이 담겨있다고 해서 일단 5개 정도만 구해주면 될 것 같은데"
        );
        chat.setResponseChat(WaitResponse.YES, 86L, true);
        chat.setResponseChat(WaitResponse.NO, 87L, true);
        Config.unloadObject(chat);

        chat = createChat(null, 86L,
                "아 다행이다",
                "그럼 다 구하고 다시 말걸어줘"
        );
        chat.getQuestId().set(QuestList.MAGIC_OF_SPIDER_EYE.getId());
        Config.unloadObject(chat);

        chat = createChat(null, 87L, "음 직접 구해야 할려나...?");
        Config.unloadObject(chat);
        
        chat = createChat(null, 88L,
                "와 고마워!",
                "앞으로도 종종 부탁해? 헤헤"
        );
        Config.unloadObject(chat);

        chat = createChat("으... 바닥에 이 끈적끈적한건 다 뭔가요?", 89L,
                "아.. 우리 봄이가 놀러 갔다가 실수로 슬라임 늪지 근처까지 갔지 뭐예요",
                "근데 요즘 슬라임이 많아져서 그런지 늪지 바깥까지 슬라임이 가끔 나오거든요",
                "그래서 겨우 도망쳐왔다고 하는데... 걱정이네요",
                "아 혹시 슬라임 20마리만 잡아주실 수 있을까요?",
                "슬라임 처리 증거로 슬라임 조각 20개면 충분할것 같은데..."
        );
        chat.setResponseChat(WaitResponse.YES, 90L, true);
        chat.setResponseChat(WaitResponse.NO, 91L, true);
        Config.unloadObject(chat);

        chat = createChat(null, 90L,
                "정말 고마워요!",
                "보수는 넉넉히 챙겨 드릴게요"
        );
        chat.getQuestId().set(QuestList.STICKY_SLIME.getId());
        Config.unloadObject(chat);

        chat = createChat(null, 91L,
                "그래도 아이의 안전을 위한건데...",
                "할 일만 다 끝내면 다시 와주세요..."
        );
        Config.unloadObject(chat);

        chat = createChat(null, 92L,
                "덕분에 마음이 좀 놓이네요",
                "고마워요. 여기 약속한 보수예요"
        );
        Config.unloadObject(chat);

        chat = createChat("꽃들이 좀 시들해진 것 같네요. 무슨 문제라도 있나요?", 93L,
                "흐응... 요즘 날씨때문에 엘프의 기운으로도 꽃이 잘 안자라네요",
                "이럴때는 엘릭서 허브만 있으면 될텐데...",
                "혹시 약초를 좀 캐와주실 수 있을까요?",
                "약초 10개정도를 구해와주시면 제가 엘릭서 허브를 만들어서 하나 나눠드릴게요"
        );
        chat.setResponseChat(WaitResponse.YES, 94L, true);
        chat.setResponseChat(WaitResponse.NO, 95L, true);
        Config.unloadObject(chat);

        chat = createChat(null, 94L,
                "그럼 빨리 구해와주세요~"
        );
        chat.getQuestId().set(QuestList.HERB_COLLECTING.getId());
        Config.unloadObject(chat);

        chat = createChat(null, 95L, "아쉽네요, 엘릭서 허브에 관심이 있으실 줄 알았는데...");
        Config.unloadObject(chat);

        chat = createChat(null, 96L,
                "잠시만 기다려주세요?",
                "(숲의 기운이 강하게 느껴진다)",
                "여기 주기로 했던 엘릭서 허브예요"
        );
        chat.getPauseTime().set(4000L);
        Config.unloadObject(chat);

        chat = createChat(null, 97L, "힝...");
        Config.unloadObject(chat);

        for(int i = 0; i < Config.GEMS.length; i++) {
            long chatId = 98L + (i * 2);
            String gemName = Config.GEMS[i].getDisplayName();

            chat = createChat("보석 수집 - " + gemName, chatId,
                    "모험가님... 혹시 " + gemName + " 하나 구해줄 수 있어요?",
                    "보석을 모으고 있는데 " + gemName + "이 필요해서요..."
            );
            chat.setResponseChat(WaitResponse.YES, chatId + 1, true);
            chat.setResponseChat(WaitResponse.NO, 97L, true);
            Config.unloadObject(chat);

            chat = createChat(null, chatId + 1, "감사합니다");
            chat.getQuestId().set(QuestList.GEM_COLLECTING_QUARTZ.getId() + i);
            Config.unloadObject(chat);
        }

        chat = createChat(null, 128L,
                "(엘프가 쓰러져있다)",
                "으으........",
                "(엘프의 치료법을 묻기 위해 " + Emoji.focus("엘") + " 에게 가보자)"
        );
        chat.getQuestId().set(QuestList.HEALING_ELF1.getId());
        Config.unloadObject(chat);

        chat = createChat(null, 129L,
                "숲에 엘프가 쓰러져있다고요?",
                "이런... 저는 지금 봄이때문에 가기가 좀 그런데...",
                "일단 엘릭서 허브를 그 엘프에게 주면 어느정도 회복할거예요",
                "제가 가지고 있는 엘릭서 허브를 하나 드릴테니 가서 회복시켜주세요"
        );
        chat.getQuestId().set(QuestList.HEALING_ELF2.getId());
        Config.unloadObject(chat);

        chat = createChat(null, 130L,
                "......",
                "감사합니다...",
                "도와주셔서 감사하지만 일단 어느정도 회복을 해야할 것 같아요",
                "나중에 와주세요"
        );
        Config.unloadObject(chat);

        chat = createChat("이 시간까지 낚시를 하고 있는거야?", 131L, "zzz......");
        Config.unloadObject(chat);

        Config.ID_COUNT.put(Id.CHAT, Math.max(Config.ID_COUNT.get(Id.CHAT), 132L));
        Logger.i("ObjectMaker", "Chat making is done!");
   }

   private static void createQuests() {
       Quest quest = new Quest("광부의 일", NpcList.NOAH.getId(), 13L);
       quest.setNeedItem(ItemList.STONE.getId(), 30);
       quest.setRewardCloseRate(NpcList.NOAH.getId(), 1, true);
       quest.getRewardExp().set(35000L);
       quest.getRewardMoney().set(250L);
       Config.unloadObject(quest);

       quest = new Quest("쓰레기 수거", NpcList.NOAH.getId(), 13L);
       quest.setNeedItem(ItemList.TRASH.getId(), 3);
       quest.setRewardCloseRate(NpcList.NOAH.getId(), 1, true);
       quest.getRewardExp().set(100000L);
       Config.unloadObject(quest);

       quest = new Quest("불이 필요해!", NpcList.NOAH.getId(), 19L);
       quest.setNeedItem(ItemList.RED_SPHERE.getId(), 1);
       quest.setRewardCloseRate(NpcList.NOAH.getId(), 5, true);
       quest.getRewardExp().set(300000L);
       quest.getRewardMoney().set(500L);
       Config.unloadObject(quest);
       
       quest = new Quest("불이 너무 강했나?", NpcList.NOAH.getId(), 23L);
       quest.setNeedItem(ItemList.LOW_MP_POTION.getId(), 3);
       quest.setRewardCloseRate(NpcList.NOAH.getId(), 10, true);
       quest.getRewardItem().put(ItemList.STAT_POINT.getId(), 30);
       Config.unloadObject(quest);
       
       quest = new Quest("경험을 증명해라", NpcList.MOO_MYEONG.getId(), 55L);
       quest.setNeedItem(ItemList.LOW_ADV_TOKEN.getId(), 3);
       quest.setNeedItem(ItemList.LOW_FISH_TOKEN.getId(), 7);
       quest.setNeedItem(ItemList.LOW_MINER_TOKEN.getId(), 30);
       quest.setRewardCloseRate(NpcList.MOO_MYEONG.getId(), 10, true);
       quest.setRewardCloseRate(NpcList.SELINA.getId(), 10, true);
       quest.setRewardItem(ItemList.LOW_EXP_POTION.getId(), 30);
       quest.setRewardItem(ItemList.ADV_STAT.getId(), 20);
       Config.unloadObject(quest);

       quest = new Quest("낚싯대 재료 구해오기", NpcList.KANG_TAE_GONG.getId(), 61L);
       quest.setNeedItem(ItemList.LEATHER.getId(), 20);
       quest.setNeedItem(ItemList.BRANCH.getId(), 10);
       quest.setRewardCloseRate(NpcList.KANG_TAE_GONG.getId(), 10, true);
       quest.setRewardItem(ItemList.SMALL_GOLD_BAG.getId(), 2);
       quest.getRewardExp().set(100000L);
       Config.unloadObject(quest);

       quest = new Quest("증표의 힘?", NpcList.SELINA.getId(), 65L);
       quest.setNeedItem(ItemList.LOW_HUNTER_TOKEN.getId(), 5);
       quest.setNeedItem(ItemList.HUNTER_TOKEN.getId(), 5);
       quest.setRewardCloseRate(NpcList.SELINA.getId(), 1, true);
       quest.setRewardItem(ItemList.EXP_POTION.getId(), 1);
       Config.unloadObject(quest);

       quest = new Quest("금반지 선물", NpcList.HYEONG_SEOK.getId(), 69L);
       quest.setNeedItem(ItemList.GOLD.getId(), 5);
       quest.getNeedMoney().set(5000L);
       quest.setRewardCloseRate(NpcList.HYEONG_SEOK.getId(), 10, true);
       quest.setRewardCloseRate(NpcList.EL.getId(), 10, true);
       quest.setRewardItem(ItemList.LOW_AMULET.getId(), 1);
       Config.unloadObject(quest);

       quest = new Quest("석탄이 다 어디갔지?", NpcList.PEDRO.getId(), 73L);
       quest.setNeedItem(ItemList.COAL.getId(), 100);
       quest.setRewardCloseRate(NpcList.PEDRO.getId(), 2, true);
       quest.setRewardItem(ItemList.LOW_REINFORCE_STONE.getId(), 1);
       Config.unloadObject(quest);
       
       quest = new Quest("또 다른 선물", NpcList.EL.getId(), 77L);
       quest.setNeedItem(ItemList.GLOW_STONE.getId(), 10);
       quest.setNeedItem(ItemList.COMMON_FISH1.getId(), 3);
       quest.setNeedItem(ItemList.COMMON_FISH2.getId(), 3);
       quest.setNeedItem(ItemList.COMMON_FISH3.getId(), 3);
       quest.setNeedItem(ItemList.COMMON_FISH4.getId(), 3);
       quest.setNeedItem(ItemList.COMMON_FISH5.getId(), 3);
       quest.setRewardCloseRate(NpcList.HYEONG_SEOK.getId(), 10, true);
       quest.setRewardCloseRate(NpcList.EL.getId(), 10, true);
       quest.setRewardStat(StatType.MAXHP, 30);
       Config.unloadObject(quest);
       
       quest = new Quest("50레벨 달성", NpcList.NOAH.getId(), 79L);
       quest.getClearLimitLv().set(50);
       quest.setRewardItem(ItemList.STAT_POINT.getId(), 50);
       Config.unloadObject(quest);

       quest = new Quest("100레벨 달성", NpcList.NOAH.getId(), 79L);
       quest.setRewardItem(ItemList.STAT_POINT.getId(), 100);
       Config.unloadObject(quest);
       
       quest = new Quest("제사용 돼지머리", NpcList.JOON_SIK.getId(), 84L);
       quest.setNeedItem(ItemList.PIG_HEAD.getId(), 3);
       quest.getRewardItemRecipe().add(ItemList.COMMON_GRILLED_FISH.getId());
       quest.getRewardItemRecipe().add(ItemList.RARE_GRILLED_FISH.getId());
       quest.getRewardItemRecipe().add(ItemList.SPECIAL_GRILLED_FISH.getId());
       quest.getRewardItemRecipe().add(ItemList.UNIQUE_GRILLED_FISH.getId());
       quest.getRewardItemRecipe().add(ItemList.LEGENDARY_GRILLED_FISH.getId());
       quest.getRewardItemRecipe().add(ItemList.MYSTIC_GRILLED_FISH.getId());
       quest.getRewardItemRecipe().add(ItemList.COOKED_LAMB.getId());
       quest.getRewardItemRecipe().add(ItemList.COOKED_PORT.getId());
       quest.getRewardItemRecipe().add(ItemList.COOKED_BEEF.getId());
       quest.setRewardCloseRate(NpcList.JOON_SIK.getId(), 10, true);
       Config.unloadObject(quest);

       quest = new Quest("거미 눈의 마법", NpcList.SELINA.getId(), 88L);
       quest.setNeedItem(ItemList.SPIDER_EYE.getId(), 5);
       quest.setRewardCloseRate(NpcList.SELINA.getId(), 3, true);
       quest.getRewardExp().set(100000L);
       Config.unloadObject(quest);

       quest = new Quest("끈적한 슬라임", NpcList.EL.getId(), 92L);
       quest.setNeedItem(ItemList.PIECE_OF_SLIME.getId(), 20);
       quest.setRewardCloseRate(NpcList.EL.getId(), 20, true);
       quest.setRewardCloseRate(NpcList.BOAM_E.getId(), 10, true);
       quest.getRewardExp().set(500000L);
       quest.getRewardMoney().set(10000L);
       Config.unloadObject(quest);

       quest = new Quest("약초 모으기", NpcList.EL.getId(), 96L);
       quest.setNeedItem(ItemList.HERB.getId(), 10);
       quest.setRewardItem(ItemList.ELIXIR_HERB.getId(), 1);
       Config.unloadObject(quest);

       for(int i = 0; i < Config.GEMS.length; i++) {
           ItemList gem = Config.GEMS[i];

           quest = new Quest("보석 수집 - " + gem.getDisplayName(), NpcList.BOAM_E.getId(), 99L + (i * 2));
           quest.setNeedItem(gem.getId(), 1);
           quest.setRewardCloseRate(NpcList.BOAM_E.getId(), 5, true);
           quest.setRewardItem(ItemList.STAT_POINT.getId(), 5);
           Config.unloadObject(quest);
       }
       
       quest = new Quest("엘프 치료하기1", NpcList.EL.getId(), 129L);
       quest.getRewardExp().set(100000L);
       quest.setRewardItem(ItemList.ELIXIR_HERB.getId(), 1);
       Config.unloadObject(quest);

       quest = new Quest("엘프 치료하기2", NpcList.SYLVIA.getId(), 130L);
       quest.setNeedItem(ItemList.ELIXIR_HERB.getId(), 1);
       quest.getRewardExp().set(100000L);
       quest.setRewardCloseRate(NpcList.SYLVIA.getId(), 10, true);
       Config.unloadObject(quest);

       Config.ID_COUNT.put(Id.QUEST, Math.max(Config.ID_COUNT.get(Id.QUEST), 35L));
       Logger.i("ObjectMaker", "Quest making is done!");
   }

    private static void createNpc() {
        Npc npc = new Npc("???");
        npc.getLocation().set(0, 0, 1, 1);
        Config.unloadObject(npc);

        npc = new Npc("아벨");
        npc.getLocation().set(0, 0, 1, 1);
        Config.unloadObject(npc);


        npc = new Npc("노아");
        npc.getLocation().set(0, 0, 16, 16);
        npc.setFirstChat(5L);

        npc.setBaseChat(new ChatLimit(), 6L);

        ChatLimit chatLimit = new ChatLimit();
        chatLimit.getLimitHour1().set(7, 11);
        npc.setBaseChat(chatLimit, 14L);

        chatLimit = new ChatLimit();
        chatLimit.getLimitHour1().set(12, 14);
        npc.setBaseChat(chatLimit, 15L);

        chatLimit = new ChatLimit();
        chatLimit.getLimitHour1().set(18, 22);
        npc.setBaseChat(chatLimit, 16L);

        npc.setChat(new ChatLimit(), 7L);

        chatLimit = new ChatLimit();
        chatLimit.getNotRunningQuest().add(QuestList.WORK_OF_MINER.getId());
        npc.setChat(chatLimit, 8L);

        chatLimit = new ChatLimit();
        chatLimit.getNotRunningQuest().add(QuestList.TRASH_COLLECTING.getId());
        npc.setChat(chatLimit, 9L);

        chatLimit = new ChatLimit();
        chatLimit.getNotRunningQuest().add(QuestList.NEED_FIRE.getId());
        chatLimit.getClearedQuest().put(QuestList.WORK_OF_MINER.getId(), 5);
        chatLimit.getClearedQuest().put(QuestList.TRASH_COLLECTING.getId(), 5);
        chatLimit.getNotClearedQuest().add(QuestList.NEED_FIRE.getId());
        npc.setChat(chatLimit, 17L);

        chatLimit = new ChatLimit();
        chatLimit.getNotRunningQuest().add(QuestList.TOO_STRONG_FIRE.getId());
        chatLimit.getClearedQuest().put(QuestList.NEED_FIRE.getId(), 1);
        chatLimit.getNotClearedQuest().add(QuestList.TOO_STRONG_FIRE.getId());
        npc.setChat(chatLimit, 20L);

        chatLimit = new ChatLimit();
        chatLimit.getNotClearedQuest().add(QuestList.LV50.getId());
        chatLimit.getNotRunningQuest().add(QuestList.LV50.getId());
        chatLimit.getLimitQuest().addMin(QuestList.WORK_OF_MINER.getId(), 5);
        chatLimit.getLimitQuest().addMin(QuestList.TRASH_COLLECTING.getId(), 5);
        npc.setChat(chatLimit, 78L);

        Config.unloadObject(npc);


        npc = new Npc("형석");
        npc.getLocation().set(0, 0, 40, 40);
        npc.setFirstChat(25L);

        npc.setBaseChat(new ChatLimit(), 26L);

        chatLimit = new ChatLimit();
        chatLimit.getLimitHour1().set(22, 23);
        chatLimit.getLimitHour2().set(0, 6);
        npc.setBaseChat(chatLimit, 27L);

        npc.setChat(new ChatLimit(), 28L);

        chatLimit = new ChatLimit();
        chatLimit.getNotRunningQuest().add(QuestList.GOLD_RING.getId());
        chatLimit.getNotClearedQuest().add(QuestList.GOLD_RING.getId());
        chatLimit.getLimitQuest().addMin(QuestList.WORK_OF_MINER.getId(), 10);
        npc.setChat(chatLimit, 66L);

        Config.unloadObject(npc);


        npc = new Npc("봄이");
        npc.getLocation().set(0, 0, 41, 40);
        npc.setFirstChat(29L);

        chatLimit = new ChatLimit();
        chatLimit.getLimitHour1().set(6, 23);
        npc.setBaseChat(chatLimit, 30L);

        npc.setChat(new ChatLimit(), 31L);

        for(int i = 0; i < Config.GEMS.length; i++) {
            chatLimit = new ChatLimit();
            chatLimit.getLimitQuest().addMin(QuestList.STICKY_SLIME.getId(), 1);
            chatLimit.getNotRunningQuest().add(QuestList.GEM_COLLECTING_QUARTZ.getId() + i);
            chatLimit.getNotClearedQuest().add(QuestList.GEM_COLLECTING_QUARTZ.getId() + i);
            npc.setChat(chatLimit, 98 + (i * 2));
        }

        Config.unloadObject(npc);


        npc = new Npc("엘");
        npc.getLocation().set(0, 0, 41, 40);
        npc.setFirstChat(32L);

        chatLimit = new ChatLimit();
        chatLimit.getLimitHour1().set(6, 23);
        npc.setBaseChat(chatLimit, 33L);

        npc.setChat(new ChatLimit(), 34L);

        chatLimit = new ChatLimit();
        chatLimit.getNotRunningQuest().add(QuestList.ANOTHER_PRESENT.getId());
        chatLimit.getNotClearedQuest().add(QuestList.ANOTHER_PRESENT.getId());
        chatLimit.getClearedQuest().put(QuestList.GOLD_RING.getId(), 1);
        npc.setChat(chatLimit, 74L);

        chatLimit = new ChatLimit();
        chatLimit.getNotRunningQuest().add(QuestList.STICKY_SLIME.getId());
        chatLimit.getNotClearedQuest().add(QuestList.STICKY_SLIME.getId());
        chatLimit.getClearedQuest().put(QuestList.ANOTHER_PRESENT.getId(), 1);
        npc.setChat(chatLimit, 89L);

        chatLimit = new ChatLimit();
        chatLimit.getNotRunningQuest().add(QuestList.HERB_COLLECTING.getId());
        chatLimit.getClearedQuest().put(QuestList.ANOTHER_PRESENT.getId(), 1);
        npc.setChat(chatLimit, 93L);

        Config.unloadObject(npc);


        npc = new Npc("준식");
        npc.getLocation().set(1, 1,32, 32);
        npc.setFirstChat(35L);

        chatLimit = new ChatLimit();
        chatLimit.getLimitHour1().set(8, 22);
        npc.setBaseChat(chatLimit, 36L);

        chatLimit = new ChatLimit();
        chatLimit.getLimitHour1().set(23, 23);
        chatLimit.getLimitHour2().set(0, 7);
        npc.setBaseChat(chatLimit, 38L);

        chatLimit = new ChatLimit();
        chatLimit.getLimitHour1().set(8, 22);
        npc.setChat(chatLimit, 37L);

        chatLimit = new ChatLimit();
        chatLimit.getLimitHour1().set(23, 23);
        chatLimit.getLimitHour2().set(0, 7);
        npc.setChat(chatLimit, 131L);

        chatLimit = new ChatLimit();
        chatLimit.getLimitQuest().addMin(QuestList.NEED_FISHING_ROD_ITEM.getId(), 1);
        chatLimit.getNotClearedQuest().add(QuestList.MEMORIAL_CEREMONY.getId());
        chatLimit.getNotRunningQuest().add(QuestList.MEMORIAL_CEREMONY.getId());
        npc.setChat(chatLimit, 81L);

        Config.unloadObject(npc);


        npc = new Npc("강태공");
        npc.getLocation().set(0, 1,32, 32);
        npc.setFirstChat(39L);

        npc.setBaseChat(new ChatLimit(), 40L);

        npc.setChat(new ChatLimit(), 41L);

        chatLimit = new ChatLimit();
        chatLimit.getNotRunningQuest().add(QuestList.NEED_FISHING_ROD_ITEM.getId());
        chatLimit.getNotClearedQuest().add(QuestList.NEED_FISHING_ROD_ITEM.getId());
        chatLimit.getLimitQuest().addMin(QuestList.TRASH_COLLECTING.getId(), 5);
        npc.setChat(chatLimit, 58L);

        Config.unloadObject(npc);


        npc = new Npc("페드로");
        npc.getLocation().set(0, 0,64, 64);
        npc.setFirstChat(42L);

        npc.setBaseChat(new ChatLimit(), 43L);

        npc.setChat(new ChatLimit(), 44L);

        chatLimit = new ChatLimit();
        chatLimit.getNotRunningQuest().add(QuestList.NEED_COAL.getId());
        npc.setChat(chatLimit, 70L);

        Config.unloadObject(npc);


        npc = new Npc("무명");
        npc.getLocation().set(1, 0,1, 1);
        npc.setFirstChat(45L);

        npc.setBaseChat(new ChatLimit(), 46L);

        chatLimit = new ChatLimit();
        chatLimit.getLimitLv().set(1, 10);
        npc.setChat(chatLimit, 47L);

        chatLimit = new ChatLimit();
        chatLimit.getLimitLv().set(11, Config.MAX_LV);
        chatLimit.getNotClearedQuest().add(QuestList.PROVE_EXPERIENCE.getId());
        chatLimit.getNotRunningQuest().add(QuestList.PROVE_EXPERIENCE.getId());
        npc.setChat(chatLimit, 52L);

        chatLimit = new ChatLimit();
        chatLimit.getLimitCloseRate().getMin().put(NpcList.MOO_MYEONG.getId(), 10);
        npc.setChat(chatLimit, 56L);

        Config.unloadObject(npc);


        npc = new Npc("셀리나");
        npc.getLocation().set(1, 0,2, 2);
        npc.setFirstChat(48L);

        npc.setBaseChat(new ChatLimit(), 49L);

        chatLimit = new ChatLimit();
        chatLimit.getLimitHour1().set(9, 20);
        npc.setChat(chatLimit, 50L);

        chatLimit = new ChatLimit();
        chatLimit.getLimitHour1().set(21, 23);
        chatLimit.getLimitHour2().set(0, 8);
        chatLimit.getLimitCloseRate().addMax(NpcList.SELINA.getId(), 9);
        npc.setChat(chatLimit, 51L);

        chatLimit = new ChatLimit();
        chatLimit.getLimitCloseRate().addMin(NpcList.SELINA.getId(), 10);
        npc.setChat(chatLimit, 57L);

        chatLimit = new ChatLimit();
        chatLimit.getLimitCloseRate().addMin(NpcList.SELINA.getId(), 10);
        chatLimit.getNotRunningQuest().add(QuestList.HUNTER_TALENT.getId());
        npc.setChat(chatLimit, 62L);

        chatLimit = new ChatLimit();
        chatLimit.getLimitCloseRate().addMin(NpcList.SELINA.getId(), 10);
        chatLimit.getNotRunningQuest().add(QuestList.MAGIC_OF_SPIDER_EYE.getId());
        npc.setChat(chatLimit, 85L);

        Config.unloadObject(npc);


        npc = new Npc("실비아");
        npc.getLocation().set(3, 1,16, 16);
        npc.setFirstChat(128L);

        Config.unloadObject(npc);


        Config.ID_COUNT.put(Id.NPC, Math.max(Config.ID_COUNT.get(Id.NPC), 12L));
        Logger.i("ObjectMaker", "Npc making is done!");
    }

}
