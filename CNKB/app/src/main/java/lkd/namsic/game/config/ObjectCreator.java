package lkd.namsic.game.config;

import android.util.Log;
import android.widget.Button;

import androidx.annotation.NonNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import lkd.namsic.MainActivity;
import lkd.namsic.game.base.ChatLimit;
import lkd.namsic.game.enums.Id;
import lkd.namsic.game.enums.MapType;
import lkd.namsic.game.enums.MonsterType;
import lkd.namsic.game.enums.StatType;
import lkd.namsic.game.enums.WaitResponse;
import lkd.namsic.game.enums.object_list.ItemList;
import lkd.namsic.game.gameObject.Chat;
import lkd.namsic.game.gameObject.Item;
import lkd.namsic.game.gameObject.GameMap;
import lkd.namsic.game.gameObject.Monster;
import lkd.namsic.game.gameObject.Npc;
import lkd.namsic.game.gameObject.Player;
import lkd.namsic.game.gameObject.Quest;
import lkd.namsic.game.manager.ItemManager;
import lkd.namsic.setting.Logger;

public class ObjectCreator {

    private static final ItemManager itemManager = ItemManager.getInstance();

    public static void start(Button button) {
        Logger.i("ObjectMaker", "Making objects...");

        Thread thread = new Thread(() -> {
            MainActivity.mainActivity.runOnUiThread(() -> button.setEnabled(false));

            try {
                //Name duplicate check
                int totalCount = ItemList.idMap.size() + ObjectList.equipList.size();

                Set<String> list = new HashSet<>(totalCount);
                list.addAll(ItemList.nameMap.keySet());
                list.addAll(ObjectList.equipList.keySet());
                if(list.size() != totalCount) {
                    throw new RuntimeException("Duplicate item or equip name");
                }

                Config.IGNORE_FILE_LOG = true;

                //TODO: 퀘스트, 몬스터, 장비, npc 추가
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
            int money = new Random().nextInt(100 * self.getLv().get()) + 500;

            self.addMoney(money);
            return "골드 주머니를 사용하여 " + money + "G 를 획득했습니다\n" + Emoji.GOLD + " 골드: " + self.getMoney() + "\n";
        });
        Config.unloadObject(item);

        item = new Item("골드 보따리", "골드가 들어간 주머니다");
        item.getHandleLv().set(5);
        item.setUse((self, other) -> {
            int money = new Random().nextInt(500 * self.getLv().get()) + 3000;

            self.addMoney(money);
            return "골드 보따리를 사용하여 " + money + "G 를 획득했습니다\n" + Emoji.GOLD + " 골드: " + self.getMoney() + "\n";
        });
        Config.unloadObject(item);

        createItem("약초", "약의 기운이 있는 풀이다");

        item = new Item("마나 조각", "마나의 기운이 있는 고체 마나 파편이다");
        for(long itemId = ItemList.RED_SPHERE.getId(); itemId < ItemList.WHITE_SPHERE.getId(); itemId++) {
            long itemId_ = itemId;
            
            item.addRecipe(new HashMap<Long, Integer>() {{
                put(itemId_, 3);
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

        createItem("돌", "가장 기본적인 광석이다");

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

        createItem("(일반) 금강모치", "일반 등급의 물고기다", 2);
        createItem("(일반) 미꾸라지", "일반 등급의 물고기다", 2);
        createItem("(일반) 붕어", "일반 등급의 물고기다", 2);
        createItem("(일반) 송사리", "일반 등급의 물고기다", 2);
        createItem("(일반) 피라미", "일반 등급의 물고기다", 2);

        createItem("(희귀) 망둑어", "희귀 등급의 물고기다", 3);
        createItem("(희귀) 미꾸라지", "희귀 등급의 물고기다", 3);
        createItem("(희귀) 배스", "희귀 등급의 물고기다", 3);
        createItem("(희귀) 살치", "희귀 등급의 물고기다", 3);
        createItem("(희귀) 쏘가리", "희귀 등급의 물고기다", 3);
        createItem("(희귀) 은어", "희귀 등급의 물고기다", 3);

        createItem("(특별) 강준치", "특별 등급의 물고기다", 5);
        createItem("(특별) 망둑어", "특별 등급의 물고기다", 5);
        createItem("(특별) 메기", "특별 등급의 물고기다", 5);
        createItem("(특별) 뱀장어", "특별 등급의 물고기다", 5);
        createItem("(특별) 산천어", "특별 등급의 물고기다", 5);
        createItem("(특별) 숭어", "특별 등급의 물고기다", 5);
        createItem("(특별) 쏘가리", "특별 등급의 물고기다", 5);
        createItem("(특별) 연어", "특별 등급의 물고기다", 5);
        createItem("(특별) 은어", "특별 등급의 물고기다", 5);
        createItem("(특별) 잉어", "특별 등급의 물고기다", 5);

        createItem("(유일) 강준치", "유일 등급의 물고기다", 8);
        createItem("(유일) 메기", "유일 등급의 물고기다", 8);
        createItem("(유일) 뱀장어", "유일 등급의 물고기다", 8);
        createItem("(유일) 산천어", "유일 등급의 물고기다", 8);
        createItem("(유일) 숭어", "유일 등급의 물고기다", 8);
        createItem("(유일) 연어", "유일 등급의 물고기다", 8);
        createItem("(유일) 잉어", "유일 등급의 물고기다", 8);

        createItem("(전설) 다금바리", "전설 등급의 물고기다", 10);
        createItem("(전설) 돗돔", "전설 등급의 물고기다", 10);
        createItem("(전설) 자치", "전설 등급의 물고기다", 10);
        createItem("(전설) 쿠니마스", "전설 등급의 물고기다", 10);

        createItem("(신화) 실러캔스", "신화 등급의 물고기다", 12);
        createItem("(신화) 폐어", "신화 등급의 물고기다", 12);

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

        item = new Item("하급 제작법", "장비의 제작법을 무작위로 1개 획득할 수 있다(중복 가능)");
        item.setUse((self, other) -> {
            itemManager.giveLowRecipe((Player) self);
            return null;
        });
        Config.unloadObject(item);

        item = new Item("중급 제작법", "장비의 제작법을 무작위로 1개 획득할 수 있다(중복 가능)");
        item.setUse((self, other) -> {
            itemManager.giveMiddleRecipe((Player) self);
            return null;
        });
        Config.unloadObject(item);

        item = new Item("상급 제작법", "장비의 제작법을 무작위로 1개 획득할 수 있다(중복 가능)");
        item.setUse((self, other) -> {
            itemManager.giveHighRecipe((Player) self);
            return null;
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
            long exp = new Random().nextInt(2000 * self.getLv().get()) + 20000;

            Player player = (Player) self;
            player.addExp(exp);
            return "하급 경험치 포션을 사용하여 " + exp + " 경험치를 획득했습니다\n" + Emoji.LV + " 레벨: " + player.getDisplayLv() + "\n";
        });
        Config.unloadObject(item);

        item = new Item("중급 경험치 포션", "경험치를 제공해주는 포션이다");
        item.getHandleLv().set(5);
        item.setUse((self, other) -> {
            long exp = new Random().nextInt(10000 * self.getLv().get()) + 150_000;

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
            self.addItem(itemId, 1);

            return null;
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
        item.setUse((self, other) -> {
            //TODO
            return "TODO";
        });
        Config.unloadObject(item);

        item = new Item("중급 부적", "중급 부적 1개를 뽑을 수 있는 아이템이다");
        item.setUse((self, other) -> {
            //TODO
            return "TODO";
        });
        Config.unloadObject(item);

        item = new Item("상급 부적", "상급 부적 1개를 뽑을 수 있는 아이템이다");
        item.setUse((self, other) -> {
            //TODO
            return "TODO";
        });
        Config.unloadObject(item);

        item = new Item("양고기", "양에게서 나온 고기다");
        item.setCanEat(true);
        item.setEatBuff(100000, StatType.ATS, 20);
        item.setEatBuff(100000, StatType.ATK, 1);
        Config.unloadObject(item);
        
        createItem("양가죽", "양을 죽여서 얻은 가죽이다");
        createItem("양털", "양을 죽여서 얻은 털이다");

        Config.ID_COUNT.put(Id.ITEM, Math.max(Config.ID_COUNT.get(Id.ITEM), 147L));
        Logger.i("ObjectMaker", "Item making is done!");
    }

    private static void createEquips() {
        Config.ID_COUNT.put(Id.EQUIPMENT, Math.max(Config.ID_COUNT.get(Id.EQUIPMENT), 1L));
        Logger.i("ObjectMaker", "Equipment making is done!");
    }

    private static void createMonsters() {
        Monster monster = new Monster("양");
        monster.getId().setObjectId(ObjectList.monsterList.get(monster.getRealName()));
        monster.getLv().set(2);
        monster.setLocation(null);
        monster.setType(MonsterType.MIDDLE);

        //Stat
        monster.setBasicStat(StatType.MAXHP, 20);
        monster.setBasicStat(StatType.HP, 20);
        monster.setBasicStat(StatType.ATK, 3);
        monster.setBasicStat(StatType.DEF, 5);
        //---

        //Item Drop
        monster.setItemDrop(ItemList.LAMB.getId(), 0.5D, 1, 1);
        monster.setItemDrop(ItemList.SHEEP_LEATHER.getId(), 0.2D, 1, 1);
        monster.setItemDrop(ItemList.WOOL.getId(), 0.5D, 1, 3);
        //---

        Config.unloadObject(monster);

        Config.ID_COUNT.put(Id.MONSTER, Math.max(Config.ID_COUNT.get(Id.MONSTER), 2L));
        Logger.i("ObjectMaker", "Monster making is done!");
    }

    private static void createMaps() {
        GameMap map = new GameMap(ObjectList.mapList.get("0-0"));
        map.setMapType(MapType.COUNTRY);
        map.getLocation().set(0, 0, 1, 1);
        Config.unloadMap(map);

        map = new GameMap(ObjectList.mapList.get("0-1"));
        map.setMapType(MapType.SEA);
        map.getLocation().set(0, 1, 1, 1);
        Config.unloadMap(map);

        for(int y = 2; y <= 10; y++) {
            map = new GameMap(ObjectList.mapList.getOrDefault("0-" + y, Config.INCOMPLETE));
            map.getLocation().setMap(0, y);
            map.setMapType(MapType.FIELD);
            map.getLocation().set(0, y, 1, 1);
            Config.unloadMap(map);
        }

        map = new GameMap(ObjectList.mapList.get("1-0"));
        map.setMapType(MapType.FIELD);
        map.getLocation().set(1, 0, 1, 1);
        map.setSpawnMonster(1L, 1D, 4);
        Config.unloadMap(map);

        map = new GameMap(ObjectList.mapList.get("1-1"));
        map.setMapType(MapType.RIVER);
        map.getLocation().set(1, 1, 1, 1);
        Config.unloadMap(map);

        for(int x = 1; x <= 10; x++) {
            for(int y = 0; y <= 10; y++) {
                if(x == 1 && y <= 1) {
                    continue;
                }

                map = new GameMap(ObjectList.mapList.getOrDefault(x + "-" + y, Config.INCOMPLETE));
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

    private static void createChats() {
        Chat chat = new Chat();
        chat.getId().setObjectId(1L);
        chat.getText().addAll(Arrays.asList(
                "드디어 일어났네 __nickname",
                "이 소리가 어디서 들려오는지는 아직은 몰라도 되. 결국엔 알게 될테니까",
                "어찌됬든 넌 여기서 성장해야만 해. 그리고 니가 나한테 했던 약속을 지켜야겠지",
                "음 뭐가됬든 기본적인거부터 가르쳐줄게. " + Emoji.focus("n 도움말")
                        + " 을 입력해서 명령어를 살펴봐"
        ));
        chat.setAnyResponseChat("__도움말", 2L, true);
        chat.setAnyResponseChat("__명령어", 2L, true);
        chat.setAnyResponseChat("__?", 2L, true);
        chat.setAnyResponseChat("__h", 2L, true);
        chat.setAnyResponseChat("__help", 2L, true);
        Config.unloadObject(chat);

        chat = new Chat();
        chat.getId().setObjectId(2L);
        chat.getText().addAll(Arrays.asList(
                "좋아, 잘 따라오고 있네. 네 정보도 살펴봐야겠지?",
                "명령어 목록을 보고 네 정보를 표시하는 명령어를 사용해봐",
                "아 물론 거기 적혀있기도 하지만 네가 명령어 창을 연 것 처럼, " +
                        "모든 명령어에는 " + Emoji.focus("n") + "이나 " +
                        Emoji.focus("ㅜ") + "라는 글자가 붙으니까 기억해"
        ));
        chat.setAnyResponseChat("__정보", 3L, true);
        chat.setAnyResponseChat("__info", 3L, true);
        chat.setAnyResponseChat("__i", 3L, true);
        Config.unloadObject(chat);

        chat = new Chat();
        chat.getId().setObjectId(3L);
        chat.getText().addAll(Arrays.asList(
                "기본 정보랑 상세 정보로 나뉘어서 표시되는게 보이지?",
                "일단 너는 0-0-1-1, 그러니까 서남쪽 끝에 있는거고, 거점도 여기로 잡혀있어",
                "거점? 아 거점은 죽으면 태어나는 장소야. 어짜피 넌 내 권능때문에 죽을 수 없거든...",
                "마지막으로 간단한거 하나만 소개하고 가봐야겠네. 마을에서는 광질을 할 수 있으니까" +
                        " 광질 명령어를 입력해봐"
        ));
        chat.setAnyResponseChat("__광질", 4L, true);
        chat.setAnyResponseChat("__mine", 4L, true);
        Config.unloadObject(chat);

        chat = new Chat();
        chat.getId().setObjectId(4L);
        chat.getText().addAll(Arrays.asList(
                "그럼 난 가볼게. 어짜피 할 일도 많고 너도 이젠 혼자서 다 할 수 있을거 같으니까",
                "맵 정보를 확인하면 다른 주민들 이름도 보이니까 대화도 해보고 말이야",
                "뭐라도 주고 가라고? 골드를 조금 넣어놨으니까 그거라도 써",
                "아 그리고... ... ... (더 이상 들리지 않는다)"
        ));
        chat.getDelayTime().set(1500L);
        chat.getMoney().set(1000L);
        Config.unloadObject(chat);

        chat = new Chat();
        chat.getId().setObjectId(5L);
        chat.getText().addAll(Arrays.asList(
                "음? 못보던 얼굴이군",
                "그래 이름이 __nickname 이라고?",
                "난 이 '시작의 마을' 의 이장이라네. 잘 지네봄세",
                "우리 마을은 다양한 인종이나 종족이 있지",
                "한번 마을을 살펴보고 오는것도 좋을게야"
        ));
        Config.unloadObject(chat);

        chat = new Chat();
        chat.getId().setObjectId(6L);
        chat.getText().add("그래 무슨 일인가 __nickname?");
        chat.setBaseMsg(true);
        Config.unloadObject(chat);

        chat = new Chat("아무일도 아닙니다");
        chat.getId().setObjectId(7L);
        chat.getText().add("그래그래, 무슨 일 있으면 언제나 말 걸게나");
        Config.unloadObject(chat);

        chat = new Chat("광질을 하면서 할만한 퀘스트가 있을까요?");
        chat.getId().setObjectId(8L);
        chat.getText().addAll(Arrays.asList(
                "음, 아무래도 돌은 항상 부족해서 말이지",
                "돌 30개만 구해다 줄 수 있겠나?"
        ));
        chat.setResponseChat(WaitResponse.YES, 10L, true);
        chat.setResponseChat(WaitResponse.NO, 11L, true);
        Config.unloadObject(chat);

        chat = new Chat("낚시를 하면서 할만한 퀘스트가 있을까요?");
        chat.getId().setObjectId(9L);
        chat.getText().addAll(Arrays.asList(
                "요즘 바다나 강이 너무 더러워저셔 말일세",
                "쓰레기가 낚이는게 있으면 5개만 구해다 줄 수 있겠나?"
        ));
        chat.setResponseChat(WaitResponse.YES, 12L, true);
        chat.setResponseChat(WaitResponse.NO, 11L, true);
        Config.unloadObject(chat);

        chat = new Chat();
        chat.getId().setObjectId(10L);
        chat.getText().add("고맙네! 돌을 다 구하고 다시 말을 걸어주게나");
        chat.getQuestId().set(1L);
        Config.unloadObject(chat);

        chat = new Chat();
        chat.getId().setObjectId(11L);
        chat.getText().add("그럼 어쩔 수 없지. 나중에 마음 바뀌면 다시 받아가게나");
        Config.unloadObject(chat);

        chat = new Chat();
        chat.getId().setObjectId(12L);
        chat.getText().add("고맙네! 쓰레기를 다 수거하고 다시 말을 걸어주게나");
        chat.getQuestId().set(2L);
        Config.unloadObject(chat);

        chat = new Chat();
        chat.getId().setObjectId(13L);
        chat.getText().add("빨리 구해와줬군! 여기 보상이네");
        Config.unloadObject(chat);

        chat = new Chat();
        chat.getId().setObjectId(14L);
        chat.getText().add("좋은 아침이네 __nickname!\n그래 무슨 일인가?");
        chat.setBaseMsg(true);
        Config.unloadObject(chat);

        chat = new Chat();
        chat.getId().setObjectId(15L);
        chat.getText().add("점심은 먹었나 __nickname?\n그래 무슨 일로 왔나?");
        chat.setBaseMsg(true);
        Config.unloadObject(chat);

        chat = new Chat();
        chat.getId().setObjectId(16L);
        chat.getText().add("한적한 저녁이군\n무슨 일로 왔나 __nickname?");
        chat.setBaseMsg(true);
        Config.unloadObject(chat);
        
        chat = new Chat("얼굴에 걱정이 많아보이시는데, 무슨 일 있나요?");
        chat.getId().setObjectId(17L);
        chat.getText().addAll(Arrays.asList(
                "이런, 얼굴에 보였나? 사실 감기에 걸렸는데 방에 불을 아무리 떼도 추워서 말일세",
                "아무래도 불의 기운을 좀 쥐고 있으면 괜찮아질 것 같은데...",
                "붉은색 구체 하나만 구해줄 수 있겠나?"
        ));
        chat.setResponseChat(WaitResponse.YES, 24L, true);
        chat.setResponseChat(WaitResponse.NO, 18L, true);
        Config.unloadObject(chat);
        
        chat = new Chat();
        chat.getId().setObjectId(18L);
        chat.getText().add("그래 뭐... 바쁘다면 어쩔 수 없는 일이지");
        Config.unloadObject(chat);

        chat = new Chat();
        chat.getId().setObjectId(19L);
        chat.getText().addAll(Arrays.asList(
                "드디어 따뜻한 방에서 쉴 수 있겠구만",
                "여기 보상이네!"
        ));
        Config.unloadObject(chat);

        chat = new Chat("오히려 얼굴이 더 안좋아지신 것 같은데요..?");
        chat.getId().setObjectId(20L);
        chat.getText().addAll(Arrays.asList(
                "끄윽... 마침 잘왔네",
                "자네가 준 붉은색 구체가 너무 강해서 오히려 역효과가 나는 모양이야..",
                "마지막으로 하급 마나 포션 3개만 만들어와줄 수 있겠나?"
        ));
        chat.setResponseChat(WaitResponse.YES, 21L, true);
        chat.setResponseChat(WaitResponse.NO, 22L, true);
        Config.unloadObject(chat);
        
        chat = new Chat();
        chat.getId().setObjectId(21L);
        chat.getText().add("자꾸 번거롭게 해서 미안하네\n최대한 빨리 구해와주게나");
        chat.getQuestId().set(4L);
        Config.unloadObject(chat);

        chat = new Chat();
        chat.getId().setObjectId(22L);
        chat.getText().addAll(Arrays.asList(
                "40년만 젊었어도 직접 만들었을텐데...",
                "있는 일만 빨리 끝내고 다시 와주게"
        ));
        Config.unloadObject(chat);

        chat = new Chat();
        chat.getId().setObjectId(23L);
        chat.getText().addAll(Arrays.asList(
                "휴 이제야 살 것 같구만\n고맙네 __nickname",
                "이건 보상이니 받아가게나"
        ));
        Config.unloadObject(chat);

        chat = new Chat();
        chat.getId().setObjectId(24L);
        chat.getText().add("그래도 __nickname 자네가 구해준다니 마음이 놓이는구만");
        chat.getQuestId().set(3L);
        Config.unloadObject(chat);

        chat = new Chat();
        chat.getId().setObjectId(25L);
        chat.getText().addAll(Arrays.asList(
                "여행자인가? 하하",
                "반갑네, 난 이 마을 대장장이 형석이라고 한다",
                "마을을 둘러볼거면 내 아내 '엘' 에게도 가봤나 모르겠군"
        ));
        Config.unloadObject(chat);

        chat = new Chat();
        chat.getId().setObjectId(26L);
        chat.getText().add("무슨 일이지?");
        chat.setBaseMsg(true);
        Config.unloadObject(chat);
        
        chat = new Chat();
        chat.getId().setObjectId(27L);
        chat.getText().addAll(Arrays.asList(
                "이렇게 늦은 시간에 찾아오다니",
                "하하 역시 이 마을은 내가 없으면 안되는군",
                "그래서 무슨 일이지?"
        ));
        chat.setBaseMsg(true);
        Config.unloadObject(chat);

        chat = new Chat("아무일도 아닙니다");
        chat.getId().setObjectId(28L);
        chat.getText().add("흠... 싱겁긴");
        Config.unloadObject(chat);

        chat = new Chat();
        chat.getId().setObjectId(29L);
        chat.getText().add("... 안녕하세요");
        Config.unloadObject(chat);

        chat = new Chat();
        chat.getId().setObjectId(30L);
        chat.getText().add("엄마가 모르는 사람이랑 얘기하지 말랬어요...");
        chat.setBaseMsg(true);
        Config.unloadObject(chat);

        chat = new Chat("어... 그래");
        chat.getId().setObjectId(31L);
        chat.getText().add("...");
        Config.unloadObject(chat);
        
        chat = new Chat();
        chat.getId().setObjectId(32L);
        chat.getText().addAll(Arrays.asList(
                "음? 처음 보는 분이네요",
                "흐응, 그런 눈으로 보는걸 보니 엘프는 처음 보시나 보네요",
                "아무래도 인간들이 엘프를 처음 보면 그렇게 신기한 눈빛을 보내더라고요",
                "어쨌든 마을에 있는 동안 잘 부탁드려요"
        ));
        Config.unloadObject(chat);
        
        chat = new Chat();
        chat.getId().setObjectId(33L);
        chat.getText().add("안녕하세요");
        chat.setBaseMsg(true);
        Config.unloadObject(chat);

        chat = new Chat("아 그냥 지나가다 들렀습니다");
        chat.getId().setObjectId(34L);
        chat.getText().addAll(Arrays.asList(
                "오늘도 부지런하시네요",
                "가끔은 여유를 가지는 것도 좋아요",
                "다음번에 오실 땐 꽃 사러 오세요~"
        ));
        Config.unloadObject(chat);
        
        chat = new Chat();
        chat.getId().setObjectId(35L);
        chat.getText().addAll(Arrays.asList(
                "...............",
                "아 멍때리고 있었어 미안",
                "__nickname 이라고? 그래 잘 부탁해",
                "너도 시간나면 여기서 낚시나 해보라구"
        ));
        Config.unloadObject(chat);

        chat = new Chat();
        chat.getId().setObjectId(36L);
        chat.getText().add("음? 왜?");
        chat.setBaseMsg(true);
        Config.unloadObject(chat);
        
        chat = new Chat("그냥 뭐하나 싶어서");
        chat.getId().setObjectId(37L);
        chat.getText().addAll(Arrays.asList(
                "엄... 나야 뭐 항상 여기서 낚싯대나 드리우고 있지",
                "그럼 잘가"
        ));
        Config.unloadObject(chat);

        chat = new Chat();
        chat.getId().setObjectId(38L);
        chat.getText().add(".....");
        chat.setBaseMsg(true);
        Config.unloadObject(chat);

        chat = new Chat("이 시간까지 낚시를 하고 있는거야?");
        chat.getId().setObjectId(39L);
        chat.getText().add("zzz.......");
        Config.unloadObject(chat);

        Config.ID_COUNT.put(Id.CHAT, Math.max(Config.ID_COUNT.get(Id.CHAT), 25L));
        Logger.i("ObjectMaker", "Chat making is done!");
   }

   private static void createQuests() {
       Quest quest = new Quest("광부의 일", 3L, 13L);
       quest.getId().setObjectId(ObjectList.questList.get(quest.getName()));
       quest.setNeedItem(ItemList.STONE.getId(), 30);
       quest.setRewardCloseRate(3L, 1, true);
       quest.getRewardExp().set(20000L);
       quest.getRewardMoney().set(250L);
       Config.unloadObject(quest);

       quest = new Quest("쓰레기 수거", 3L, 13L);
       quest.getId().setObjectId(ObjectList.questList.get(quest.getName()));
       quest.setNeedItem(ItemList.TRASH.getId(), 5);
       quest.setRewardCloseRate(3L, 1, true);
       quest.getRewardExp().set(50000L);
       Config.unloadObject(quest);

       quest = new Quest("불이 필요해!", 3L, 19L);
       quest.getId().setObjectId(ObjectList.questList.get(quest.getName()));
       quest.setNeedItem(ItemList.RED_SPHERE.getId(), 1);
       quest.setRewardCloseRate(3L, 5, true);
       quest.getRewardMoney().set(500L);
       Config.unloadObject(quest);
       
       quest = new Quest("불이 너무 강했나...?", 3L, 23L);
       quest.getId().setObjectId(ObjectList.questList.get(quest.getName()));
       quest.setNeedItem(ItemList.LOW_MP_POTION.getId(), 3);
       quest.setRewardCloseRate(3L, 10, true);
       quest.getRewardItem().put(ItemList.STAT_POINT.getId(), 20);
       Config.unloadObject(quest);

       Config.ID_COUNT.put(Id.QUEST, Math.max(Config.ID_COUNT.get(Id.CHAT), 5L));
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

        npc.setCommonChat(new ChatLimit(), 6L);

        ChatLimit chatLimit = new ChatLimit();
        chatLimit.getLimitHour1().set(7, 11);
        npc.setCommonChat(chatLimit, 14L);

        chatLimit = new ChatLimit();
        chatLimit.getLimitHour1().set(12, 14);
        npc.setCommonChat(chatLimit, 15L);

        chatLimit = new ChatLimit();
        chatLimit.getLimitHour1().set(18, 22);
        npc.setCommonChat(chatLimit, 16L);

        npc.setChat(new ChatLimit(), 7L);

        chatLimit = new ChatLimit();
        chatLimit.getNotRunningQuest().add(1L);
        npc.setChat(chatLimit, 8L);

        chatLimit = new ChatLimit();
        chatLimit.getNotRunningQuest().add(2L);
        npc.setChat(chatLimit, 9L);

        chatLimit = new ChatLimit();
        chatLimit.getNotRunningQuest().add(3L);
        chatLimit.getClearedQuest().put(1L, 5);
        chatLimit.getClearedQuest().put(2L, 5);
        chatLimit.getNotClearedQuest().add(3L);
        npc.setChat(chatLimit, 17L);

        chatLimit = new ChatLimit();
        chatLimit.getNotRunningQuest().add(4L);
        chatLimit.getClearedQuest().put(3L, 1);
        chatLimit.getNotClearedQuest().add(4L);
        npc.setChat(chatLimit, 20L);

        Config.unloadObject(npc);


        npc = new Npc("형석");
        npc.getLocation().set(0, 0, 40, 40);
        npc.setFirstChat(25L);

        npc.setCommonChat(new ChatLimit(), 26L);

        chatLimit = new ChatLimit();
        chatLimit.getLimitHour1().set(22, 23);
        chatLimit.getLimitHour2().set(0, 6);
        npc.setCommonChat(chatLimit, 27L);

        npc.setChat(new ChatLimit(), 28L);

        Config.unloadObject(npc);


        npc = new Npc("봄이");
        npc.getLocation().set(0, 0, 41, 40);
        npc.setFirstChat(29L);

        chatLimit = new ChatLimit();
        chatLimit.getLimitHour1().set(6, 23);
        npc.setCommonChat(chatLimit, 30L);

        npc.setChat(new ChatLimit(), 31L);

        Config.unloadObject(npc);


        npc = new Npc("엘");
        npc.getLocation().set(0, 0, 41, 40);
        npc.setFirstChat(32L);

        chatLimit = new ChatLimit();
        chatLimit.getLimitHour1().set(6, 23);
        npc.setCommonChat(chatLimit, 33L);

        npc.setChat(new ChatLimit(), 34L);

        Config.unloadObject(npc);


        npc = new Npc("준식");
        npc.getLocation().set(1, 1,32, 32);
        npc.setFirstChat(35L);

        chatLimit = new ChatLimit();
        chatLimit.getLimitHour1().set(8, 22);
        npc.setCommonChat(chatLimit, 36L);

        chatLimit = new ChatLimit();
        chatLimit.getLimitHour1().set(23, 23);
        chatLimit.getLimitHour2().set(0, 7);
        npc.setCommonChat(chatLimit, 38L);

        chatLimit = new ChatLimit();
        chatLimit.getLimitHour1().set(8, 22);
        npc.setChat(chatLimit, 37L);

        chatLimit = new ChatLimit();
        chatLimit.getLimitHour1().set(23, 23);
        chatLimit.getLimitHour2().set(0, 7);
        npc.setChat(chatLimit, 39L);

        Config.unloadObject(npc);


        //TODO
        npc = new Npc("강태공");
        npc.getLocation().set(0, 1,32, 32);
        Config.unloadObject(npc);


        npc = new Npc("페드로");
        npc.getLocation().set(0, 0,64, 64);
        Config.unloadObject(npc);


        npc = new Npc("무명");
        npc.getLocation().set(1, 0,1, 1);
        Config.unloadObject(npc);


        npc = new Npc("셀리나");
        npc.getLocation().set(1, 0,2, 2);
        Config.unloadObject(npc);


        Config.ID_COUNT.put(Id.NPC, Math.max(Config.ID_COUNT.get(Id.NPC), 4L));
        Logger.i("ObjectMaker", "Npc making is done!");
    }

}